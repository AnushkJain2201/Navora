# from app.nodes import parse_intent

## Test for checking the parsing node

# test_state = {
#     "query": "3 days in Egypt, budget 80k",
#     "destination": None,
#     "duration_days": None,
#     "budget": None,
#     "itinerary": None,
#     "clarification_message": None,
# }

# test_state_incomplete = {
#     "query": "I want to go somewhere historic",
#     "destination": None,
#     "duration_days": None,
#     "budget": None,
#     "itinerary": None,
#     "clarification_message": None,
# }

# result = parse_intent(test_state_incomplete)
# print(result)

# result = parse_intent(test_state_incomplete)
# print(result)

## Test for checking the clarification node
# from app.nodes import route_after_intent, ask_clarification

# complete_state = {
#     "query": "3 days in Egypt, budget 80k",
#     "destination": "Egypt",
#     "duration_days": 3,
#     "budget": 80000.0,
#     "itinerary": None,
#     "clarification_message": None,
# }
# print(route_after_intent(complete_state))  # expect: "generate_itinerary"

# incomplete_state = {
#     "query": "I want to go somewhere historic",
#     "destination": None,
#     "duration_days": None,
#     "budget": None,
#     "itinerary": None,
#     "clarification_message": None,
# }
# print(route_after_intent(incomplete_state))  # expect: "ask_clarification"
# print(ask_clarification(incomplete_state))    # expect: a dict with a helpful message listing all 3 missing fields

## Test for generating itinerary node
# import os
# from dotenv import load_dotenv
# load_dotenv()
# os.environ["LANGSMITH_TRACING"] = "true"
# os.environ["LANGSMITH_API_KEY"] = os.getenv("LANGSMITH_API_KEY") or os.getenv("LANGCHAIN_API_KEY")
# os.environ["LANGSMITH_PROJECT"] = os.getenv("LANGSMITH_PROJECT") or os.getenv("LANGCHAIN_PROJECT")
 
# ## Usually optional for hosted LangSmith, but safe to set
# os.environ["LANGSMITH_ENDPOINT"] = os.getenv("LANGSMITH_ENDPOINT")
# from app.nodes import generate_itinerary

# test_state = {
#     "query": "3 days in India, budget 80k",
#     "destination": "India",
#     "duration_days": 3,
#     "budget": 80000.0,
#     "itinerary": None,
#     "clarification_message": None,
# }

# result = generate_itinerary(test_state)
# import json
# print(json.dumps(result, indent=2))


import base64

with open("ganesh-pol.jpg", "rb") as f:
    encoded = base64.b64encode(f.read()).decode("utf-8")
    print(encoded)