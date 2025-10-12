'use client'

import {useAuth} from "./hooks/useAuth";

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
            </h1>

        </div>

  );
}
