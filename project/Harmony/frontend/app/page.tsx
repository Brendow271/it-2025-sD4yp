'use client';

import React from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from './hooks/useAuth';
import SwipeCards from './components/SwipeCards';

export default function Home() {
    const { isAuthenticated, isLoading } = useAuth();
    const router = useRouter();

    if (isLoading) {
        return (
            <div className="flex items-center justify-center w-full h-full">
                <p>Загрузка...</p>
            </div>
        );
    }

    if (isAuthenticated) {
        return (
            <div className="flex flex-col items-center w-full px-4 pb-8">
                <SwipeCards />
            </div>
        );
    }

    return (
        <div className="flex items-center justify-center w-full min-h-[60vh] px-4">
            <button
                onClick={() => router.push('/pages/auth')}
                className="text-xl text-center hover:underline cursor-pointer"
            >
                Добро пожаловать в HARMONY, войдите в аккаунт
            </button>
        </div>
    );
}
