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
