'use client';

import React from 'react'
import {useState, useEffect} from 'react';
import EditableTextInput from "../../components/EditableTextInput";
import {MultiSelect} from "../../components/MultiSelect";
import Buttons from "../../components/Buttons";
import EditableSelector from "../../components/EditableSelector";
import PhotoDisplay from "../../components/PhotoDisplay";
import {authCookies} from "../../components/common/cookies";
import {API_BASE, pickDisplayPhotoUrl} from "../../lib/api";

interface ListOption {
    value: string;
    label: string;
}

interface InstrumentResponse {
    name: string;
    type: string;
}

interface GenreResponse {
    name: string;
}

interface LocationResponse {
    name: string;
}

interface UserInfo{
    userId: number;
    age: number;
    genres: string[];
    instruments: string[];
    location: string;
    about?: string;
}

interface UserPhoto {
    imageId: number;
    imageUrl: string;
}

export default function Profile() {
    const [isLoading, setIsLoading] = useState(true);
    const [userInfo, setUserInfo] = useState<UserInfo | null>(null);
    const [isProfileComplete, setIsProfileComplete] = useState(false);
    const [userId, setUserId] = useState<number | null>(null);

    const [text, setText] = useState('');
    const [tempText, setTempText] = useState(text);
    const [isEditing, setIsEditing] = useState(false);
    const [age, setAge] = useState<number | ''>('');
    const [tempAge, setTempAge] = useState<number | ''>('');

    const [instrumentsList, setInstrumentsList] = useState<ListOption[]>([]);
    const [selectedInstruments, setSelectedInstruments] = useState<string[]>([]);
    const [tempSelectedInstruments, setTempSelectedInstruments] = useState<string[]>(selectedInstruments);
    const [genresList, setGenresList] = useState<ListOption[]>([]);
    const [selectedGenres, setSelectedGenres] = useState<string[]>([]);
    const [tempSelectedGenres, setTempSelectedGenres] = useState<string[]>(selectedGenres);
    const [locationsList, setLocationsList] = useState<ListOption[]>([]);
    const [selectedLocation, setSelectedLocation] = useState('');
    const [tempSelectedLocation, setTempSelectedLocation] = useState(selectedLocation);
    const [about, setAbout] = useState('');
    const [tempAbout, setTempAbout] = useState(about);
    const [photos, setPhotos] = useState<UserPhoto[]>([]);
    const [isUploadingPhoto, setIsUploadingPhoto] = useState(false);

    useEffect(() => {
        loadUserProfile();
    }, []);

    const loadUserProfile = async () => {
        try {
            const token = authCookies.getAuthToken();
            const userData = authCookies.getUserData();

            if (!token || !userData) {
                window.location.href = '/pages/auth';
                return;
            }

            const userName = userData.name || '';
            setText(userName);
            setTempText(userName);

            const currentUserId = userData.userId || userData.id;
            if (currentUserId) {
                setUserId(Number(currentUserId));
            }

            if (!currentUserId) {
                console.error('UserId не найден в данных пользователя');
                setIsLoading(false);
                return;
            }

            await Promise.all([
                loadInstruments(token),
                loadGenres(token),
                loadLocations(token),
                loadPhotos(token, Number(currentUserId)),
            ]);

            const response = await fetch(`http://localhost:8080/info/${currentUserId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const data = await response.json();
                setUserInfo(data);

                setAge(data.age || '');
                setTempAge(data.age || '');
                setSelectedInstruments(data.instruments || []);
                setTempSelectedInstruments(data.instruments || []);
                setSelectedGenres(data.genres || []);
                setTempSelectedGenres(data.genres || []);
                setSelectedLocation(data.location || '');
                setTempSelectedLocation(data.location || '');
                setAbout(data.about || '');
                setTempAbout(data.about || '');

                const complete = checkProfileComplete(data);
                setIsProfileComplete(complete);
            } else if (response.status === 404) {
                setIsProfileComplete(false);
            } else {
                console.error('Ошибка загрузки профиля');
            }
        } catch (error) {
            console.error('Ошибка загрузки профиля:', error)
        } finally {
            setIsLoading(false);
        }
    }

    const loadInstruments = async (token: string) => {
        try {
            const response = await fetch('http://localhost:8080/list/instruments', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const data: InstrumentResponse[] = await response.json();
                setInstrumentsList(data.map((instrument) => ({
                    value: instrument.name,
                    label: instrument.name,
                })));
            } else {
                console.error('Ошибка загрузки списка инструментов');
            }
        } catch (error) {
            console.error('Ошибка загрузки списка инструментов:', error);
        }
    };

    const loadGenres = async (token: string) => {
        try {
            const response = await fetch('http://localhost:8080/list/genres', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const data: GenreResponse[] = await response.json();
                setGenresList(data.map((genre) => ({
                    value: genre.name,
                    label: genre.name,
                })));
            } else {
                console.error('Ошибка загрузки списка жанров');
            }
        } catch (error) {
            console.error('Ошибка загрузки списка жанров:', error);
        }
    };

    const loadLocations = async (token: string) => {
        try {
            const response = await fetch('http://localhost:8080/list/locations', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const data: LocationResponse[] = await response.json();
                setLocationsList(data.map((location) => ({
                    value: location.name,
                    label: location.name,
                })));
            } else {
                console.error('Ошибка загрузки списка городов');
            }
        } catch (error) {
            console.error('Ошибка загрузки списка городов:', error);
        }
    };

    const loadPhotos = async (token: string, currentUserId: number) => {
        try {
            const response = await fetch(`${API_BASE}/photo/user/${currentUserId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });

            if (response.ok) {
                const data: UserPhoto[] = await response.json();
                setPhotos(data);
            } else {
                console.error('Ошибка загрузки фотографий');
            }
        } catch (error) {
            console.error('Ошибка загрузки фотографий:', error);
        }
    };

    const handlePhotoUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        const token = authCookies.getAuthToken();
        const userData = authCookies.getUserData();
        const currentUserId = userId || userData?.userId || userData?.id;

        if (!token || !currentUserId) return;

        setIsUploadingPhoto(true);

        try {
            const formData = new FormData();
            formData.append('file', file);

            const response = await fetch(`${API_BASE}/photo/upload`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                body: formData,
            });

            if (response.ok) {
                const photo: UserPhoto = await response.json();
                setPhotos((prev) => [...prev, photo]);
            } else {
                const errorData = await response.json();
                console.error('Ошибка загрузки фото:', errorData.error);
            }
        } catch (error) {
            console.error('Ошибка загрузки фото:', error);
        } finally {
            setIsUploadingPhoto(false);
            e.target.value = '';
        }
    };

    const checkProfileComplete = (info: UserInfo | null): boolean => {
        if (!info) return false;

        return !!(
            info.age &&
            info.genres &&
            info.genres.length > 0 &&
            info.instruments &&
            info.instruments.length > 0 &&
            info.location &&
            info.location.trim() !== ''
        );
    };

    const handleEdit = () => {
        setTempText(text);
        setTempAge(age);
        setTempSelectedInstruments(selectedInstruments);
        setTempSelectedGenres(selectedGenres);
        setTempSelectedLocation(selectedLocation);
        setTempAbout(about);
        setIsEditing(true);
    }

    const handleSave = async () => {
        try {
            const token = authCookies.getAuthToken();
            const userData = authCookies.getUserData();

            if (!token || !userData) {
                console.log('Ошибка аутентификации');
                return;
            }

            const currentUserId = userId || userData.userId || userData.id;

            if (!currentUserId) {
                console.log('Ошибка: не удалось определить ID пользователя');
                return;
            }

            if (!tempAge || tempSelectedInstruments.length === 0 || tempSelectedGenres.length === 0 || !tempSelectedLocation) {
                console.log('Заполните все обязательные поля: возраст, инструменты, жанры, город');
                return;
            }

            const ageValue: number = typeof tempAge === 'number'
                ? tempAge
                : (tempAge === '' ? 0 : Number(tempAge));

            if (!ageValue || ageValue <= 0) {
                console.log('Введите корректный возраст');
                return;
            }

            const response = await fetch ("http://localhost:8080/info/update", {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    userId: currentUserId,
                    age: ageValue,
                    genres: tempSelectedGenres,
                    instruments: tempSelectedInstruments,
                    location: tempSelectedLocation,
                    about: tempAbout || '',
                }),
            });

            if (response.ok) {
                const data = await response.json();
                setText(tempText);
                setAge(tempAge);
                setSelectedInstruments(tempSelectedInstruments);
                setSelectedGenres(tempSelectedGenres);
                setSelectedLocation(tempSelectedLocation);
                setAbout(tempAbout);
                setIsEditing(false);

                const complete = checkProfileComplete(data);
                setIsProfileComplete(complete);

                if (complete) {
                    console.log('Профиль успешно сохранен!');
                }
            } else {
                const errorData = await response.json();
                console.log('Ошибка сохранения: ' + (errorData.message || 'Неизвестная ошибка'));
            }
        } catch (error) {
            console.error('Ошибка сохранения профиля', error);
            console.log('Ошибка сети или сервера');
        }
    };

    const handleCancel = () => {
        setTempText(text);
        setTempAge(age);
        setTempSelectedInstruments(selectedInstruments);
        setTempSelectedGenres(selectedGenres);
        setTempSelectedLocation(selectedLocation);
        setTempAbout(about);

        if (isProfileComplete){
            setIsEditing(false);
        }

    };

    if (isLoading){
        return (
            <div className = "flex flex-col items-center justify-center w-full h-full">
                <div>Загрузка профиля...</div>
            </div>
        );
    }


    return(
        <div className="flex flex-col items-center justify-center w-full">
            <h1 className="text-3xl mb-4">
                {isProfileComplete ? 'Профиль' : 'Завершите регистрацию'}
            </h1>
            {!isProfileComplete && (
                <p className="text-gray-600 mb-4">Пожалуйста, заполните обязательные поля для завершения регистрации</p>
            )}
            <div className="flex justify-center gap-4">
                <div className="flex flex-col items-center gap-2 w-48">
                    <PhotoDisplay
                        imageUrl={pickDisplayPhotoUrl(photos)}
                        alt="Фото профиля"
                        className="w-48 h-48 object-cover bg-gray-200 rounded-md"
                    />
                    {isEditing && (
                        <label className="w-full text-center cursor-pointer border border-black rounded-md px-2 py-1 text-sm hover:bg-gray-100">
                            {isUploadingPhoto ? 'Загрузка...' : 'Загрузить фото'}
                            <input
                                type="file"
                                accept="image/jpeg,image/png,image/webp,image/heic"
                                className="hidden"
                                onChange={handlePhotoUpload}
                                disabled={isUploadingPhoto}
                            />
                        </label>
                    )}
                </div>
                <div className="flex flex-col w-200 text-xl gap-3">
                    <EditableTextInput
                        text={text}
                        tempText={tempText}
                        isEditing={isEditing}
                        setTempText={setTempText}
                    />
                    <div>
                        <label className="text-sm text-gray-600">Возраст *</label>
                        {isEditing ? (
                            <input
                                type="number"
                                value={tempAge}
                                onChange={(e) => setTempAge(e.target.value ? parseInt(e.target.value) : '')}
                                className="text-2xl border-1 border-black rounded-md px-2 w-full"
                                min="1"
                                max="120"
                                required
                            />
                        ) : (
                            <div className="text-2xl">{age || 'Не указан'}</div>
                        )}
                    </div>
                    <MultiSelect
                        options={instrumentsList}
                        onValueChange={setTempSelectedInstruments}
                        value={tempSelectedInstruments}
                        placeholder="Выбери музыкальные инструменты *"
                        variant="inverted"
                        disabled={!isEditing}
                        animation={1}
                        maxCount={10}
                    />
                    <MultiSelect
                        options={genresList}
                        onValueChange={setTempSelectedGenres}
                        value={tempSelectedGenres}
                        placeholder="Выбери жанры *"
                        variant="inverted"
                        disabled={!isEditing}
                        animation={1}
                        maxCount={10}
                    />
                    <EditableSelector
                        isEditing={isEditing}
                        selectedLocation={selectedLocation}
                        tempSelectedLocation={tempSelectedLocation}
                        setTempSelectedLocation={setTempSelectedLocation}
                        locations={locationsList}
                    />
                    {isEditing ? (
                        <textarea
                            name="about"
                            value={tempAbout}
                            onChange={(e) => setTempAbout(e.target.value)}
                            placeholder="О себе"
                            className="w-full h-full pl-1 bg-gray-100 resize-none"
                        />
                    ) : (
                        <textarea
                            name="about"
                            value={about}
                            placeholder="О себе"
                            className="w-full h-full pl-1 bg-gray-100 resize-none"
                            disabled
                        />
                    )}
                    <Buttons
                        isEditing={isEditing}
                        setIsEditing={setIsEditing}
                        edit={handleEdit}
                        save={handleSave}
                        cancel={handleCancel}
                    />
                </div>
            </div>
        </div>
    );
}