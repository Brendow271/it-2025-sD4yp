'use client'

import React, {useState} from "react";
import {useAuth} from "./hooks/useAuth";
import SwipeCards from "./components/SwipeCards";

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
                Home
                <SwipeCards/>
            </h1>

        </div>

  );
}
