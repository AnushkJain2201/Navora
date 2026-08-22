import { useState } from "react"
import { useAuth } from "../context/AuthContext"
import { useCameraCapture } from "../hooks/useCameraCapture"
import { useGeolocation } from "../hooks/useGeolocation"
import { identifyScan, ScanResult } from "../api/scanApi"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Layout } from "../components/Layout"

export default function ScanPage() {
  const { token } = useAuth()
  const { videoRef, isActive, error: cameraError, startCamera, stopCamera, capturePhoto } = useCameraCapture()
  const { requestLocation, error: locationError } = useGeolocation()

  const [loading, setLoading] = useState(false)
  const [result, setResult] = useState<ScanResult | null>(null)
  const [error, setError] = useState("")

  async function handleCapture() {
    if (!token) return

    setError("")
    setResult(null)
    setLoading(true)

    try {
      const location = await requestLocation()
      if (!location) {
        setError("Location is required to identify a landmark")
        return
      }

      const blob = await capturePhoto()
      if (!blob) {
        setError("Could not capture photo")
        return
      }

      const scanResult = await identifyScan(blob, location.latitude, location.longitude, token)
      setResult(scanResult)
    } catch (err) {
      setError(err instanceof Error ? err.message : "Scan failed")
    } finally {
      setLoading(false)
    }
  }

  return (
    <Layout>
      <div className="mx-auto max-w-2xl px-8 py-12">
        <h1 className="font-display text-3xl font-semibold">Scan a landmark</h1>
        <p className="mt-2 text-muted-foreground">
          Point your camera at a monument to learn what you're looking at.
        </p>

        <Card className="mt-6">
          <CardContent className="pt-6">
            {!isActive && (
              <Button onClick={startCamera}>Start camera</Button>
            )}

            {cameraError && <p className="mt-2 text-sm text-destructive">{cameraError}</p>}
            {locationError && <p className="mt-2 text-sm text-destructive">{locationError}</p>}

            <video
              ref={videoRef}
              autoPlay
              playsInline
              className={isActive ? "mt-4 w-full rounded-lg" : "hidden"}
            />

            {isActive && (
              <div className="mt-4 flex gap-3">
                <Button onClick={handleCapture} disabled={loading}>
                  {loading ? "Identifying..." : "Capture and identify"}
                </Button>
                <Button variant="outline" onClick={stopCamera}>
                  Stop camera
                </Button>
              </div>
            )}
          </CardContent>
        </Card>

        {error && <p className="mt-4 text-sm text-destructive">{error}</p>}

        {result && (
          <Card className="mt-6">
            <CardHeader>
              <CardTitle>
                {result.matched ? (result.specificFeature ?? result.landmarkName) : "No confident match"}
              </CardTitle>
            </CardHeader>
            <CardContent>
              {result.matched && result.landmarkName && (
                <p className="text-sm text-muted-foreground">Part of {result.landmarkName}</p>
              )}
              {result.generatedContext && (
                <p className="mt-3 text-sm">{result.generatedContext}</p>
              )}
              {!result.matched && (
                <p className="text-sm text-muted-foreground">
                  We couldn't confidently identify this. Try getting closer or a clearer angle.
                </p>
              )}
            </CardContent>
          </Card>
        )}
      </div>
    </Layout>
  )
}