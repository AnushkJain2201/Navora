import type { ItineraryDay } from "../api/tripApi"
import { MapContainer, TileLayer, Marker, Polyline, Popup } from "react-leaflet"
import "../lib/leafletIconFix"

interface TripMapProps {
    itineraryDays: ItineraryDay[]
}

export const TripMap = ({ itineraryDays }: TripMapProps) => {
    const allStopsWithCoords = itineraryDays
        .flatMap((day) => day.stops)
        .filter((stop) => stop.latitude !== null && stop.longitude !== null);

    if (allStopsWithCoords.length === 0) {
        return (
            <div className="flex h-64 items-center justify-center rounded-lg border text-sm text-muted-foreground">
                No location data available for this trip
            </div>
        )
    }

    const center: [number, number] = [
        allStopsWithCoords[0].latitude!,
        allStopsWithCoords[0].longitude!,
    ]


    return (
        <div className="h-96 w-full overflow-hidden rounded-lg border">
            <MapContainer center={center} zoom={12} scrollWheelZoom={true} className="h-full w-full">
                <TileLayer
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />

                {itineraryDays.map((day) => {
                    const dayStops = day.stops.filter(
                        (stop) => stop.latitude !== null && stop.longitude !== null
                    )
                    const positions: [number, number][] = dayStops.map((stop) => [
                        stop.latitude!,
                        stop.longitude!,
                    ])

                    return (
                        <div key={day.id}>
                            {positions.length > 1 && (
                                <Polyline positions={positions} color="#2563eb" weight={3} opacity={0.7} />
                            )}
                            {dayStops.map((stop) => (
                                <Marker key={stop.id} position={[stop.latitude!, stop.longitude!]}>
                                    <Popup>
                                        <strong>Day {day.dayNumber}: {stop.landmarkName}</strong>
                                        <br />
                                        {stop.description}
                                    </Popup>
                                </Marker>
                            ))}
                        </div>
                    )
                })}
            </MapContainer>
        </div>
    )
}
