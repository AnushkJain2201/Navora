from pydantic import BaseModel

class TripRequest(BaseModel):
    query: str

class TripResponse(BaseModel):
    raw_response: str