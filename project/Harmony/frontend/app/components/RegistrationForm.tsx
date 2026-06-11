'use client';

import React from "react";
import {useState} from "react";
import {authCookies} from "./common/cookies";

export default function RegistrationForm() {

    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: "",
    });
    const [isLoading, setIsLoading] = useState(false);
    const [message, setMessage] = useState("");

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const{name, value} = e.target;
        setFormData(prev =>({
            ...prev,
            [name]:value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setMessage("");

        try{
            const response = await fetch("http://localhost:8080/auth/register", {
                method: "POST",
                headers:{
                    'content-type': 'application/json',
                },
                body: JSON.stringify(formData),
            });
            const data = await response.json();

            if (response.ok){
                if (data.token){
                    authCookies.setAuthToken(data.token);
                    if (data.user){
                        authCookies.setUserData(data.user);
                    }
                    window.location.href = '/pages/profile';
                } else{
                    setMessage('Регистрация прошла успешно');
                }
            } else {
                setMessage(data.message || data.error || 'Ошибка регистрации');
            }
        } catch (error) {
            setMessage('Ошибка сети или сервера');
            console.error('Ошибка регистрации', error);
        }finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="flex flex-col justify-center h-full w-full items-center border border-gray-200 bg-gray-200 px-4 py-4">
            <h1 className="mb-4">Регистрация</h1>
            <form onSubmit={handleSubmit} className="flex flex-col gap-4 w-full">
                <input type="name" name = "name" id = "name" className="bg-white px-2" value = {formData.name} onChange = {handleChange} required placeholder="Username" />
                <input type="email" name = "email" id = "email" className="bg-white px-2" value = {formData.email} onChange = {handleChange} required placeholder="Email" />
                <input type="password" name = "password" id = "password" className="bg-white px-2" value = {formData.password} onChange = {handleChange} required placeholder="Password" />
                <button type = "submit" className="bg-gray-300 py-1 hover:bg-gray-400" disabled = {isLoading}>{isLoading ? 'Регистрация...' : 'Зарегистрироваться'}</button>

                {message && (<div className={`${message.includes('успешн') ? 'bg-green-100' : 'bg-red-100'}`}>{message}</div>)}
            </form>
        </div>
    );
}