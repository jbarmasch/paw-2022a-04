import {Fragment} from 'react'
import Document, {Html, DocumentContext, DocumentInitialProps, Head, Main, NextScript} from 'next/document'
import NoSSR from 'react-no-ssr';
import i18nextConfig from '../next-i18next.config'

// interface DocumentProps extends DocumentInitialProps {
//     isProduction: boolean
// }

const isSSREnabled = () => typeof window === 'undefined';

export default class CustomDocument extends Document<DocumentProps> {
    // static async getInitialProps(ctx: DocumentContext): Promise<DocumentProps> {
    //     const initialProps = await Document.getInitialProps(ctx)

    //     const isProduction = process.env.NODE_ENV === 'production'

    //     return {
    //         ...initialProps,
    //         isProduction,
    //     }
    // }

    render() {
        // const {isProduction} = this.props
        const currentLocale = this.props.__NEXT_DATA__.query.locale || i18nextConfig.i18n.defaultLocale;

        return (
            <Html lang={currentLocale}>
                <Head>
                    {/* {isProduction && (
                        <Fragment/>
                    )} */}
                </Head>
                <body>
                    <Main/>
                    <NextScript/>
                </body>
            </Html>
        )
    }
}
