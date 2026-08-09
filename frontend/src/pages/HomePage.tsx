import { useState } from "react"
import { TripPlannerForm } from "../components/TripPlannerForm"
import { ItineraryDisplay } from "../components/ItineraryDisplay"
import { TripMap } from "../components/TripMap"
import { Layout } from "../components/Layout"
import type { TripPlanResponse } from "../api/tripApi"

export default function HomePage() {
	const [lastTrip, setLastTrip] = useState<TripPlanResponse | null>(null)

	return (
		<Layout>
			<div className="mx-auto max-w-2xl px-8 py-12">
				<div className="mb-10">
					<h1 className="font-display text-4xl font-semibold leading-tight">
						Tell us where history calls.
					</h1>
					<p className="mt-3 text-muted-foreground">
						Describe a trip in your own words — a destination, a length of stay,
						a budget — and we'll route you through the sites that made it matter.
					</p>
				</div>

				<TripPlannerForm onTripPlanned={setLastTrip} />

				{lastTrip && lastTrip.itineraryDays && (
					<div className="mt-6 space-y-6">
						<ItineraryDisplay trip={lastTrip} />
						<TripMap itineraryDays={lastTrip.itineraryDays} />
					</div>
				)}
			</div>
		</Layout>
	)
}