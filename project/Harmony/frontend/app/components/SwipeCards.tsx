import React from 'react'
import {Card} from '../types/card'

const SwipeCards = () => {
    return(
        <div
        className="min-h-screen bg-neutral-100 grid place-items-center"
        style={{
            backgroundImage:`url("../public/images/card1.png")`,
        }}
        >
            {cardData.map((card) =>{
                return (<Card key={card.id} {...card}/>)
            })}
        </div>
    );
};

const Card = ({id, url}: {id: number, url: string})  => {
    return <img src={url} alt="placeholder" className = "h-90 object-cover" style={{gridRow: 1, gridColumn: 1,}}/>
};

export default SwipeCards;

const cardData: Card[] = [
    {
        id: 1,
        url: "/images/card2.png",
    },
    {
        id: 2,
        url: "/images/card3.png"
    },
    {
        id: 3,
        url: "/images/card4.png"
    }
]