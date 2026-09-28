const BASE_URL = "http://localhost:8080";

export interface ChatSessionSummary {
    id: string;
    title: string;
    createdAt: string;
}

export interface ChatMessage {
    id: string;
    role: "user" | "assistant";
    content: string;
    createdAt: string
}

export interface AskResponse {
    sessionId: string;
    answer: string;
    sources: string[];
}

export const askQuestion = async(sessionId: string | null,
    question: string,
    token: string
): Promise<AskResponse> => {
    const response = await fetch(`${BASE_URL}/api/chat/ask`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({
            sessionId,
            question
        })
    })

    if(!response.ok){
        throw new Error("Failed to get response.");
    }
    
    return response.json();
}

export const getChatSessions = async(token: string): Promise<ChatSessionSummary[]> => {
    const response = await fetch(`${BASE_URL}/api/chat/sessions`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    })

    if(!response.ok) {
        throw new Error("Failed to load chat sessions.");
    }

    return response.json();
}

export const getSessionMessages = async(sessionId: string, token: string): Promise<ChatMessage[]> => {
    const response = await fetch(`${BASE_URL}/api/chat/sessions/${sessionId}/messages`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    })

    if(!response.ok) {
        throw new Error("Failed to load chat messages.");
    }

    return response.json();
}