'use client';

import React from 'react'
import {useState, useEffect} from 'react';
import EditableTextInput from "../../components/EditableTextInput";
import {MultiSelect} from "../../components/MultiSelect";
import Buttons from "../../components/Buttons";
import { Cat, Dog, Fish, Rabbit, Turtle } from "lucide-react";
import EditableSelector from "../../components/EditableSelector";
import {authCookies} from "../../components/common/cookies";

const instrumentsList = [
    { value: "Пианино", label: "Пианино", icon: Turtle },
    { value: "Гитара", label: "Гитара", icon: Cat },
    { value: "Скрипка", label: "Скрипка", icon: Dog },
    { value: "Барабан", label: "Барабан", icon: Rabbit },
    { value: "Труба", label: "Труба", icon: Fish },
];

const genresList = [
    { value: "Поп", label: "Поп", icon: Turtle },
    { value: "Рок", label: "Рок", icon: Cat },
    { value: "Рэп", label: "Рэп", icon: Dog },
    { value: "Джаз", label: "Джаз", icon: Rabbit },
    { value: "Блюз", label: "Блюз", icon: Fish },
];

const locationsList = [
    { value: "Cанкт-Петербург", label: "Санкт-Петербург", icon: Turtle },
    { value: "Москва", label: "Москва", icon: Cat },
    { value: "Хабаровск", label: "Хабаровск", icon: Dog },
    { value: "Краснодар", label: "Краснодар", icon: Rabbit },
];

interface UserInfo{
    userId: number;
    age: number;
    genres: string[];
    instruments: string[];
    location: string;
    about?: string;
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

    const [selectedInstruments, setSelectedInstruments] = useState<string[]>([]);
    const [tempSelectedInstruments, setTempSelectedInstruments] = useState<string[]>(selectedInstruments);
    const [selectedGenres, setSelectedGenres] = useState<string[]>([]);
    const [tempSelectedGenres, setTempSelectedGenres] = useState<string[]>(selectedGenres);
    const [selectedLocation, setSelectedLocation] = useState('');
    const [tempSelectedLocation, setTempSelectedLocation] = useState(selectedLocation);
    const [about, setAbout] = useState('');
    const [tempAbout, setTempAbout] = useState(about);

    useEffect(() => {
        loadUserProfile();
    }, []);

    const loadUserProfile = async () => {
        try {
            const token = authCookies.getAuthToken();
            const userData = authCookies.getUserData();

            if (!token || !userData) {
                window.location.href = 'pages/auth';
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

            const response = await fetch(`http://localhost:8080/info/${userId}`, {
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
                setAbout(data.about || '');
                setTempAbout(data.about || '');

                const complete = checkProfileComplete(data);
                setIsProfileComplete(complete);
                setIsProfileComplete(!complete);
            } else if (response.status === 404) {
                setIsProfileComplete(false);
                setIsProfileComplete(true);
            } else {
                console.error('Ошибка загрузки профиля');
            }
        } catch (error) {
            console.error('Ошибка загрузки профиля:', error)
        } finally {
            setIsLoading(false);
        }
    }

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
                <div><img src="/images/Frame%208.png" alt="Avatar"/></div>
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