import { useState } from "react"
import { useAuth } from "../context/AuthContext"
import { Button } from "@/components/ui/button"
import { TripPlannerForm } from "../components/TripPlannerForm"
import type { TripPlanResponse } from "../api/tripApi"

export default function HomePage() {
  const { user, logout } = useAuth()
  const [lastTrip, setLastTrip] = useState<TripPlanResponse | null>(null)

  return (
    <div className="mx-auto max-w-2xl p-8">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Navora</h1>
        <div className="flex items-center gap-4">
          <span className="text-sm text-muted-foreground">Welcome, {user?.name}</span>
          <Button variant="outline" onClick={logout}>
            Log out
          </Button>
        </div>
      </div>

      <div className="mt-8">
        <TripPlannerForm onTripPlanned={setLastTrip} />
      </div>

      {lastTrip && lastTrip.itineraryDays && (
        <div className="mt-6">
          <p className="text-sm text-muted-foreground">
            Trip planned: {lastTrip.destination}, {lastTrip.durationDays} days —
            itinerary display coming in 5e.
          </p>
        </div>
      )}
    </div>
  )
}