'use client'

import {useState} from 'react';
import {useAuth} from "../hooks/useAuth";
import React from 'react'

export default function LoginForm() {
    const [formData, setFormData] = useState({
        email: '',
        password: ''
    });

    const [isLoading, setIsLoading] = useState(false);
    const [message, setMessage] = useState('');

    const {login} = useAuth();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setFormData(prev =>({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setMessage('');

        try{
            const success = await login(formData.email, formData.password);

            if (success){
                setMessage('Вход выполнен успешно!');
                setFormData({email:'', password:''});
            }else{
                setMessage('Ошибка входа. Проверьте логин или пароль')
            }
        }catch(error){
            setMessage('Ошибка сети или сервера');
            console.error('Login error: ', error);
        }finally {
            setIsLoading(false);
        }
    };

    return(
        <div className="flex flex-col justify-center h-full w-full items-center border border-gray-200 bg-gray-200 px-4 py-4">
            <h1 className="mb-4">Аутентификация</h1>
            <form onSubmit={handleSubmit} className="flex flex-col gap-4 w-full">
                <input type="email" name = "email" id = "email" className="bg-white px-2" value = {formData.email} onChange = {handleChange} required placeholder="Email" />
                <input type="password" name = "password" id = "password" className="bg-white px-2" value = {formData.password} onChange = {handleChange} required placeholder="Password" />
                <button type = "submit" className="bg-gray-300 py-1 hover:bg-gray-400" disabled = {isLoading}>{isLoading ? 'Вход...' : 'Войти'}</button>
                {message && (<div className={`${message.includes('успешн') ? 'text-green-100' : 'text-red-100'}`}>{message}</div>)}
            </form>
        </div>
    );
}