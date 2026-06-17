'use client';

import React from 'react';
import { useAuth } from '../../hooks/useAuth';
import { useRouter } from 'next/navigation';

export default function Settings() {
    const { logout, isAuthenticated, isLoading } = useAuth();
    const router = useRouter();

    const handleLogout = () => {
        logout();
        router.push('/');
    };

    if (isLoading) {
        return (
            <div className="flex items-center justify-center w-full h-full">
                <p>Загрузка...</p>
            </div>
        );
    }

    if (!isAuthenticated) {
        return (
            <div className="flex flex-col items-center justify-center gap-4 p-8">
                <p>Войдите в аккаунт для доступа к настройкам</p>
                <button
                    onClick={() => router.push('/pages/auth')}
                    className="px-6 py-2 border border-black rounded-md hover:bg-gray-100"
                >
                    Войти
                </button>
            </div>
        );
    }

    return (
        <div className="flex flex-col items-center gap-6 p-8">
            <h1 className="text-3xl">Настройки</h1>
            <button
                onClick={handleLogout}
                className="px-6 py-2 border border-red-500 text-red-500 rounded-md hover:bg-red-50"
            >
                Выйти
            </button>
        </div>
    );
}
