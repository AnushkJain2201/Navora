import { useState } from "react"
import { useAuth } from "../context/AuthContext"
import { Button } from "@/components/ui/button"
import { TripPlannerForm } from "../components/TripPlannerForm"
import { ItineraryDisplay } from "../components/ItineraryDisplay"
import type { TripPlanResponse } from "../api/tripApi"
import { TripMap } from "@/components/TripMap"

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

      <div className="mt-8 space-y-6">
        <TripPlannerForm onTripPlanned={setLastTrip} />
      </div>

      {lastTrip && lastTrip.itineraryDays && (
        <div className="space-y-6">
          <ItineraryDisplay trip={lastTrip} />
          <TripMap itineraryDays={lastTrip.itineraryDays} />
        </div>
      )}
    </div>
  )
}