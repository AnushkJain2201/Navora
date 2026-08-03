// import TripRequestForm from "@/components/TripRequestForm"
import { useAuth } from "./context/AuthContext"

function App() {
	const { user, isAuthenticated, logout } = useAuth()
	return (
		<div className="p-8">
			<h1 className="text-3xl font-bold text-blue-600">Navora</h1>
			<p>Authenticated: {isAuthenticated ? "Yes" : "No"}</p>
			{user && <p>Welcome, {user.name}</p>}
			{isAuthenticated && <button onClick={logout}>Log out</button>}
		</div>
	)
}

export default App
