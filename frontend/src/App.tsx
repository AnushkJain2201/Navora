import { BrowserRouter, Routes, Route } from "react-router-dom"
import LoginPage from "./pages/LoginPage"
import RegisterPage from "./pages/RegisterPage"

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/" element={<div className="p-8">Home (placeholder)</div>} />
      </Routes>
    </BrowserRouter>
  )
}

export default App