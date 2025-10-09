interface User{
    id: string;
    email: string;
}
interface AuthData{
    token: string;
    user: User;
}
interface AuthContextType{
    isAuthenticated: boolean;
    user: User | null;
    login: (email: string, password: string) => Promise<boolean>;
    logout: () => void;
    isLoading: boolean;
}