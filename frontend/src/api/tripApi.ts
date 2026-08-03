const BASE_URL = "http://localhost:8080";

interface TripPlanResponse {
    tripId: string | null
    destination: string | null
    durationDays: number | null
    budget: number | null
    itineraryDays: ItineraryDay[] | null
    clarificationMessage: string | null
}

interface ItineraryDay {
  id: string
  dayNumber: number
  stops: ItineraryStop[]
}

interface ItineraryStop {
  id: string
  landmarkName: string
  description: string
  estimatedDurationHours: number | null
  orderIndex: number
}

export async function planTrip(query: string, token: string): Promise<TripPlanResponse> {
    const response = await fetch(`${BASE_URL}/api/trips/plan`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify({ query }),
    });

    if (!response.ok) {
        throw new Error("Failed to plan trip");
    }

    return response.json();
}