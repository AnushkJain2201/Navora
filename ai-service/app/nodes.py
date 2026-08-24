from langchain_core.messages import HumanMessage
from langchain_core.prompts import message
from app.models import ScanIdentifyRequest
from app.models import IdentifiedLandmark
from app.models import GeneratedItinerary
from dotenv import load_dotenv
load_dotenv()

from app.state import TripState
from langchain_core.prompts import ChatPromptTemplate
from app.models import ParsedIntent
from langchain.chat_models import init_chat_model
import os

## Intent Parsing Node

intent_llm = init_chat_model(
    "gpt-4o-mini",
    model_provider="openai",
    api_key=os.getenv("OPENAI_API_KEY"),
    temperature=0
)

structured_intent_llm = intent_llm.with_structured_output(ParsedIntent)

intent_prompt = ChatPromptTemplate.from_messages([
    ("system",
     "You are a travel planning assistant. Extract the destination, "
     "trip duration in days, and total budget as a plain number from the "
     "user's request. If any of these are not clearly stated, leave that "
     "field empty rather than guessing."),
    ("user", "{query}"),
])

intent_chain = intent_prompt | structured_intent_llm

def parse_intent(state: TripState)-> dict:
    result: ParsedIntent = intent_chain.invoke({"query": state["query"]})
    return {
        "destination": result.destination,
        "duration_days": result.duration_days,
        "budget": result.budget
    }

## Clarification Asking Node

def ask_clarification(state: TripState) -> dict:
    missing = []

    if not state.get("destination"):
        missing.append("destination")
    
    if not state.get("duration_days"):
        missing.append("duration_days")
    
    if not state.get("budget"):
        missing.append("budget")
    
    message = (
        f"I need a bit more information to plan your trip. "
        f"Could you tell me your {', '.join(missing)}?"
    )
    
    return {"clarification_message": message}

## Routing Function
def route_after_intent(state: TripState) -> str:
    if state.get("destination") and state.get("duration_days") and state.get("budget"):
        return "generate_itinerary"
    return "ask_clarification"

## Generate Itinerary Node
itinerary_llm = init_chat_model(
    "gpt-4o",
    model_provider="openai",
    api_key=os.getenv("OPENAI_API_KEY"),
    temperature=0.7,
)

structured_itinerary_llm = itinerary_llm.with_structured_output(GeneratedItinerary)

itinerary_prompt = ChatPromptTemplate.from_messages([
    ("system",
     "You are an expert travel planner specializing in historical and cultural tourism. "
     "Create a day-by-day itinerary based on the given destination, duration, and budget. "
     "Focus on historically significant sites. Keep the plan realistic — "
     "don't overpack a single day with more than 3-4 major stops. "
     "IMPORTANT: You must generate exactly {duration_days} day entries in the 'days' list, "
     "numbered sequentially from 1 to {duration_days}. Do not stop early — "
     "every single day of the trip must have its own entry."),
    ("user",
     "Destination: {destination}\n"
     "Duration: {duration_days} days\n"
     "Budget: {budget}\n"
     "Plan a historically-focused itinerary covering all {duration_days} days."),
])

itinerary_chain = itinerary_prompt | structured_itinerary_llm

def generate_itinerary(state: TripState) -> dict:
    expected_days = state["duration_days"]

    result: GeneratedItinerary = itinerary_chain.invoke({
        "destination": state["destination"],
        "duration_days": expected_days,
        "budget": state["budget"],
    })

    if len(result.days) != expected_days:
        result = itinerary_chain.invoke({
            "destination": state["destination"],
            "duration_days": expected_days,
            "budget": state["budget"],
        })

    if len(result.days) != expected_days:
        raise ValueError(
            f"Itinerary generation failed to produce {expected_days} days "
            f"after retry (got {len(result.days)})"
        )

    itinerary_as_dicts = [day.model_dump() for day in result.days]
    return {"itinerary": itinerary_as_dicts}

# Vision LLM nodes
vision_llm = init_chat_model(
    "gpt-4o-mini",
    model_provider="openai",
    api_key=os.getenv("OPENAI_API_KEY"),
    temperature=0.5,
)

structured_vision_llm = vision_llm.with_structured_output(IdentifiedLandmark)

def identify_landmark(request: ScanIdentifyRequest) -> IdentifiedLandmark:
    candidates_text = "\n".join(
        f"ID: {c.id}\tName: {c.name}\tCategory: {c.category}\tDescription: {c.description}" 
        for c in request.candidates
    )

    prompt_text = (
    "You are helping a tourist identify exactly what they're looking at in a photo. "
    "Based on GPS location, the photo was taken at or near one of these landmarks:\n\n"
    f"{candidates_text}\n\n"
    "Look closely at the photo. First, determine which candidate landmark complex it "
    "belongs to. Then, using your own knowledge of that landmark, identify the SPECIFIC "
    "feature visible in the photo — for example a particular gate, courtyard, hall, "
    "mural, or structure — not just the landmark as a whole. Many landmarks like forts "
    "and palaces have multiple famous named features within them.\n\n"
    "If you can identify a specific feature, name it explicitly and start with 'you are looking at <specific feature> at <landmark name>' and then write 2-3 interesting facts "
    "specifically about THAT feature (its history, significance, what to notice about "
    "its design) — not a general overview of the parent landmark. If you genuinely "
    "cannot identify a specific feature and the photo just shows the landmark generally, "
    "say so and give general context instead."
)

    message = HumanMessage(content=[
        {"type": "text", "text": prompt_text},
        {
            "type": "image_url",
            "image_url": {
                "url": f"data:image/jpg;base64,{request.image_base64}",
            }
        }
    ])

    result: IdentifiedLandmark = structured_vision_llm.invoke([message])
    return result