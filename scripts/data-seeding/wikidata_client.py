import requests
import time

WIKIDATA_ENDPOINT = "https://query.wikidata.org/sparql"

CATEGORIES = {
    "archaeological_site": "Q839954",
    "palace": "Q16560",
    "monument": "Q4989906",
    "tourist_attraction": "Q570116",
    "fortification": "Q57821",
    "temple": "Q44539",
    "mosque": "Q32815",
    "tomb": "Q381885",
    "mausoleum": "Q162875",
}

def build_query(category_qid: str) -> str:
    return f"""
    SELECT ?place ?placeLabel ?coord ?description WHERE {{
      VALUES ?country {{ wd:Q79 wd:Q668 }}
      ?place wdt:P17 ?country ;
             wdt:P31 wd:{category_qid} ;
             wdt:P625 ?coord .
      OPTIONAL {{ ?place schema:description ?description . FILTER(LANG(?description) = "en") }}
      SERVICE wikibase:label {{ bd:serviceParam wikibase:language "en". }}
    }}
    LIMIT 50
    """

def fetch_category(category_qid: str) -> list[dict]:
    query = build_query(category_qid)
    response = requests.get(
        WIKIDATA_ENDPOINT,
        params={"query": query, "format": "json"},
        headers={"User-Agent": "NavoraDataSeeder/1.0 (student portfolio project)"},
    )
    response.raise_for_status()
    return response.json()["results"]["bindings"]

def fetch_all_landmarks() -> list[dict]:
    all_results = {}

    for category_name, qid in CATEGORIES.items():
        print(f"  Fetching category: {category_name}...")
        rows = fetch_category(qid)

        for row in rows:
            place_uri = row["place"]["value"]

            if place_uri in all_results:
                continue

            coord_str = row["coord"]["value"]
            lon, lat = coord_str.replace("Point(", "").replace(")", "").split(" ")

            all_results[place_uri] = {
                "wikidata_id": place_uri.split("/")[-1],
                "name": row["placeLabel"]["value"],
                "latitude": float(lat),
                "longitude": float(lon),
                "wikidata_description": row.get("description", {}).get("value", ""),
                "category": category_name,
            }

        time.sleep(1.5)  # be polite to Wikidata's endpoint between category queries

    return list(all_results.values())