from fastapi import FastAPI
from app.models import TripRequest, TripResponse
from app.llm_client import get_trip_response

app = FastAPI(title="Navora AI Service")

@app.get("/health")
def health_check():
    return {"status": "ok"}


@app.post("/generate", response_model=TripResponse)
def generate_trip(request: TripRequest):
    response = get_trip_response(request.query)
    return TripResponse(raw_response=response)