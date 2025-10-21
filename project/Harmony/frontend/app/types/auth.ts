export interface User{
    id: string;
    email: string;
}
export interface AuthData{
    token: string;
    user: User;
}
export interface AuthContextType{
    isAuthenticated: boolean;
    user: User | null;
    login: (email: string, password: string) => Promise<boolean>;
    logout: () => void;
    isLoading: boolean;
}