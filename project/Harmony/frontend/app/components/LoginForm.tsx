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

    const {login, isAuthenticated} = useAuth();

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
    if (isAuthenticated) {
        return (
            <h1>Вы вошли в систему</h1>
        );
    }

    return(
        <div className="flex justify-center h-full items-center">
            <form onSubmit={handleSubmit} className="flex flex-col">
                <input type="email" name = "email" id = "email" value = {formData.email} onChange = {handleChange} required placeholder="Email" />
                <input type="password" name = "password" id = "password" value = {formData.password} onChange = {handleChange} required placeholder="Password" />
                <button type = "submit" disabled = {isLoading}>{isLoading ? 'Вход...' : 'Войти'}</button>
                {message && (<div className={`${message.includes('успешн') ? 'text-green-100' : 'text-red-100'}`}>{message}</div>)}
            </form>
        </div>
    );
}