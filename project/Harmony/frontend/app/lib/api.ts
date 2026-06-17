export const API_BASE = 'http://localhost:8080';

export function getPhotoUrl(imageUrl: string | null | undefined): string | null {
    if (!imageUrl) return null;
    if (imageUrl.startsWith('http')) return imageUrl;
    return `${API_BASE}${imageUrl}`;
}

export function pickDisplayPhotoUrl(photos: { imageUrl: string }[]): string | null {
    if (photos.length === 0) return null;
    const uploaded = photos.filter((photo) => photo.imageUrl.startsWith('/uploads/'));
    if (uploaded.length === 0) return null;
    return uploaded[uploaded.length - 1].imageUrl;
}
