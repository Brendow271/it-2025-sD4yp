'use client'
import {useContext} from "react";

import {useAuth} from "../hooks/useAuth";
import {ReactNode} from "react";

interface ProtectedRouteProps {
    children: ReactNode;
    fallback?: ReactNode;
}

export function ProtectedRoute({children, fallback}: ProtectedRouteProps){
    const {isAuthenticated, isLoading} = useAuth();

    if (isLoading){
        return (
            <div>Проверка аутентификации</div>
        );
    }
    if (!isAuthenticated){
        return fallback || (
            <div>У вас нет доступа к данной странице</div>
        );
    }

    return <>{children}</>
}