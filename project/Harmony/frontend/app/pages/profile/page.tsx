'use client';

import React from 'react'

import {useState} from 'react';
import EditableTextInput from "../../components/EditableTextInput";
import Buttons from "../../components/Buttons";

export default function Profile() {
    const [text, setText] = useState('Name');
    const [isEditing, setIsEditing] = useState(false);
    const [tempText, setTempText] = useState(text);

    const handleEdit = () => {
        setTempText(text);
        setIsEditing(true);
    }
     const handleSave = () => {
        setText(tempText);
        setIsEditing(false);
    };

    const handleCancel = () => {
        setTempText(text);
        setIsEditing(false);
    };

    return(
        <div className="flex flex-col items-center justify-center w-full">
        <h1 className="text-3xl">Профиль</h1>
        <div className="flex justify-center gap-4">
            <div><img src="/images/Frame%208.png" alt="Avatar"/></div>
            <div className = "flex flex-col w-200 text-xl gap-3">
                <div>
                   <EditableTextInput
                       text = {text}
                       tempText = {tempText}
                       isEditing={isEditing}
                       setTempText={setTempText}
                   />
                </div>
                <select name="instrument" id="" className = "border border-1 border-gray-200 p-1 rounded-md">
                    <option value="">Инструмент</option>
                    <option value="piano">Фортепиано</option>
                    <option value="violin">Срипка</option>
                    <option value="guitar">Гитара</option>
                </select>
                <select name="genre" id="" className = "border border-1 border-gray-200 p-1 rounded-md">
                    <option value="">Жанр</option>
                    <option value="pop">Поп</option>
                    <option value="rock">Рок</option>
                    <option value="edm">EDM</option>
                </select>
                <select name="location" id="" className = "border border-1 border-gray-200 p-1 rounded-md">
                    <option value="">Город</option>
                    <option value="moscow">Москва</option>
                    <option value="spb">Санкт-Петербург</option>
                </select>
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