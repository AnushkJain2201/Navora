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
    "minaret": "Q48324"
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

MUST_INCLUDE_QIDS = {
    "Q45957": "monument",   # Red Fort 
    "Q187635": "monument",     # Qutub Minar
    "Q185374": "temple",     # Lotus Temple
    "Q178932": "mosque",     # Jama Masjid
    "Q422420": "monument",     # India Gate
    "Q771955": "tomb",     # Humayun's Tomb
}

def fetch_single_landmark(qid: str, category: str) -> dict | None:
    query = f"""
    SELECT ?placeLabel ?coord ?description WHERE {{
      BIND(wd:{qid} AS ?place)
      ?place wdt:P625 ?coord .
      OPTIONAL {{ ?place schema:description ?description . FILTER(LANG(?description) = "en") }}
      SERVICE wikibase:label {{ bd:serviceParam wikibase:language "en". }}
    }}
    """
    response = fetch_with_retry(
        WIKIDATA_ENDPOINT,
        params={"query": query, "format": "json"},
        headers={"User-Agent": "NavoraDataSeeder/1.0 (student portfolio project)"},
    )
    bindings = response.json()["results"]["bindings"]

    if not bindings:
        return None

    row = bindings[0]
    coord_str = row["coord"]["value"]
    lon, lat = coord_str.replace("Point(", "").replace(")", "").split(" ")

    return {
        "wikidata_id": qid,
        "name": row["placeLabel"]["value"],
        "latitude": float(lat),
        "longitude": float(lon),
        "wikidata_description": row.get("description", {}).get("value", ""),
        "category": category,
    }

def fetch_with_retry(url, params, headers, max_retries=3):
    retryable_statuses = {429, 500, 502, 503, 504}

    for attempt in range(max_retries):
        response = requests.get(url, params=params, headers=headers)

        if response.status_code in retryable_statuses:
            wait_time = 5 * (attempt + 1)
            print(f"    Got {response.status_code}, waiting {wait_time}s before retry...")
            time.sleep(wait_time)
            continue

        response.raise_for_status()
        return response

    raise Exception(f"Max retries exceeded, last status was transient error")

def fetch_category(category_qid: str) -> list[dict]:
    query = build_query(category_qid)
    response = fetch_with_retry(
        WIKIDATA_ENDPOINT,
        params={"query": query, "format": "json"},
        headers={"User-Agent": "NavoraDataSeeder/1.0"},
    )
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
        time.sleep(1.5)

    print("  Fetching must-include landmarks...")
    for qid, category in MUST_INCLUDE_QIDS.items():
        landmark = fetch_single_landmark(qid, category)
        if landmark:
            place_uri = f"http://www.wikidata.org/entity/{qid}"
            all_results[place_uri] = landmark
        time.sleep(2)

    return list(all_results.values())
