import { FormEvent, useState } from "react";
import { planTrip } from "../api/tripApi";

function TripRequestForm() {
    const [query, setQuery] = useState<string>("");
    const [loading, setLoading] = useState<boolean>(false);
    const [response, setResponse] = useState<string>("");
    const [error, setError] = useState<string>("");

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setLoading(true);
        setError("");
        setResponse("");

        try {
            const token = localStorage.getItem("token") ?? "";
            const data = await planTrip(query, token);
            console.log(data);
            setResponse(JSON.stringify(data, null, 2));
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
            {response && <pre>{response}</pre>}
        </div>
    )
}

export default TripRequestForm

