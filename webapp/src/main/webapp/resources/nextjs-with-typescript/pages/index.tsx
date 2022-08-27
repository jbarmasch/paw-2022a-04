import * as React from 'react';
import type { NextPage } from 'next';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Link from 'next/link';
import Head from 'next/head';
import Image from 'next/image';
import party from '../public/images/png/error.png'
import Blockcard from '../components/Blockcard'
import Eventcard from '../components/Eventcard'

const Home: NextPage = () => {
    return (
        <div>
            <Head>
                <title>BotPass</title>
                <link rel="icon" href="/favicon.ico" />
            </Head>
            <main className="flex w-full flex-1 flex-col items-center justify-center px-20 text-center">
                <div className="parallax">
                    BotPass
                </div>

                <div className="container flex justify-center">
                    <Blockcard image={party}/>
                    <Blockcard image={party}/>
                    <Blockcard image={party}/>
                </div>

                Upcoming events
                <div className="container flex justify-center">
                    <Eventcard event={null}/>
                    <Eventcard event={null}/>
                    <Eventcard event={null}/>
                </div>

            </main>
        </div>
    );
};

export default Home;
