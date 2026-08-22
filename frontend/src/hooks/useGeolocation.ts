import { useState, useCallback } from "react"

export function useGeolocation() {
  const [coords, setCoords] = useState<{ latitude: number; longitude: number } | null>(null)
  const [error, setError] = useState("")

  const requestLocation = useCallback((): Promise<{ latitude: number; longitude: number } | null> => {
    return new Promise((resolve) => {
      if (!navigator.geolocation) {
        setError("Geolocation not supported")
        resolve(null)
        return
      }

      navigator.geolocation.getCurrentPosition(
        (position) => {
          const result = {
            latitude: position.coords.latitude,
            longitude: position.coords.longitude,
          }
          setCoords(result)
          resolve(result)
        },
        () => {
          setError("Could not get location. Check permissions.")
          resolve(null)
        },
        { enableHighAccuracy: true }
      )
    })
  }, [])

  return { coords, error, requestLocation }
}