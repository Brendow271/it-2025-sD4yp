import {ProtectedRoute} from "../../components/ProtectedRoute";
import React from 'react'
export default function Likes(){
    return(
        <ProtectedRoute>
            <h1>Likes</h1>
        </ProtectedRoute>
    )
}