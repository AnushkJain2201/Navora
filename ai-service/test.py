from app.nodes import parse_intent

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
from app.nodes import route_after_intent, ask_clarification

complete_state = {
    "query": "3 days in Egypt, budget 80k",
    "destination": "Egypt",
    "duration_days": 3,
    "budget": 80000.0,
    "itinerary": None,
    "clarification_message": None,
}
print(route_after_intent(complete_state))  # expect: "generate_itinerary"

incomplete_state = {
    "query": "I want to go somewhere historic",
    "destination": None,
    "duration_days": None,
    "budget": None,
    "itinerary": None,
    "clarification_message": None,
}
print(route_after_intent(incomplete_state))  # expect: "ask_clarification"
print(ask_clarification(incomplete_state))    # expect: a dict with a helpful message listing all 3 missing fields

