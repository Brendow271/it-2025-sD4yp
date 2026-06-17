'use client';

import React, { useState, useRef, useEffect, useCallback } from 'react';
import { MOCK_USERS } from '../data/mockUsers';
import { authCookies } from './common/cookies';
import { API_BASE } from '../lib/api';
import PhotoDisplay from './PhotoDisplay';

const SWIPE_THRESHOLD = 100;

interface UserInfoResponse {
    userId: number;
    age: number;
    genres: string[];
    instruments: string[];
    location: string;
    about?: string;
    photoUrl?: string | null;
    message?: string;
}

interface CardData {
    userId: number;
    name: string;
    age: number;
    genres: string[];
    instruments: string[];
    location: string;
    about: string;
    photoUrl: string | null;
}

export default function SwipeCards() {
    const [currentCard, setCurrentCard] = useState<CardData | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [noMore, setNoMore] = useState(false);
    const [emptyMessage, setEmptyMessage] = useState('Карточки закончились');
    const [offsetX, setOffsetX] = useState(0);
    const [isDragging, setIsDragging] = useState(false);
    const [isAnimating, setIsAnimating] = useState(false);
    const dragStartX = useRef(0);

    const loadNextCard = useCallback(async () => {
        const token = authCookies.getAuthToken();
        const userData = authCookies.getUserData();
        const currentUserId = userData?.userId || userData?.id;

        if (!token || !currentUserId) {
            setIsLoading(false);
            return;
        }

        setIsLoading(true);

        try {
            const response = await fetch(`${API_BASE}/recommendations/next/${currentUserId}`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });

            if (!response.ok) {
                setNoMore(true);
                setCurrentCard(null);
                setEmptyMessage('Карточки закончились');
                return;
            }

            const data: UserInfoResponse = await response.json();

            if (!data.userId) {
                setNoMore(true);
                setCurrentCard(null);
                setEmptyMessage('Карточки закончились');
                return;
            }

            const mockMatch = MOCK_USERS.find((user) => user.about === data.about);

            setCurrentCard({
                userId: data.userId,
                name: mockMatch?.name ?? 'Музыкант',
                age: data.age,
                genres: data.genres ?? [],
                instruments: data.instruments ?? [],
                location: data.location ?? '',
                about: data.about ?? '',
                photoUrl: data.photoUrl ?? null,
            });
            setNoMore(false);
        } catch (error) {
            console.error('Ошибка загрузки карточки:', error);
            setNoMore(true);
            setCurrentCard(null);
            setEmptyMessage('Карточки закончились');
        } finally {
            setIsLoading(false);
        }
    }, []);

    useEffect(() => {
        loadNextCard();
    }, [loadNextCard]);

    const handleRefresh = async () => {
        const token = authCookies.getAuthToken();
        const userData = authCookies.getUserData();
        const currentUserId = userData?.userId || userData?.id;

        if (!token || !currentUserId) return;

        setIsLoading(true);
        setNoMore(false);
        setCurrentCard(null);

        try {
            const refreshResponse = await fetch(`${API_BASE}/recommendations/refresh/${currentUserId}`, {
                method: 'POST',
                headers: { Authorization: `Bearer ${token}` },
            });

            if (refreshResponse.ok) {
                const refreshData = await refreshResponse.json();
                if (refreshData.count === 0) {
                    setNoMore(true);
                    setEmptyMessage('Нет отклонённых пользователей для повторного показа');
                    setIsLoading(false);
                    return;
                }
            }

            await loadNextCard();
        } catch (error) {
            console.error('Ошибка обновления карточек:', error);
            setNoMore(true);
            setEmptyMessage('Не удалось обновить карточки');
            setIsLoading(false);
        }
    };

    const goToNext = () => {
        setOffsetX(0);
        setIsAnimating(false);
        loadNextCard();
    };

    const swipe = async (direction: 'left' | 'right') => {
        if (!currentCard || isAnimating) return;

        const token = authCookies.getAuthToken();
        const userData = authCookies.getUserData();
        const currentUserId = userData?.userId || userData?.id;

        setIsAnimating(true);
        setOffsetX(direction === 'right' ? 400 : -400);

        if (token && currentUserId) {
            try {
                await fetch(`${API_BASE}/swipes/create`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        userId1: Number(currentUserId),
                        userId2: currentCard.userId,
                        decision: direction === 'right',
                    }),
                });
            } catch (error) {
                console.error('Ошибка отправки свайпа:', error);
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
            swipe('right');
        } else if (offsetX < -SWIPE_THRESHOLD) {
            swipe('left');
        } else {
            setOffsetX(0);
        }
    };

    if (isLoading && !currentCard) {
        return (
            <div className="flex items-center justify-center p-8">
                <p>Загрузка карточек...</p>
            </div>
        );
    }

    if (noMore || !currentCard) {
        return (
            <div className="flex flex-col items-center justify-center gap-4 p-8">
                <p className="text-xl text-gray-600">{emptyMessage}</p>
                <button
                    onClick={handleRefresh}
                    className="px-6 py-2 border border-black rounded-md hover:bg-gray-100"
                >
                    Обновить
                </button>
            </div>
        );
    }

    return (
        <div className="flex flex-col items-center gap-6 w-full max-w-sm mx-auto">
            <div className="relative w-full h-[480px]">
                <Card
                    card={currentCard}
                    style={{
                        zIndex: 10,
                        transform: `translateX(${offsetX}px) rotate(${offsetX * 0.05}deg)`,
                        transition: isDragging ? 'none' : 'transform 0.25s ease',
                        cursor: isDragging ? 'grabbing' : 'grab',
                    }}
                    onPointerDown={handlePointerDown}
                    onPointerMove={handlePointerMove}
                    onPointerUp={handlePointerUp}
                    onPointerCancel={handlePointerUp}
                />
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
                    onClick={() => swipe('left')}
                    className="w-14 h-14 rounded-full border-2 border-red-400 text-red-400 text-2xl hover:bg-red-50"
                    aria-label="Пропустить"
                >
                    ✕
                </button>
                <button
                    onClick={() => swipe('right')}
                    className="w-14 h-14 rounded-full border-2 border-green-400 text-green-400 text-2xl hover:bg-green-50"
                    aria-label="Нравится"
                >
                    ♥
                </button>
            </div>
        </div>
    );
}

function Card({
    card,
    style,
    onPointerDown,
    onPointerMove,
    onPointerUp,
    onPointerCancel,
}: {
    card: CardData;
    style?: React.CSSProperties;
    onPointerDown?: (e: React.PointerEvent) => void;
    onPointerMove?: (e: React.PointerEvent) => void;
    onPointerUp?: () => void;
    onPointerCancel?: () => void;
}) {
    return (
        <div
            className="absolute inset-0 bg-white border border-gray-300 rounded-xl overflow-hidden shadow-md select-none touch-none"
            style={style}
            onPointerDown={onPointerDown}
            onPointerMove={onPointerMove}
            onPointerUp={onPointerUp}
            onPointerCancel={onPointerCancel}
        >
            <PhotoDisplay imageUrl={card.photoUrl} alt={card.name} />
            <div className="p-4 flex flex-col gap-2">
                <h2 className="text-2xl font-semibold">{card.name}, {card.age}</h2>
                <p className="text-gray-600">{card.location}</p>
                <p className="text-sm">
                    <span className="font-medium">Инструменты: </span>
                    {card.instruments.join(', ')}
                </p>
                <p className="text-sm">
                    <span className="font-medium">Жанры: </span>
                    {card.genres.join(', ')}
                </p>
                <p className="text-sm text-gray-700 line-clamp-3">{card.about}</p>
            </div>
        </div>
    );
}
