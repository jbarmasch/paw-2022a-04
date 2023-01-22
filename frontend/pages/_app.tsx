import React, {Fragment} from 'react';
import Router, {useRouter} from 'next/router';
// import {wrapper} from '../store';

import type {AppProps} from 'next/app';

import 'swiper/swiper.scss';
import 'rc-slider/assets/index.css';
import 'react-rater/lib/react-rater.css';
import '../assets/css/styles.scss';
// import NoSSR from 'react-no-ssr';

const isProduction = process.env.NODE_ENV === 'production';

if (isProduction) {

}

import { IntlProvider } from "react-intl";

import es from "../lang/es.json";
import en from "../lang/en.json";

const messages = { en, es };

import { Quicksand } from '@next/font/google'

export const quicksand = Quicksand({
    subsets: ['latin'],
    variable: '--main-font'
})

// import { headers } from 'next/headers';

const MyApp = ({Component, pageProps}: AppProps) => {
    const {locale} = useRouter();
    // const headersList = headers();
    // const lang = headersList.get('Accept-Language');
    const [shortLocale] = locale ? locale.split("-") : ["en"];

    return (
        <main className={quicksand.className}>
            <Fragment>
                <IntlProvider locale={shortLocale} messages={messages[shortLocale]}>
                    <Component {...pageProps}/>
                </IntlProvider>
            </Fragment>
        </main>
    );
}

export default MyApp;