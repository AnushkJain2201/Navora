import { AuthState, LoginCredentials, RegisterCredentials } from "@/types/auth";
import { createContext, ReactNode, useContext, useEffect, useState } from "react";
import { login as loginApi, register as registerApi } from "../api/authApi"


interface AuthContextValue extends AuthState {
    login: (credentials: LoginCredentials) => Promise<void>,
    register: (credentials: RegisterCredentials) => Promise<void>,
    logout: () => void,
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

const TOKEN_KEY = "navora_token"
const USER_KEY = "navora_user"

export const AuthProvider = ({children}: {children: ReactNode}) => {
    const [token, setToken] = useState<string | null>(null);
    const [user, setUser] = useState<AuthState["user"]>(null)

    useEffect(() => {
        const savedToken = localStorage.getItem(TOKEN_KEY);
        const savedUser = localStorage.getItem(USER_KEY);

        if(savedToken && savedUser) {
            setToken(savedToken);
            setUser(JSON.parse(savedUser));
        }
    }, [])

    const login = async(credentials: LoginCredentials) => {
        const response = await loginApi(credentials);
        setToken(response.token)
        setUser(response.user)
        localStorage.setItem(TOKEN_KEY, response.token)
        localStorage.setItem(USER_KEY, JSON.stringify(response.user))
    }

    const register = async(credntials: RegisterCredentials) => {
        const response = await registerApi(credntials);
        setToken(response.token)
        setUser(response.user)
        localStorage.setItem(TOKEN_KEY, response.token)
        localStorage.setItem(USER_KEY, JSON.stringify(response.user))
    }

    const logout = () => {
        setToken(null);
        setUser(null);
        localStorage.removeItem(TOKEN_KEY)
        localStorage.removeItem(USER_KEY)
    }

    const value: AuthContextValue = {
        token,
        user,
        isAuthenticated: token !== null,
        login,
        register,
        logout,
    }

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export const useAuth = (): AuthContextValue => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
}