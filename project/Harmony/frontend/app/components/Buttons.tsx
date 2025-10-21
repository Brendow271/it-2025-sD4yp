'use client'

import React from 'react'

export default function Buttons(props: {isEditing: boolean, setIsEditing: (value: boolean) => void, edit: () => void, save: () => void, cancel: () => void}) {
    return (
        <>
            {!props.isEditing ?
                (<div className="flex justify-end"><button type="submit" onClick={props.edit} className="flex justify-center items-center w-15 h-15"><img src="/images/edit.png" alt="edit" className="w-10 hover:scale-120 transition"/></button></div>)
            : (
                <div className="flex justify-end">
                    <button type="submit" onClick={props.save} className="flex justify-center items-center w-15 h-15"><img src="/images/accept.png" alt="edit" className="w-7 hover:scale-120 transition"/></button>
                    <button type="submit" onClick={props.cancel} className="flex justify-center items-center w-15 h-15"><img src="/images/cross.png" alt="edit" className="w-6 hover:scale-120 transition"/></button>
                    <button type="submit" onClick={props.edit} className="flex justify-center items-center w-15 h-15"><img src="/images/edit.png" alt="edit" className="w-10 hover:scale-120 transition"/></button>
                </div>
                )}
        </>
    )
}