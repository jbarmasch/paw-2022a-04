import * as React from 'react';
import type {NextPage} from 'next';
import Head from 'next/head';
import party from '../public/images/png/error.png'
import Blockcard from '../components/Blockcard'
import Eventcard from '../components/Eventcard'

const Home: NextPage = () => {
    return (
        <div>
            <Head>
                <title>BotPass</title>
                <link rel="icon" href="/favicon.ico"/>
            </Head>
            <main className="flex w-full flex-1 flex-col items-center justify-center px-20 text-center">
                <div className="parallax">
                    BotPass
                </div>

                <div className="container flex justify-center"></div>

                Upcoming events

                <div className="event-discovery"></div>

            </main>
        </div>
    );
};

export default Home;
