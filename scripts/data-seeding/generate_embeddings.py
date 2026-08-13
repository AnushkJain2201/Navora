import os
import psycopg2
from dotenv import load_dotenv
from embedding_client import generate_embedding

load_dotenv()

def get_connection():
    return psycopg2.connect(
        host=os.getenv("DB_HOST"),
        port=os.getenv("DB_PORT"),
        dbname=os.getenv("DB_NAME"),
        user=os.getenv("DB_USER"),
        password=os.getenv("DB_PASSWORD"),
    )

def fetch_landmarks_without_embeddings(cursor):
    cursor.execute("""
        SELECT l.id, l.name, l.description
        FROM landmarks l
        LEFT JOIN landmark_embeddings le ON le.landmark_id = l.id
        WHERE le.id IS NULL
    """)
    return cursor.fetchall()

def insert_embedding(cursor, landmark_id: str, embedding: list[float]):
    cursor.execute(
        "INSERT INTO landmark_embeddings (landmark_id, embedding) VALUES (%s, %s)",
        (landmark_id, embedding),
    )

def main():
    conn = get_connection()
    cursor = conn.cursor()

    landmarks = fetch_landmarks_without_embeddings(cursor)
    print(f"Found {len(landmarks)} landmarks needing embeddings")

    for i, (landmark_id, name, description) in enumerate(landmarks, start=1):
        print(f"[{i}/{len(landmarks)}] {name}...")

        text_to_embed = f"{name}: {description}"
        embedding = generate_embedding(text_to_embed)

        insert_embedding(cursor, landmark_id, embedding)
        conn.commit()  # commit per-row here, unlike load_landmarks.py

    cursor.close()
    conn.close()
    print("\nDone.")

if __name__ == "__main__":
    main()