import React from 'react';
import { getPhotoUrl } from '../lib/api';

interface PhotoDisplayProps {
    imageUrl: string | null | undefined;
    alt: string;
    className?: string;
}

export default function PhotoDisplay({ imageUrl, alt, className = 'w-full h-56 object-cover bg-gray-200' }: PhotoDisplayProps) {
    const src = getPhotoUrl(imageUrl);

    if (src) {
        return <img src={src} alt={alt} className={className} draggable={false} />;
    }

    return (
        <div className={`flex items-center justify-center bg-gray-200 ${className}`}>
            <span className="text-2xl font-bold">ФОТО</span>
        </div>
    );
}
