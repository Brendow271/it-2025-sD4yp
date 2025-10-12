import {ProtectedRoute} from "../../components/ProtectedRoute";
import React from 'react'
export default function Chat(){
    return(
        <ProtectedRoute>
            <h1>Chat</h1>
        </ProtectedRoute>
    )
}