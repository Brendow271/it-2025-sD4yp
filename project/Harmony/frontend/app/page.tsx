'use client'

import React, {useState} from "react";
import {useAuth} from "./hooks/useAuth";
import Profile from "./pages/profile/page";

export default function Home() {
    const {logout, isAuthenticated} = useAuth();
    if (isAuthenticated){
        return(
            <button onClick = {logout}>Выйти</button>
        )
    }
    return (
        <div>
            <h1>
                <Profile/>
            </h1>

        </div>

  );
}
