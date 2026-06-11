'use client'

import React from 'react'

export default function EditableTextInput(props: {text: string, tempText: string, isEditing: boolean, setTempText: (value: string) => void}) {
    return(
        <>
            {props.isEditing ?
                (<input type="text"  placeholder = "Имя" value = {props.tempText} onChange={(e) => props.setTempText(e.target.value)} className="text-2xl border-1 border-black rounded-md"/>)
                : (<label className="text-2xl pb-1">{props.text}</label>)
            }
        </>
    )
}