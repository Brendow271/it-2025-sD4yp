'use client'

import React from 'react'
import {Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue} from "./ui/select";

export default function EditableSelector(props: {isEditing: boolean, tempSelectedLocation: string, selectedLocation: string,setTempSelectedLocation: (value: string) => void}) {
    return(
        <Select disabled={!props.isEditing} value = {props.tempSelectedLocation} onValueChange = {props.setTempSelectedLocation}>
            <SelectTrigger>
                <SelectValue placeholder="Выбери город"></SelectValue>
            </SelectTrigger>
            <SelectContent>
                <SelectGroup>
                    <SelectLabel>Города</SelectLabel>
                    <SelectItem value="Москва">Москва</SelectItem>
                    <SelectItem value="Санкт-Петербург">Санкт-Петербург</SelectItem>
                    <SelectItem value="Хабаровск">Хабаровск</SelectItem>
                    <SelectItem value="Краснодар">Краснодар</SelectItem>
                </SelectGroup>
            </SelectContent>
        </Select>
    )
}