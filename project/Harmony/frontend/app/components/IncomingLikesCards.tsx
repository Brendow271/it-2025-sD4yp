'use client';

import React, { useState, useRef, useEffect, useCallback } from 'react';
import { authCookies } from './common/cookies';
import { API_BASE } from '../lib/api';
import UserProfileCard, { UserCardData } from './UserProfileCard';

const SWIPE_THRESHOLD = 100;

export default function IncomingLikesCards() {
    const [cards, setCards] = useState<UserCardData[]>([]);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [isLoading, setIsLoading] = useState(true);
    const [offsetX, setOffsetX] = useState(0);
    const [isDragging, setIsDragging] = useState(false);
    const [isAnimating, setIsAnimating] = useState(false);
    const dragStartX = useRef(0);

    const loadLikes = useCallback(async () => {
        const token = authCookies.getAuthToken();
        const userData = authCookies.getUserData();
        const currentUserId = userData?.userId || userData?.id;

        if (!token || !currentUserId) {
            setIsLoading(false);
            return;
        }

        try {
            const response = await fetch(`${API_BASE}/swipes/likes/${currentUserId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            if (response.ok) {
                const data: UserCardData[] = await response.json();
                setCards(data);
                setCurrentIndex(0);
            }
        } catch (error) {
            console.error('Ошибка загрузки лайков:', error);
        } finally {
            setIsLoading(false);
        }
    }, []);

    useEffect(() => {
        loadLikes();
    }, [loadLikes]);

    const currentCard = cards[currentIndex];
    const hasMore = currentIndex < cards.length;

    const goToNext = () => {
        setCurrentIndex((prev) => prev + 1);
        setOffsetX(0);
        setIsAnimating(false);
    };

    const respond = async (decision: boolean) => {
        if (!currentCard || isAnimating) return;

        const token = authCookies.getAuthToken();
        const userData = authCookies.getUserData();
        const currentUserId = userData?.userId || userData?.id;

        setIsAnimating(true);
        setOffsetX(decision ? 400 : -400);

        if (token && currentUserId) {
            try {
                await fetch(`${API_BASE}/swipes/create`, {
                    method: 'POST',
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        userId1: Number(currentUserId),
                        userId2: currentCard.userId,
                        decision,
                    }),
                });
            } catch (error) {
                console.error('Ошибка ответа на лайк:', error);
            }
        }

        setTimeout(goToNext, 250);
    };

    const handlePointerDown = (e: React.PointerEvent) => {
        if (!currentCard || isAnimating) return;
        setIsDragging(true);
        dragStartX.current = e.clientX;
        (e.target as HTMLElement).setPointerCapture(e.pointerId);
    };

    const handlePointerMove = (e: React.PointerEvent) => {
        if (!isDragging || isAnimating) return;
        setOffsetX(e.clientX - dragStartX.current);
    };

    const handlePointerUp = () => {
        if (!isDragging || isAnimating) return;
        setIsDragging(false);

        if (offsetX > SWIPE_THRESHOLD) {
            respond(true);
        } else if (offsetX < -SWIPE_THRESHOLD) {
            respond(false);
        } else {
            setOffsetX(0);
        }
    };

    if (isLoading) {
        return <div className="p-8 text-center">Загрузка лайков...</div>;
    }

    if (!hasMore || !currentCard) {
        return (
            <div className="flex flex-col items-center justify-center gap-4 p-8">
                <h1 className="text-3xl">Лайки</h1>
                <p className="text-xl text-gray-600">Новых лайков нет</p>
                <button
                    onClick={() => {
                        setIsLoading(true);
                        loadLikes();
                    }}
                    className="px-6 py-2 border border-black rounded-md hover:bg-gray-100"
                >
                    Обновить
                </button>
            </div>
        );
    }

    return (
        <div className="flex flex-col items-center gap-6 w-full max-w-sm mx-auto px-4 pb-8">
            <h1 className="text-3xl">Лайки</h1>
            <p className="text-gray-600 text-sm text-center">Эти пользователи лайкнули вас</p>
            <div className="relative w-full h-[480px]">
                <div
                    className="absolute inset-0 select-none touch-none"
                    style={{
                        transform: `translateX(${offsetX}px) rotate(${offsetX * 0.05}deg)`,
                        transition: isDragging ? 'none' : 'transform 0.25s ease',
                        cursor: isDragging ? 'grabbing' : 'grab',
                    }}
                    onPointerDown={handlePointerDown}
                    onPointerMove={handlePointerMove}
                    onPointerUp={handlePointerUp}
                    onPointerCancel={handlePointerUp}
                >
                    <UserProfileCard user={currentCard} />
                </div>
                {offsetX > 40 && (
                    <div className="absolute top-8 left-8 z-20 border-4 border-green-500 text-green-500 font-bold text-2xl px-4 py-1 rotate-[-15deg]">
                        LIKE
                    </div>
                )}
                {offsetX < -40 && (
                    <div className="absolute top-8 right-8 z-20 border-4 border-red-500 text-red-500 font-bold text-2xl px-4 py-1 rotate-[15deg]">
                        NOPE
                    </div>
                )}
            </div>
            <div className="flex gap-8">
                <button
                    onClick={() => respond(false)}
                    className="w-14 h-14 rounded-full border-2 border-red-400 text-red-400 text-2xl hover:bg-red-50"
                    aria-label="Пропустить"
                >
                    ✕
                </button>
                <button
                    onClick={() => respond(true)}
                    className="w-14 h-14 rounded-full border-2 border-green-400 text-green-400 text-2xl hover:bg-green-50"
                    aria-label="Нравится"
                >
                    ♥
                </button>
            </div>
        </div>
    );
}
