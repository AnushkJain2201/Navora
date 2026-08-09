import { useAuth } from "@/context/AuthContext";
import { ReactNode } from "react";
import { Navigate } from "react-router-dom";

export default function PublicOnlyRoute({ children }: { children: ReactNode }) {
    const { isAuthenticated } = useAuth();

    if(isAuthenticated) {
        return <Navigate to="/" replace />
    }

    return <>{children}</>;
}
