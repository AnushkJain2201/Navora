import { LoginCredentials, RegisterCredentials, User } from "@/types/auth";

const BASE_URL = "http://localhost:8080";

interface AuthResponse {
    token: string,
    email: string,
    name: string
}

const handleAuthResponse = async (response: Response): Promise<{ token: string, user: User }> => {
    if (!response.ok) {
        const errorBody = await response.json().catch(() => ({ error: "Request failed" }))
        throw new Error(errorBody.error ?? `Request failed: ${response.status}`)
    }

    const data: AuthResponse = await response.json()
    return {
        token: data.token,
        user: { email: data.email, name: data.name },
    }
}


export const login = async (credentials: LoginCredentials) => {
    const response = await fetch(`${BASE_URL}/api/auth/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(credentials)
    });

    return handleAuthResponse(response)
}

export async function register(credentials: RegisterCredentials) {
    const response = await fetch(`${BASE_URL}/api/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(credentials),
    })
    return handleAuthResponse(response)
}