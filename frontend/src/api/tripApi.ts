const BASE_URL = "http://localhost:8080";

export interface TripPlanResponse {
    tripId: string | null
    destination: string | null
    durationDays: number | null
    budget: number | null
    itineraryDays: ItineraryDay[] | null
    clarificationMessage: string | null
}

export interface ItineraryDay {
  id: string
  dayNumber: number
  stops: ItineraryStop[]
}

export interface ItineraryStop {
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
        const errorBody = await response.json().catch(() => ({ error: "Request failed" }))
        throw new Error(errorBody.error ?? `Request failed: ${response.status}`)
    }
    
    return response.json();
}