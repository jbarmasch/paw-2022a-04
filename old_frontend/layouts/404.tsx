import Head from 'next/head';
import Header from 'components/header';
import {useRouter} from 'next/router';

type LayoutType = {
    title?: string;
    children?: React.ReactNode;
}

export default ({children}: LayoutType) => {
    const router = useRouter();
    const pathname = router.pathname;

    return (
        <div className="app-main">
            <Head>
            </Head>

            {/* <Header isErrorPage/> */}

            <main className={(pathname !== '/' ? 'main-page' : '')}>
                {children}
            </main>
        </div>
    )
}