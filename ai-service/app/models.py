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