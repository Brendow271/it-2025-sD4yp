'use client';

import React from 'react';
import { ProtectedRoute } from "../../components/ProtectedRoute";
import IncomingLikesCards from "../../components/IncomingLikesCards";

export default function Likes() {
    return (
        <ProtectedRoute>
            <IncomingLikesCards />
        </ProtectedRoute>
    );
}
