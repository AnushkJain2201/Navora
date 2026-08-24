from app.nodes import identify_landmark
from app.models import ScanIdentifyRequest
from app.models import IdentifiedLandmark
from app.models import TripPlanResponse
from app.graph import trip_graph
from fastapi import FastAPI
from app.models import TripRequest, TripResponse
from app.llm_client import get_trip_response

app = FastAPI(title="Navora AI Service")

@app.get("/health")
def health_check():
    return {"status": "ok"}


@app.post("/generate", response_model=TripPlanResponse)
def generate_trip(request: TripRequest):
    initial_state = {
        "query": request.query,
        "destination": None,
        "duration_days": None,
        "budget": None,
        "itinerary": None,
        "clarification_message": None,
    }

    final_state = trip_graph.invoke(initial_state)

    return TripPlanResponse(
        destination=final_state.get("destination"),
        duration_days=final_state.get("duration_days"),
        budget=final_state.get("budget"),
        itinerary=final_state.get("itinerary"),
        clarification_message=final_state.get("clarification_message"),
    )

@app.post("/identify", response_model=IdentifiedLandmark)
def identify(request: ScanIdentifyRequest):
    return identify_landmark(request)