import * as React from 'react';
import Head from 'next/head';
import {AppProps} from 'next/app';
import {ThemeProvider} from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import {CacheProvider, EmotionCache} from '@emotion/react';
import theme from '../src/theme';
import createEmotionCache from '../src/createEmotionCache';
import Layout from '../components/Layout';
import '../style/globals.sass';
import { useRouter } from "next/router";
import { IntlProvider } from "react-intl";

import es from "../lang/es.json";
import en from "../lang/en.json";

const messages = { en, es };

// Client-side cache, shared for the whole session of the user in the browser.
const clientSideEmotionCache = createEmotionCache();

interface MyAppProps extends AppProps {
    emotionCache?: EmotionCache;
}

export default function MyApp(props: MyAppProps) {
    const {Component, emotionCache = clientSideEmotionCache, pageProps} = props;
    const {locale} = useRouter();

    return (
        <Layout>
            <CacheProvider value={emotionCache}>
                <Head>
                    <meta name="viewport" content="initial-scale=1, width=device-width"/>
                </Head>
                <ThemeProvider theme={theme}>
                    <CssBaseline/>
                    <IntlProvider locale={locale} messages={messages[locale]}>
                        <Component {...pageProps}/>
                    </IntlProvider>
                </ThemeProvider>
            </CacheProvider>
        </Layout>
    );
}
