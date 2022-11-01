import * as React from 'react';
import Image from "next/image";
import useSWR from 'swr'
import Link from 'next/link'
import { useRouter } from 'next/router'

// @ts-ignore
const fetcher = (...args) => fetch(...args).then((res) => res.json())

export default function Eventcard({event}) {
    const router = useRouter()
    const { data : image, error : errorData } = useSWR(event.image, fetcher)

    const handleClick = (e, href) => {
        e.preventDefault()
        router.push(href)
    }

    if (errorData) return <p>Loading...</p>
    if (!image) return <p>No data</p>

    return (
        <div className="event-card" onClick={e => handleClick(e, `/events/${event.id}`)}>
            <Image src={`data:image/png;base64,${image.image}`} className="cover-img" width={350} height={350} objectFit={"cover"}/>
            <div>
                <div className="event-card-info">
                    <h2>{event.name}</h2>
                    <p>{event.location}</p>
                </div>
            </div>
        </div>
    );
};