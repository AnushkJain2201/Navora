import { useCallback, useRef, useState } from "react";

export function useCameraCapture() {
    const videoRef = useRef<HTMLVideoElement>(null);
    const streamRef = useRef<MediaStream | null>(null);
    const [isActive, setIsActive] = useState(false);
    const [error, setError] = useState("")

    const startCamera = useCallback(async () => {
        setError("")
        try {
            const stream = await navigator.mediaDevices.getUserMedia({
                video: {facingMode: "environment"},
            });

            streamRef.current = stream;

            if(videoRef.current) {
                videoRef.current.srcObject = stream;
            }

            setIsActive(true);
        } catch (err) {
            setError("Could not access camera. Check permissions.");
        }
    }, []);

    const stopCamera = useCallback(() => {
        streamRef.current?.getTracks().forEach((track) => track.stop());
        streamRef.current = null;
        setIsActive(false);
    }, []);

    const capturePhoto = useCallback((): Promise<Blob | null> => {
        return new Promise((resolve) => {
            const video = videoRef.current;
            if(!video) {
                resolve(null);
                return;
            }

            const canvas = document.createElement("canvas");
            canvas.width = video.videoWidth;
            canvas.height = video.videoHeight;
            const ctx = canvas.getContext("2d");
            if(!ctx) {
                resolve(null);
                return;
            }

            ctx.drawImage(video, 0, 0);
            canvas.toBlob((blob) => resolve(blob), "image/jpeg", 0.9);
        });
    }, []);

    return {videoRef, isActive, error, startCamera, stopCamera, capturePhoto};
}