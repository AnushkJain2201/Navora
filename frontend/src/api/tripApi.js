const BASE_URL = "http://localhost:8080/api/trips";

export async function planTrip(query) {
    const response = await fetch(`${BASE_URL}/plan`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ query }),
    });

    if (!response.ok) {
        throw new Error("Failed to plan trip");
    }

    return response.json();
}