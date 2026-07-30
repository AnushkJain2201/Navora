from langgraph.graph import StateGraph, END
from app.state import TripState
from app.nodes import parse_intent, ask_clarification, generate_itinerary, route_after_intent

graph_builder = StateGraph(TripState)

graph_builder.add_node("parse_intent", parse_intent)
graph_builder.add_node("ask_clarification", ask_clarification)
graph_builder.add_node("generate_itinerary", generate_itinerary)

graph_builder.set_entry_point("parse_intent")

graph_builder.add_conditional_edges("parse_intent", route_after_intent, {
    "generate_itinerary": "generate_itinerary",
    "ask_clarification": "ask_clarification",
})

graph_builder.add_edge("ask_clarification", END)
graph_builder.add_edge("generate_itinerary", END)

trip_graph = graph_builder.compile()
