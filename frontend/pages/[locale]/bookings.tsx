import Layout from '../../layouts/Main';
import Footer from '../../components/footer';
import Breadcrumb from '../../components/breadcrumb';
import BookingsContent from "../../components/bookings-content";
import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import {server} from '../../utils/server';
import { useTranslation } from 'next-i18next'
import { getStaticPaths, makeStaticProps } from '../../utils/get-static'
import Link from '../../components/Link'

const Bookings = () => {
    const { t } = useTranslation(['common'])

    const router = useRouter();

    const [isLoading, setIsLoading] = useState(true);
    const [userId, setUserId] = useState(true);

    let accessToken: string | null
    let refreshToken: string | null
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
        refreshToken = localStorage.getItem("Refresh-Token");
    }

    useEffect(() => {
        const fetchData = async (accessToken, refreshToken, pathname) => {
            let res = fetch(`${server}/api/users/test`, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
            })

            let aux = await res;
            if (aux.status == 200) {
                return;
            }

            res = fetch(`${server}/api/users/test`, {
                headers: {
                    'Authorization': `Bearer ${refreshToken}`
                },
            })

            aux = await res;
            if (aux.status == 200) {
                localStorage.setItem("Access-Token", aux.headers.get("Access-Token"))
                return;
            }

            if (router.pathname !== "/login") {
                router.push({
                    pathname: '/login',
                    query: {pathname: pathname},
                }, '/login');
            }
        }

        if (accessToken && refreshToken) {
            fetchData(accessToken, refreshToken, router.pathname)
            const userId = localStorage.getItem("User-ID");
            setUserId(userId)
            setIsLoading(false)
        } else {
            const pathname = router.pathname
            if (router.pathname !== "/login") {
                router.push({
                    pathname: '/login',
                    query: {pathname: pathname},
                }, '/login');
            }
        }
    }, [accessToken, refreshToken]);

    if (isLoading) {
        return <></>
    }

    return (
        <Layout t={t}>
            <Breadcrumb text={t("bookings.all")}/>
            <section className="products-page">
                <div className="container">
                    <BookingsContent userId={userId} t={t}/>
                </div>
            </section>
            <Footer/>
        </Layout>
    )
}

export default Bookings

const getStaticProps = makeStaticProps(['common'])
export { getStaticPaths, getStaticProps }
