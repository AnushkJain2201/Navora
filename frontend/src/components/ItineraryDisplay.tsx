import type { TripPlanResponse, ItineraryDay, ItineraryStop } from "../api/tripApi"
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"

interface ItineraryDisplayProps {
  trip: TripPlanResponse
}

export function ItineraryDisplay({ trip }: ItineraryDisplayProps) {
  if (!trip.itineraryDays) {
    return null
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>
          {trip.destination} — {trip.durationDays} days
        </CardTitle>
        <CardDescription>
          Budget: ₹{trip.budget?.toLocaleString("en-IN")}
        </CardDescription>
      </CardHeader>
      <CardContent className="space-y-6">
        {trip.itineraryDays.map((day, index) => (
          <div key={day.id}>
            <DayCard day={day} />
            {index < trip.itineraryDays!.length - 1 && <Separator className="mt-6" />}
          </div>
        ))}
      </CardContent>
    </Card>
  )
}

function DayCard({ day }: { day: ItineraryDay }) {
  return (
    <div>
      <h3 className="text-lg font-semibold">Day {day.dayNumber}</h3>
      <div className="mt-3 space-y-3">
        {day.stops
          .sort((a, b) => a.orderIndex - b.orderIndex)
          .map((stop) => (
            <StopCard key={stop.id} stop={stop} />
          ))}
      </div>
    </div>
  )
}

function StopCard({ stop }: { stop: ItineraryStop }) {
  return (
    <div className="rounded-lg border p-4">
      <div className="flex items-start justify-between gap-2">
        <h4 className="font-medium">{stop.landmarkName}</h4>
        {stop.estimatedDurationHours && (
          <Badge variant="secondary">{stop.estimatedDurationHours}h</Badge>
        )}
      </div>
      <p className="mt-1 text-sm text-muted-foreground">{stop.description}</p>
    </div>
  )
}