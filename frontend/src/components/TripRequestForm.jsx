import { useState } from "react";
import { planTrip } from "../api/tripApi";

function TripRequestForm() {
    const [query, setQuery] = useState("");
    const [loading, setLoading] = useState(false);
    const [response, setResponse] = useState(null);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError("");
        setResponse("");

        try {
            const data = await planTrip(query);
            console.log(data);
            setResponse(data.raw_response);
        } catch (err) {
            setError("Something went wrong. Check the console.");
            console.error(err);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <input type="text" 
                    placeholder="e.g. 3 days in Egypt, budget 80k"
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                />
                <button type="submit" disabled={loading} >
                    {loading ? "Planning..." : "Plan my Trip"}
                </button>
            </form>

            {error && <p style={{ color: "red" }}>{error}</p>}
            {response && <p>{response}</p>}
        </div>
    )
}

export default TripRequestForm

