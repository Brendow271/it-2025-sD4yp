'use client'

import React from 'react'
import {Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue} from "./ui/select";

interface LocationOption {
    value: string;
    label: string;
}

export default function EditableSelector(props: {
    isEditing: boolean;
    tempSelectedLocation: string;
    selectedLocation: string;
    setTempSelectedLocation: (value: string) => void;
    locations: LocationOption[];
}) {
    return(
        <Select disabled={!props.isEditing} value = {props.tempSelectedLocation} onValueChange = {props.setTempSelectedLocation}>
            <SelectTrigger>
                <SelectValue placeholder="Выбери город"></SelectValue>
            </SelectTrigger>
            <SelectContent>
                <SelectGroup>
                    <SelectLabel>Города</SelectLabel>
                    {props.locations.map((location) => (
                        <SelectItem key={location.value} value={location.value}>
                            {location.label}
                        </SelectItem>
                    ))}
                </SelectGroup>
            </SelectContent>
        </Select>
    )
}