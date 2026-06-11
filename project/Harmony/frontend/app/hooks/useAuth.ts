'use client'

import {useContext} from 'react';
import {AuthContext} from "../components/AuthProvider";
import {AuthContextType} from "../types/auth";

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth должен быть использован с AuthProvider');
    }
    return context;
};