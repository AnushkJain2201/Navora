import { useState, FormEvent } from "react"
import { useNavigate, Link } from "react-router-dom"
import { useAuth } from "../context/AuthContext"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card"

export default function LoginPage() {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [error, setError] = useState("")
    const [loading, setLoading] = useState(false)

    const { login } = useAuth()
    const navigate = useNavigate()

    async function handleSubmit(e: FormEvent<HTMLFormElement>) {
        e.preventDefault()
        setError("")
        setLoading(true)

        try {
            await login({ email, password })
            navigate("/")
        } catch (err) {
            setError(err instanceof Error ? err.message : "Login failed")
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="flex min-h-screen items-center justify-center bg-muted p-4">
            <Card className="w-full max-w-sm">
                <CardHeader>
                    <CardTitle className="text-2xl">Welcome back</CardTitle>
                    <CardDescription>Log in to plan your next trip</CardDescription>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit} className="space-y-4">
                        <div className="space-y-2">
                            <Label htmlFor="email">Email</Label>
                            <Input
                                id="email"
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </div>
                        <div className="space-y-2">
                            <Label htmlFor="password">Password</Label>
                            <Input
                                id="password"
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        {error && <p className="text-sm text-destructive">{error}</p>}
                        <Button type="submit" className="w-full" disabled={loading}>
                            {loading ? "Logging in..." : "Log in"}
                        </Button>
                    </form>
                    <p className="mt-4 text-center text-sm text-muted-foreground">
                        Don't have an account?{" "}
                        <Link to="/register" className="underline underline-offset-4">
                            Register
                        </Link>
                    </p>
                </CardContent>
            </Card>
        </div>
    )
}