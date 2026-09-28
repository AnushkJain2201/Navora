from app.embedding_client import generate_embedding
import os
import psycopg2
from dotenv import load_dotenv

load_dotenv()

def get_connection():
    return psycopg2.connect(
        host=os.getenv("DB_HOST"),
        port=os.getenv("DB_PORT"),
        user=os.getenv("DB_USER"),
        password=os.getenv("DB_PASSWORD"),
        dbname=os.getenv("DB_NAME"),
    )

def retrieve_relevant_landmarks(query: str, top_k: int = 5) -> list[dict]:
    query_embedding = generate_embedding(query)

    conn = get_connection()
    cursor = conn.cursor()

    cursor.execute(
        """
        SELECT
            l.id,
            l.name,
            l.category,
            l.country,
            l.description,
            le.embedding <=> %s::vector AS distance
        FROM landmark_embeddings le
        JOIN landmarks l ON l.id = le.landmark_id
        ORDER BY le.embedding <=> %s::vector
        LIMIT %s
        """,
        (query_embedding, query_embedding, top_k),
    )

    rows = cursor.fetchall()
    cursor.close()
    conn.close()

    return [
        {
            "id": str(row[0]),
            "name": row[1],
            "category": row[2],
            "country": row[3],
            "description": row[4],
            "distance": row[5],
        }
        for row in rows
    ]

    
