import { BrowserRouter, Routes, Route } from "react-router-dom"
import LoginPage from "./pages/LoginPage"
import RegisterPage from "./pages/RegisterPage"
import PublicOnlyRoute from "./components/PublicOnlyRoute"
import { ProtectedRoute } from "./components/ProtectedRoute"
import HomePage from "./pages/HomePage"

function App() {
	return (
		<BrowserRouter>
			<Routes>
				<Route path="/login" element={<PublicOnlyRoute><LoginPage /></PublicOnlyRoute>} />
				<Route path="/register" element={<PublicOnlyRoute><RegisterPage /></PublicOnlyRoute>} />
				<Route path="/" element={<ProtectedRoute><HomePage /></ProtectedRoute>} />
			</Routes>
		</BrowserRouter>
	)
}

export default App