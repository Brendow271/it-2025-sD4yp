'use client';

import {createContext, useState, useEffect, ReactNode} from 'react';
import {AuthContextType, User} from '../types/auth'
import {authCookies} from "./common/cookies";

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
    children: ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps) {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [user, setUser] = useState<User | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        checkAuth();
    }, []);

    const checkAuth = async () => {
        try {
            const token = authCookies.getAuthToken();
            if (token) {
                const userData = authCookies.getUserData();
                if (userData) {
                    setUser(userData);
                    setIsAuthenticated(true);
                }
            }

        }catch (error){
            console.error('Ошибка проверки аутентификации: ', error);
            logout();
        } finally{
            setIsLoading(false);
        }
    };

    const login = async (email: string, password: string): Promise<boolean> => {
        try {
            const response = await fetch("https://jsonplaceholder.typicode.com/posts", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({email, password}),
            });
             const data = await response.json();

             if (response.ok && data.token) {

                 authCookies.setAuthToken(data.token);
                 authCookies.setUserData(data.user);

                 setUser(data.user);
                 setIsAuthenticated(true);
                 return true;
             }else{
                 console.error('Не удалось войти: ', data.message);
                 return false;
             }
        }catch(error){
            console.error('Ошибка входа: ', error);
            return false;
        }
    };
     const logout = async () => {
         authCookies.removeAuthToken();
         authCookies.removeUserData();

         setUser(null);
         setIsAuthenticated(false);
     };

     const value: AuthContextType = {
         isAuthenticated,
         user,
         login,
         logout,
         isLoading,
     };

     return (
         <AuthContext.Provider value={value}>
             {children}
         </AuthContext.Provider>
     );
}