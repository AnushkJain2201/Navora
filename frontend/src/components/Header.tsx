import { Link } from "react-router-dom"
import { useAuth } from "../context/AuthContext"
import { Button } from "@/components/ui/button"

export function Header() {
    const { user, isAuthenticated, logout } = useAuth()

    return (
        <header className="border-b border-border bg-card">
            <div className="mx-auto flex w-full max-w-6xl items-center justify-between px-8 py-4">
                <Link to="/" className="font-display text-2xl font-semibold tracking-tight">
                    Navora
                </Link>

                {isAuthenticated && (
                    <div className="flex items-center gap-4">
                        <span className="text-sm text-muted-foreground">
                            {user?.name}
                        </span>
                        <Link to="/scan" className="font-display text-2xl font-semibold tracking-tight">
                            Scan
                        </Link>
                        <Button variant="outline" size="sm" onClick={logout}>
                            Log out
                        </Button>
                    </div>
                )}
            </div>
        </header>
    )
}