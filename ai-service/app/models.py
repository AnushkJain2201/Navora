from os import name
from typing import List, Dict
from pydantic import Field
from typing import Optional
from pydantic import BaseModel

class TripRequest(BaseModel):
    query: str

class TripResponse(BaseModel):
    raw_response: str

# ParsedIntent — this is a structured output model, used only inside parse_intent to force the LLM's response into a clean shape. It's narrower than the state — just the fields that one specific LLM call needs to produce.
class ParsedIntent(BaseModel):
    destination: Optional[str] = Field(
        default=None, description="The country or city, user wants to visit."
    )
    duration_days: Optional[int] = Field(
        default=None, description="Number of days for the trip."
    )
    budget: Optional[float] = Field(
        default=None, description="Total trip budget in the currency mentioned, as a plain number."
    )

# Models for the Generate Itinerary Node
class ItineraryStop(BaseModel):
    name: str = Field(description="Name of the site or landmark to visit")
    description: str = Field(description="A short 1-2 sentence historical or cultural note about this stop")
    estimated_duration_hours: float = Field(description="Roughly how many hours to spend here")

class ItineraryDay(BaseModel):
    day_number: int = Field(description="Which day of the trip this is, starting from 1")
    theme: str = Field(description="A short theme or focus for the day, e.g. 'Ancient temples' or 'Old city exploration'")
    stops: List[ItineraryStop] = Field(description="Ordered list of places to visit this day")

class GeneratedItinerary(BaseModel):
    days: List[ItineraryDay] = Field(description="The full day-by-day plan")
    budget_note: str = Field(description="A brief note on how the budget was considered in planning")

# TripResponse — this is the external-facing response model. It is deliberately richer and more user-friendly than the internal TripState.
class TripPlanResponse(BaseModel):
    destination: Optional[str] = None
    duration_days: Optional[int] = None
    budget: Optional[float] = None
    itinerary: Optional[List[Dict]] = None
    clarification_message: Optional[str] = None    

## Models for the response of scanned pictures
class LandmarkCandidate(BaseModel):
    id: str
    name: str
    category: str
    description: str

class ScanIdentifyRequest(BaseModel):
    image_base64: str
    candidates: List[LandmarkCandidate]

class IdentifiedLandmark(BaseModel):
    matched: bool = Field(description="Whether the photo confidently matches within one of the candidate landmark complexes")
    landmark_id: Optional[str] = Field(default=None, description="The id of the matched parent landmark, if any")
    landmark_name: Optional[str] = Field(default=None, description="The name of the matched parent landmark, if any")
    specific_feature: Optional[str] = Field(
        default=None, description="The specific structure, gate, courtyard, or feature actually visible in the photo, if identifiable (e.g. 'Ganesh Pol', 'Sheesh Mahal'). Null if the photo shows the landmark generally rather than a specific named feature."
    )
    confidence_reason: str = Field(description="Brief explanation of the match")
    generated_context: Optional[str] = Field(
        default=None, description="small description about the feature and 2-3 specific, interesting facts about exactly what's visible in the photo — the specific feature if one is identified, not a general overview of the parent landmark"
    )
