'use client';

import React from 'react'

import {useState} from 'react';
import EditableTextInput from "../../components/EditableTextInput";
import {MultiSelect} from "../../components/MultiSelect";
import Buttons from "../../components/Buttons";

import { Cat, Dog, Fish, Rabbit, Turtle } from "lucide-react";

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
export default function Profile() {
    const [text, setText] = useState('Name');
    const [tempText, setTempText] = useState(text);
    const [isEditing, setIsEditing] = useState(false);

    const [selectedInstruments, setSelectedInstruments] = useState<string[]>([]);
    const [tempSelectedInstruments, setTempSelectedInstruments] = useState<string[]>(selectedInstruments);
    const [selectedGenres, setSelectedGenres] = useState<string[]>([]);
    const [tempSelectedGenres, setTempSelectedGenres] = useState<string[]>(selectedGenres);
    const [selectedLocations, setSelectedLocations] = useState<string[]>([]);
    const [tempSelectedLocations, setTempSelectedLocations] = useState<string[]>(selectedLocations);

    const handleEdit = () => {
        setTempText(text);
        setTempSelectedInstruments(selectedInstruments);
        setTempSelectedGenres(selectedGenres);
        setTempSelectedLocations(selectedLocations);
        setIsEditing(true);
    }
     const handleSave = () => {
        setText(tempText);
        setSelectedInstruments(tempSelectedInstruments);
         setSelectedGenres(tempSelectedGenres);
         setSelectedLocations(tempSelectedLocations);
        setIsEditing(false);
    };

    const handleCancel = () => {
        setTempText(text);
        setTempSelectedInstruments(selectedInstruments);
        setTempSelectedGenres(selectedGenres);
        setTempSelectedLocations(selectedLocations);
        setIsEditing(false);
    };

    return(
        <div className="flex flex-col items-center justify-center w-full">
        <h1 className="text-3xl">Профиль</h1>
        <div className="flex justify-center gap-4">
            <div><img src="/images/Frame%208.png" alt="Avatar"/></div>
            <div className = "flex flex-col w-200 text-xl gap-3">
                <EditableTextInput
                       text = {text}
                       tempText = {tempText}
                       isEditing={isEditing}
                       setTempText={setTempText}
                />
                <MultiSelect
                    options={instrumentsList} // Pass your options
                    onValueChange={setTempSelectedInstruments} // Function to update state
                    value={tempSelectedInstruments} // Set initial selected values
                    placeholder="Выбери музыкальные инструменты" // Customize placeholder
                    variant="inverted" // Choose a style variant
                    disabled = {!isEditing}
                    animation={1} // Optional animation duration
                    maxCount={10} // Max tags to display before summarizing
                />
                <MultiSelect
                    options={genresList} // Pass your options
                    onValueChange={setTempSelectedGenres} // Function to update state
                    value={tempSelectedGenres} // Set initial selected values
                    placeholder="Выбери жанры" // Customize placeholder
                    variant="inverted" // Choose a style variant
                    disabled = {!isEditing}
                    animation={1} // Optional animation duration
                    maxCount={10} // Max tags to display before summarizing
                />
                <MultiSelect
                    options={locationsList} // Pass your options
                    onValueChange={setTempSelectedLocations} // Function to update state
                    value={tempSelectedLocations} // Set initial selected values
                    placeholder="Выбери город" // Customize placeholder
                    variant="inverted" // Choose a style variant
                    disabled = {!isEditing}
                    animation={1} // Optional animation duration
                    maxCount={10} // Max tags to display before summarizing
                />
                <textarea name="about" id="" placeholder="О себе" className = "w-full h-full pl-1 bg-gray-100 resize-none"></textarea>
                <Buttons
                    isEditing={isEditing}
                    setIsEditing={setIsEditing}
                    edit={handleEdit}
                    save = {handleSave}
                    cancel = {handleCancel}
                />
            </div>
        </div>
        </div>);
}