import * as React from 'react';
import Image from "next/image";

export default function Blockcard({image}) {

    return (
        <div className="block">
            <div className="absolute">
                {/* <Image src={image} className="cover-img"/> */}
            </div>
            <div className="padding h-full w-full">
                <div className="block-card-title">
                    Fiestas
                </div>
                <div className="block-card-subtitle">
                    por la zona
                </div>
            </div>
        </div>
    );
};