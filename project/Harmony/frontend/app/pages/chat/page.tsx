'use client';

import React from 'react';
import { ProtectedRoute } from "../../components/ProtectedRoute";
import MatchList from "../../components/MatchList";

export default function Chat() {
    return (
        <ProtectedRoute>
            <MatchList />
        </ProtectedRoute>
    );
}
