import { useState, useEffect, useRef } from "react";
import { useAuth } from "../context/AuthContext";
import {
  askQuestion,
  getChatSessions,
  getSessionMessages,
  ChatSessionSummary,
  ChatMessage,
} from "../api/chatApi";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Layout } from "../components/Layout";

export default function ChatPage() {
  const { token } = useAuth();

  const [sessions, setSessions] = useState<ChatSessionSummary[]>([]);
  const [activeSessionId, setActiveSessionId] = useState<string | null>(null);
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  //   const messagesEndRef = useRef<HTMLDivElement>(null);
  const messagesContainerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const container = messagesContainerRef.current;
    if (!container) return;
    container.scrollTo({ top: container.scrollHeight, behavior: "smooth" });
  }, [messages, loading]);

  useEffect(() => {
    if (token) loadSessions();
  }, [token]);

  //   useEffect(() => {
  //     messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  //   }, [messages]);

  async function loadSessions() {
    if (!token) return;
    try {
      const data = await getChatSessions(token);
      setSessions(data);
    } catch (err) {
      setError("Could not load chat history");
    }
  }

  async function openSession(sessionId: string) {
    if (!token) return;
    setActiveSessionId(sessionId);
    setError("");
    try {
      const data = await getSessionMessages(sessionId, token);
      setMessages(data);
    } catch (err) {
      setError("Could not load this conversation");
    }
  }

  function startNewChat() {
    setActiveSessionId(null);
    setMessages([]);
    setError("");
  }

  async function handleSend() {
    if (!token || !input.trim() || loading) return;

    const question = input.trim();
    setInput("");
    setError("");

    const optimisticUserMessage: ChatMessage = {
      id: crypto.randomUUID(),
      role: "user",
      content: question,
      createdAt: new Date().toISOString(),
    };
    setMessages((prev) => [...prev, optimisticUserMessage]);
    setLoading(true);

    try {
      const response = await askQuestion(activeSessionId, question, token);

      const assistantMessage: ChatMessage = {
        id: crypto.randomUUID(),
        role: "assistant",
        content: response.answer,
        createdAt: new Date().toISOString(),
      };
      setMessages((prev) => [...prev, assistantMessage]);

      if (!activeSessionId) {
        setActiveSessionId(response.sessionId);
        await loadSessions();
      }
    } catch (err) {
      setError("Something went wrong. Try again.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <Layout>
      <div className="mx-auto flex h-[calc(100vh-6rem)] max-w-5xl gap-6 px-8 py-8">
        {/* Session sidebar */}
        <aside className="w-64 shrink-0 border-r pr-4">
          <Button onClick={startNewChat} className="mb-4 w-full">
            New chat
          </Button>
          <div className="flex flex-col gap-1 overflow-y-auto">
            {sessions.map((session) => (
              <button
                key={session.id}
                onClick={() => openSession(session.id)}
                className={`truncate rounded-md px-3 py-2 text-left text-sm hover:bg-muted ${
                  activeSessionId === session.id ? "bg-muted font-medium" : ""
                }`}
              >
                {session.title ?? "New conversation"}
              </button>
            ))}
          </div>
        </aside>

        {/* Message thread */}
        <div className="flex flex-1 flex-col">
          <div ref={messagesContainerRef} className="min-h-0 flex-1 overflow-y-auto pr-2">
            {messages.length === 0 && (
              <p className="mt-12 text-center text-muted-foreground">
                Ask about landmarks, history, or specific monuments in India and
                Egypt.
              </p>
            )}
            {messages.map((message) => (
              <div
                key={message.id}
                className={`mb-4 flex ${message.role === "user" ? "justify-end" : "justify-start"}`}
              >
                <div
                  className={`max-w-md rounded-lg px-4 py-2 text-sm ${
                    message.role === "user"
                      ? "bg-primary text-primary-foreground"
                      : "bg-muted"
                  }`}
                >
                  {message.content}
                </div>
              </div>
            ))}
            {loading && (
              <div className="mb-4 flex justify-start">
                <div className="max-w-md rounded-lg bg-muted px-4 py-2 text-sm text-muted-foreground">
                  Thinking...
                </div>
              </div>
            )}
            {/* <div ref={messagesEndRef} /> */}
          </div>

          {error && <p className="mb-2 text-sm text-destructive">{error}</p>}

          <div className="flex gap-2">
            <Input
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && handleSend()}
              placeholder="Ask something..."
              disabled={loading}
            />
            <Button onClick={handleSend} disabled={loading || !input.trim()}>
              Send
            </Button>
          </div>
        </div>
      </div>
    </Layout>
  );
}
