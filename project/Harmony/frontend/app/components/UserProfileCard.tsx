'use client';

import React from 'react';
import PhotoDisplay from './PhotoDisplay';

export interface UserCardData {
    userId: number;
    name: string;
    age: number;
    genres: string[];
    instruments: string[];
    location: string;
    about: string;
    photoUrl: string | null;
}

interface UserProfileCardProps {
    user: UserCardData;
    className?: string;
}

export default function UserProfileCard({ user, className = '' }: UserProfileCardProps) {
    return (
        <div className={`bg-white border border-gray-300 rounded-xl overflow-hidden shadow-md ${className}`}>
            <PhotoDisplay imageUrl={user.photoUrl} alt={user.name} />
            <div className="p-4 flex flex-col gap-2">
                <h2 className="text-2xl font-semibold">{user.name}, {user.age}</h2>
                <p className="text-gray-600">{user.location}</p>
                <p className="text-sm">
                    <span className="font-medium">Инструменты: </span>
                    {(user.instruments ?? []).join(', ')}
                </p>
                <p className="text-sm">
                    <span className="font-medium">Жанры: </span>
                    {(user.genres ?? []).join(', ')}
                </p>
                <p className="text-sm text-gray-700 line-clamp-3">{user.about}</p>
            </div>
        </div>
    );
}
