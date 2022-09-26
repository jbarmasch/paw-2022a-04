import * as React from 'react';
import Image from "next/image";

export default function Blockcard({image}) {

    return (
        <div className="block">
            <div className="absolute">
                <Image src={image} className="cover_img"/>
            </div>
            <div className="padding h-full w-full">
                <div className="block_card_title">
                    Fiestas
                </div>
                <div className="block_card_subtitle">
                    por la zona
                </div>
            </div>
        </div>
    );
};