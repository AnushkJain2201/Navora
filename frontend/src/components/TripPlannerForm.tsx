import { FormEvent, useState } from "react";
import { planTrip, TripPlanResponse } from "../api/tripApi"
import { useAuth } from "@/context/AuthContext";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card"
import { Skeleton } from "@/components/ui/skeleton"
import { Button } from "@/components/ui/button"
import { Textarea } from "@/components/ui/textarea"

interface TripPlannerFormProps {
    onTripPlanned: (result: TripPlanResponse) => void;
}


export function TripPlannerForm({ onTripPlanned }: TripPlannerFormProps) {

    const [query, setQuery] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const { token } = useAuth();

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (!token) {
            setError("You must be logged in to plan a trip.");
            return;
        }

        setLoading(true);
        setError("");
        try {
            const result = await planTrip(query, token);
            onTripPlanned(result);

            if (!result.itineraryDays) {
                setError(result.clarificationMessage ?? "Need more information to plan this trip")
            }

        } catch (error) {
            setError(error instanceof Error ? error.message : "Failed to plan trip")
        } finally {
            setLoading(false);
        }
    }
    return (
        <Card>
            <CardHeader>
                <CardTitle>Plan a new trip</CardTitle>
                <CardDescription>
                    Tell us where you want to go, for how long, and your budget
                </CardDescription>
            </CardHeader>
            <CardContent>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <Textarea
                        placeholder="e.g. 3 days in Egypt, budget 80k"
                        value={query}
                        onChange={(e) => setQuery(e.target.value)}
                        rows={3}
                        required
                    />
                    {error && <p className="text-sm text-destructive">{error}</p>}
                    <Button type="submit" disabled={loading}>
                        {loading ? "Planning your trip..." : "Plan trip"}
                    </Button>
                </form>

                {loading && (
                    <div className="mt-6 space-y-3">
                        <Skeleton className="h-4 w-3/4" />
                        <Skeleton className="h-4 w-full" />
                        <Skeleton className="h-4 w-5/6" />
                    </div>
                )}
            </CardContent>
        </Card>
    )
}
