import React from 'react'

export default function Profile() {
    return(<div>
        <h1>Профиль</h1>
        <div className="grid grid-cols-4 min-h-screen p-5">
            <div className="justify-start border border-2"><img src="/images/Frame%208.png" alt="Avatar"/></div>
            <div className = "flex flex-col col-span-3 border border-2 text-2xl">
                <div>
                    <input type="text"  placeholder = "Имя"/>
                    <label htmlFor="">Возраст</label>
                </div>
                <select name="instrument" id="">
                    <option value="">Инструмент</option>
                    <option value="piano">Фортепиано</option>
                    <option value="violin">Срипка</option>
                    <option value="guitar">Гитара</option>
                </select>
                <select name="genre" id="">
                    <option value="">Жанр</option>
                    <option value="pop">Поп</option>
                    <option value="rock">Рок</option>
                    <option value="edm">EDM</option>
                </select>
                <select name="location" id="">
                    <option value="">Город</option>
                    <option value="moscow">Москва</option>
                    <option value="spb">Санкт-Петербург</option>
                </select>
                <textarea name="about" id="" placeholder="О себе" className = "w-full h-full"></textarea>
                <button type="submit" className="flex justify-end border border-2"><img src="/images/accept.png" alt="edit"/></button>
            </div>
        </div>
    </div>)
}