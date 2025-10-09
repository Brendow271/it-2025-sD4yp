'use client';
import RegistrationForm from "@/app/components/RegistrationForm";
import LoginForm from "../../components/LoginForm";
export default function Auth(){
    return(
        <div className="flex justify-center h-full items-center">
            <div className="flex flex-col">
                <h1>Аутентификация</h1>
                <LoginForm/>
            </div>

        </div>
    )
}