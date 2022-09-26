import * as React from 'react';
import Image from "next/image";
import image from '/public/images/png/error.png';

export default function Eventcard({event}) {

    return (
        <div className="event_card">
            <Image src={image} className="cover_img"/>
            {/*<img src="img/speakers/ingrid-camino.jpg" className=""/>*/}
            <div>
                <div className="event_card_info">
                    <h3>Izuku Bellver</h3>
                    <p>Gobierno de Chile, Ministerio del Interior y Seguridad Pública Jefa de la División de Redes y
                        Seguridad Informática.</p>
                </div>
            </div>
        </div>
    );
};