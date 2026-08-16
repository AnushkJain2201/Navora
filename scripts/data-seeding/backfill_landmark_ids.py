import os
import psycopg2
from dotenv import load_dotenv
import re

load_dotenv()

def get_connection():
    return psycopg2.connect(
        host=os.getenv("DB_HOST"),
        port=os.getenv("DB_PORT"),
        dbname=os.getenv("DB_NAME"),
        user=os.getenv("DB_USER"),
        password=os.getenv("DB_PASSWORD"),
    )

STOP_WORDS = {"the", "of", "a", "an", "at", "in"}

def normalize_and_tokenize(name: str) -> set[str]:
    words = re.findall(r"[a-z0-9]+", name.lower())
    return {w for w in words if w not in STOP_WORDS}

def get_unlinked_stops(cursor):
    cursor.execute("""
        SELECT id, landmark_name
        FROM itinerary_stops
        WHERE landmark_id IS NULL
    """)
    return cursor.fetchall()

def find_matching_landmark(cursor, stop_name: str):
    cursor.execute(
        "SELECT id FROM landmarks WHERE LOWER(name) = LOWER(%s) LIMIT 1",
        (stop_name,),
    )
    result = cursor.fetchone()
    if result:
        return result[0]

    cursor.execute("SELECT id, name FROM landmarks")
    all_landmarks = cursor.fetchall()

    stop_words = normalize_and_tokenize(stop_name)
    best_match = None
    best_overlap = 0

    for landmark_id, landmark_name in all_landmarks:
        landmark_words = normalize_and_tokenize(landmark_name)
        overlap = len(stop_words & landmark_words)

        if overlap > best_overlap and overlap >= 1:
            best_overlap = overlap
            best_match = landmark_id

    return best_match

def main():
    conn = get_connection()
    cursor = conn.cursor()

    stops = get_unlinked_stops(cursor)
    print(f"Found {len(stops)} stops without a linked landmark")

    matched = 0
    unmatched = 0

    for stop_id, stop_name in stops:
        landmark_id = find_matching_landmark(cursor, stop_name)

        if landmark_id:
            cursor.execute(
                "UPDATE itinerary_stops SET landmark_id = %s WHERE id = %s",
                (landmark_id, stop_id),
            )
            matched += 1
        else:
            print(f"  No match found for: {stop_name}")
            unmatched += 1

    conn.commit()
    cursor.close()
    conn.close()

    print(f"\nDone. Matched {matched}, unmatched {unmatched}.")

if __name__ == "__main__":
    main()