import requests
import time

WIKIPEDIA_SUMMARY_URL = "https://en.wikipedia.org/api/rest_v1/page/summary/{title}"

def fetch_wikipedia_summary(landmark_name: str) -> str | None:
    url = WIKIPEDIA_SUMMARY_URL.format(title=landmark_name.replace(" ", "_"))

    try:
        response = requests.get(
            url,
            headers={"User-Agent": "NavoraDataSeeder/1.0 (student portfolio project)"},
            timeout=5,
        )
        if response.status_code != 200:
            return None

        data = response.json()

        if data.get("type") == "disambiguation":
            return None  # ambiguous title, can't safely use this

        extract = data.get("extract", "").strip()
        return extract if extract else None

    except requests.RequestException:
        return None
    finally:
        time.sleep(0.2)  # gentle rate limiting, Wikipedia's API is generous but let's be polite