import Link from 'next/link';
import React from "react";

const Header = () =>{
    return(
        <header className = "grid grid-cols-3 items-center text-black bg-gray-300 px-8 h-20 mb-6">
            <div className="flex justify-start h-full"><Link href={'/'} className="flex items-center px-3 hover:bg-gray-400"><img className="w-8 h-8" src="/assets/settings.png" alt="Настройки"/></Link></div>
            <div className="flex h-full justify-center text-2xl">
                <Link href={'/'} className="flex items-center px-3">
                    <div className="hover:text-3xl">H</div>
                    <div className="hover:text-3xl">A</div>
                    <div className="hover:text-3xl">R</div>
                    <div className="hover:text-3xl">M</div>
                    <div className="hover:text-3xl">O</div>
                    <div className="hover:text-3xl">N</div>
                    <div className="hover:text-3xl">Y</div>
                </Link>
            </div>
            <ul className = "flex items-center justify-end font-semibold text-base h-full">
                <li className="flex h-full items-center hover:bg-gray-400 px-3"><Link href={'/pages/chat'}><img src="/assets/chat.png" alt="Чат" className="h-8 w-8"/></Link></li>
                <li className="flex h-full items-center hover:bg-gray-400 px-3"><Link href={'/pages/likes'}><img src="/assets/heart.png" alt="Лайк" className="h-8 w-8"/></Link></li>
                <li className="flex h-full items-center hover:bg-gray-400 px-3"><Link href={'/pages/auth'}><img src="/assets/user.png" alt="auth" className="h-8 w-8"/></Link></li>
            </ul>
        </header>
    )
}

export default Header;