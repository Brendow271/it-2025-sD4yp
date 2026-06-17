'use client';

import React, { useEffect, useState, useCallback } from 'react';
import { authCookies } from './common/cookies';
import { API_BASE } from '../lib/api';
import UserProfileCard, { UserCardData } from './UserProfileCard';

export default function MatchList() {
    const [matches, setMatches] = useState<UserCardData[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    const loadMatches = useCallback(async () => {
        const token = authCookies.getAuthToken();
        const userData = authCookies.getUserData();
        const currentUserId = userData?.userId || userData?.id;

        if (!token || !currentUserId) {
            setIsLoading(false);
            return;
        }

        try {
            const response = await fetch(`${API_BASE}/swipes/matches/${currentUserId}/cards`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            if (response.ok) {
                setMatches(await response.json());
            }
        } catch (error) {
            console.error('Ошибка загрузки мэтчей:', error);
        } finally {
            setIsLoading(false);
        }
    }, []);

    useEffect(() => {
        loadMatches();
    }, [loadMatches]);

    if (isLoading) {
        return <div className="p-8 text-center">Загрузка мэтчей...</div>;
    }

    if (matches.length === 0) {
        return (
            <div className="p-8 text-center text-gray-600">
                Пока нет мэтчей. Лайкайте музыкантов на главной странице!
            </div>
        );
    }

    return (
        <div className="flex flex-col items-center gap-4 w-full max-w-md mx-auto px-4 pb-8">
            <h1 className="text-3xl">Чат</h1>
            <p className="text-gray-600 text-sm">Ваши мэтчи</p>
            <ul className="flex flex-col gap-4 w-full">
                {matches.map((match) => (
                    <li key={match.userId}>
                        <UserProfileCard user={match} />
                    </li>
                ))}
            </ul>
        </div>
    );
}
