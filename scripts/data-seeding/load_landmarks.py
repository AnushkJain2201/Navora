import os
import psycopg2
from dotenv import load_dotenv
from wikidata_client import fetch_all_landmarks
from wikipedia_client import fetch_wikipedia_summary

load_dotenv()

def get_connection():
    return psycopg2.connect(
        host=os.getenv("DB_HOST"),
        port=os.getenv("DB_PORT"),
        dbname=os.getenv("DB_NAME"),
        user=os.getenv("DB_USER"),
        password=os.getenv("DB_PASSWORD"),
    )

def landmark_exists(cursor, name: str, country: str) -> bool:
    cursor.execute(
        "SELECT 1 FROM landmarks WHERE name = %s AND country = %s LIMIT 1",
        (name, country),
    )
    return cursor.fetchone() is not None

def resolve_final_description(landmark: dict) -> str | None:
    wikipedia_extract = fetch_wikipedia_summary(landmark["name"])
    if wikipedia_extract:
        return wikipedia_extract

    if landmark["wikidata_description"]:
        return landmark["wikidata_description"]

    return None  # no usable description from either source

def insert_landmark(cursor, landmark: dict, description: str, country: str):
    cursor.execute(
        """
        INSERT INTO landmarks (name, country, location, category, description)
        VALUES (%s, %s, ST_SetSRID(ST_MakePoint(%s, %s), 4326)::geography, %s, %s)
        """,
        (
            landmark["name"],
            country,
            landmark["longitude"],
            landmark["latitude"],
            landmark["category"],
            description,
        ),
    )

def main():
    print("Fetching landmarks from Wikidata...")
    landmarks = fetch_all_landmarks()
    print(f"Found {len(landmarks)} candidate landmarks")

    conn = get_connection()
    cursor = conn.cursor()

    inserted = 0
    skipped = 0

    for i, landmark in enumerate(landmarks, start=1):
        print(f"[{i}/{len(landmarks)}] {landmark['name']}...")

        # Rough country assignment based on longitude, since India/Egypt don't overlap
        country = "Egypt" if landmark["longitude"] < 50 else "India"

        if landmark_exists(cursor, landmark["name"], country):
            print("  -> skipped (already in database)")
            skipped += 1
            continue

        description = resolve_final_description(landmark)
        if not description:
            print("  -> skipped (no description available)")
            skipped += 1
            continue

        insert_landmark(cursor, landmark, description, country)
        inserted += 1

    conn.commit()
    cursor.close()
    conn.close()

    print(f"\nDone. Inserted {inserted}, skipped {skipped}.")

if __name__ == "__main__":
    main()