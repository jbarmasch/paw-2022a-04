import * as React from 'react';
import type {NextPage, GetStaticProps, GetStaticPaths} from 'next';
import Head from 'next/head';
import { ParsedUrlQuery } from 'querystring';
import { useState, useEffect } from 'react'
import useSWR from 'swr'
import { useRouter } from "next/router";

interface Event {
    name: string,
    attendance: number,
    date: string,
    description: string,
    image: string,
    location: string,
    maxCapacity: number,
    minPrice: number,
    organizer: string,
    self: string,
    tags: object,
    tickets: string,
    type: string
}

interface EventProps {
    data: Event
    dataAux: string
}

// @ts-ignore
const fetcher = (...args) => fetch(...args).then((res) => res.json())

import Image from 'next/image'
const Event: NextPage<EventProps> = (props) => {
    const router = useRouter();
    // let query
    //
    // useEffect(() => {
    //     if (!router.isReady)
    //         return;
    //     query = router.query;
    //     console.log(query)
    // }, [router.isReady, router.query]);
    //
    // const { data : data, error : errorData } = useSWR(query ? `http://localhost:8080/events/${router.query.id}` : null, fetcher)

    const { data : data, error : errorData } = useSWR(`http://localhost:8080/events/${router.query.id}`, fetcher)
    const { data : image, error : errorImage } = useSWR(data ? `${data.image}` : null, fetcher)

    if (errorData || errorImage) return <p>Loading...</p>
    if (!data || !image) return <p>No data</p>

    console.log(router.query)

    return (
        <div>
            <Head>
                <title>BotPass</title>
                <link rel="icon" href="/favicon.ico"/>
            </Head>
            <main className="flex w-full flex-1 flex-col items-center justify-center px-20 text-center">
                {data.location}
                <div>
                    <Image src={`data:image/png;base64,${image.image}`} alt="Picture of the event" width={300} height={300} className={"cover-img"}/>
                </div>
            </main>
        </div>
    );
};

export default Event;
