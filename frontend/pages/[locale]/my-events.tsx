import Layout from '../../layouts/Main';
import Footer from '../../components/footer';
import Breadcrumb from '../../components/breadcrumb';
import MyEventsContent from "../../components/my-events-content";
import ProductsFilter from "../components/products-filter";
import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import { useTranslation } from 'next-i18next'
import { getStaticPaths, makeStaticProps } from '../../utils/get-static'
import Link from '../../components/Link'


const MyEvents = () => {
    const { t } = useTranslation(['common'])
    const router = useRouter();

    const [isLoading, setIsLoading] = useState(true);
    const [userId, setUserId] = useState(true);


    useEffect(() => {
        const userId = localStorage.getItem("User-ID");
        const pathname = router.pathname
        console.log(pathname)
        if (router.pathname !== "/login" && (!userId || userId === "undefined")) {
            router.push({
                pathname: '/login',
                query: { pathname: pathname },
            }, '/login');
        } else {
            setUserId(userId)
            setIsLoading(false)
        }
    }, []);

    if (isLoading) {
        return <></>
    }

    return (
        <Layout t={t}>
            <Breadcrumb text={t("nav.myEvents")}/>
            <section className="products-page">
                <div className="container">
                    {/*<ProductsFilter/>*/}
                    <MyEventsContent userId={userId}/>
                </div>
            </section>
            <Footer/>
        </Layout>
    )
}

export default MyEvents;

const getStaticProps = makeStaticProps(['common'])
export { getStaticPaths, getStaticProps }
