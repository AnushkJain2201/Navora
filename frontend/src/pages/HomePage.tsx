import { useAuth } from "../context/AuthContext"
import { Button } from "@/components/ui/button"

export default function HomePage() {
    const { user, logout } = useAuth()

    return (
        <div className="p-8">
            <div className="flex items-center justify-between">
                <h1 className="text-3xl font-bold">Navora</h1>
                <div className="flex items-center gap-4">
                    <span className="text-sm text-muted-foreground">Welcome, {user?.name}</span>
                    <Button variant="outline" onClick={logout}>
                        Log out
                    </Button>
                </div>
            </div>
            <p className="mt-4 text-muted-foreground">Trip planner coming in 5d.</p>
        </div>
    )
}