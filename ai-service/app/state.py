from typing import List
from typing import Optional
from typing import TypedDict
from typing import Dict

# TripState — this is the LangGraph state object, the "shared form" that flows through every node, accumulating fields as it goes.
class TripState(TypedDict):
    query: str
    destination: Optional[str]
    duration_days: Optional[int]
    budget: Optional[float]
    itinerary: Optional[List[Dict]]
    clarification_message: Optional[str]