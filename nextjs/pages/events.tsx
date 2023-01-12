import * as React from 'react';
import type {NextPage} from 'next';
import Head from 'next/head';
import party from '../public/images/png/error.png'
import Blockcard from '../components/Blockcard'
import Eventcard from '../components/Eventcard'
import { useState, useEffect } from 'react'
import useSWR from 'swr'
import { FormattedMessage, useIntl } from 'react-intl';
import { useRouter } from "next/router";

// @ts-ignore
const fetcher = (...args) => fetch(...args).then((res) => res.json())

// function arrayFetcher(...urlArr) {
//     const f = (u) => fetch(u).then((r) => r.json());
//     return Promise.all(urlArr.map(f));
// }

function Page ({ index }) {
    const { data, error } = useSWR(`${process.env.API_URL}/events?page=${index}`, fetcher);

    if (error) return <p>Loading...</p>
    if (!data) return <p>No data</p>

    return data.map((x, i) => <Eventcard key={i} event={x}/>)
}

const Events: NextPage = () => {

    let imageURLs = []
    // const { data : data, error : errorData } = useSWR(`${process.env.API_URL}/events`, fetcher)
    // const { data : images, error : imagesData } = useSWR(() => {
    //     if (data) {
    //         data.forEach(ev => {
    //             imageURLs.push(ev.image)
    //         })
    //         return imageURLs
    //     }
    //     return null
    // }, arrayFetcher)
    const [pageIndex, setPageIndex] = useState(1);

    // if (errorData) return <p>Loading...</p>
    // if (!data) return <p>No data</p>

    return (
        <div>
            <Head>
                <title>BotPass</title>
                <link rel="icon" href="/favicon.ico"/>
            </Head>
            <main>
                {/*<span id="page.home.title"/>*/}
                <FormattedMessage id="home.page"/>
                {/*{data && data.map((x, i) => <Eventcard key={i} event={x}/>)}*/}
                <div className="event-discovery flex w-full flex-1 flex-col items-center justify-center px-20 text-center">
                    <Page index={pageIndex}/>
                    <div style={{ display: 'none' }}><Page index={pageIndex + 1}/></div>
                </div>
                <button onClick={() => setPageIndex(pageIndex <= 1 ? pageIndex : pageIndex - 1)}><FormattedMessage id="home.previous"/></button>
                <button onClick={() => setPageIndex(pageIndex + 1)}><FormattedMessage id="home.next"/></button>
            </main>
        </div>
    );
};

export default Events;
