'use client';

import React from "react";
import {useState} from "react";

export default function RegistrationForm() {

    const [formData, setFormData] = useState({
        name: "",
        email:"",
        password:"",
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
                setMessage('Регистрация прошла успешно');
                setFormData({name: '', email: '', password: ''});
                console.log('Регистрация прошла успешно', data);
            } else {
                setMessage(data.message || 'Ошибка регистрации');
            }
        } catch (error) {
            setMessage('Ошибка сети или сервера');
            console.error('Ошибка регистрации', error);
        }finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="flex justify-center h-full items-center">
            <form onSubmit={handleSubmit} className="flex flex-col">
                <input type="name" name = "name" id = "name" value = {formData.name} onChange = {handleChange} required placeholder="Username" />
                <input type="email" name = "email" id = "email" value = {formData.email} onChange = {handleChange} required placeholder="Email" />
                <input type="password" name = "password" id = "password" value = {formData.password} onChange = {handleChange} required placeholder="Password" />
                <button type = "submit" disabled = {isLoading}>{isLoading ? 'Регистрация...' : 'Зарегистрироваться'}</button>

                {message && (<div className={`${message.includes('успешн') ? 'bg-green-100' : 'bg-red-100'}`}>{message}</div>)}
            </form>
        </div>
    );
}