import {Fragment} from 'react'
import Document, {Html, DocumentContext, DocumentInitialProps, Head, Main, NextScript} from 'next/document'
import NoSSR from 'react-no-ssr';

interface DocumentProps extends DocumentInitialProps {
    isProduction: boolean
}

const isSSREnabled = () => typeof window === 'undefined';

export default class CustomDocument extends Document<DocumentProps> {
    static async getInitialProps(ctx: DocumentContext): Promise<DocumentProps> {
        const initialProps = await Document.getInitialProps(ctx)

        const isProduction = process.env.NODE_ENV === 'production'

        return {
            ...initialProps,
            isProduction,
        }
    }

    render() {
        const {isProduction} = this.props

        return (
            // <NoSSR>
            <Html lang="en">
                <Head>
                    {isProduction && (
                        <Fragment/>
                    )}
                </Head>
                <body>
                    <Main/>
                    {/*{!isSSREnabled() && <Main/>}*/}
                    {/*{!isSSREnabled() && <NextScript/>}*/}
                    <NextScript/>
                </body>
            </Html>
            // </NoSSR>
        )
    }
}