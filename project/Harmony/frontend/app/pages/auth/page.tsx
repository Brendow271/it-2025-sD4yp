'use client';
import RegistrationForm from "../../components/RegistrationForm";
import React, {useState, useEffect} from 'react'
import LoginForm from "../../components/LoginForm";

export default function Auth(){
    const [activeTab, setActiveTab] = useState<'login' | 'register'>('login');
    return(
        <div className="flex justify-center h-full items-center">
            <div className="flex flex-col w-full max-w-md">
                <div className="flex border-b border-gray-200 mb-4">
                    <button onClick={()=> setActiveTab('login')}
                    className={`flex-1 py-2 px-4 text-center front-medium transition-colors ${
                        activeTab === 'login' 
                            ? 'border-b-2 border-blue-500 text-blue-600 bg-gray-50' 
                            : 'text-gray-600 hover:tet-gray-800 hover:bg-gray-50'
                    }`}>
                        Вход
                    </button>
                    <button onClick={()=> setActiveTab('register')}
                    className={`flex-1 py-2 px-4 text-center front-medium transition-colors ${
                        activeTab === 'register' 
                            ? 'border-b-2 border-blue-500 text-blue-600 bg-gray-50' 
                            : 'text-gray-600 hover:tet-gray-800 hover:bg-gray-50'
                    }`}>
                        Регистрация
                    </button>
                </div>
                <div className="w-full">
                    {activeTab === 'login' ? <LoginForm /> : <RegistrationForm />}
                </div>
            </div>
        </div>
    )
}