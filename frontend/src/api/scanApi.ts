const BASE_URL = "http://localhost:8080";

export interface ScanResult {
    scanId: string
    imageUrl: string
    matched: boolean
    landmarkName: string | null
    specificFeature: string | null 
    generatedContext: string | null
}

export interface ScanSummary {
    id: string
    imageUrl: string
    matched: boolean
    landmarkName: string | null
    specificFeature: string | null
    generatedContext: string | null
    scannedAt: string
}

export async function identifyScan(
    imageBlob: Blob,
    latitude: number,
    longitude: number,
    token: string
): Promise<ScanResult> {
    const formData = new FormData();
    formData.append("image", imageBlob, "scan.jpg");
    formData.append("latitude", latitude.toString());
    formData.append("longitude", longitude.toString());

    const response = await fetch(`${BASE_URL}/api/scans/identify`, {
        method: "POST",
        headers: {"Authorization": `Bearer ${token}`},
        body: formData,
    })

    if(!response.ok) {
        const errorBody = await response.json().catch(() => ({error: "Scan failed"}))
        throw new Error(errorBody.error ?? "Scan failed")
    }

    return response.json()
}

export async function getScanHistory(token: string): Promise<ScanSummary[]> {
    const response = await fetch(`${BASE_URL}/api/scans`, {
        headers: {"Authorization": `Bearer ${token}`},
    });

    if(!response.ok) {
        throw new Error("Failed to load scan history");
    }

    return response.json();
}