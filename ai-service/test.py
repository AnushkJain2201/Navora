from app.nodes import parse_intent

# test_state = {
#     "query": "3 days in Egypt, budget 80k",
#     "destination": None,
#     "duration_days": None,
#     "budget": None,
#     "itinerary": None,
#     "clarification_message": None,
# }

test_state_incomplete = {
    "query": "I want to go somewhere historic",
    "destination": None,
    "duration_days": None,
    "budget": None,
    "itinerary": None,
    "clarification_message": None,
}

result = parse_intent(test_state_incomplete)
print(result)

result = parse_intent(test_state_incomplete)
print(result)