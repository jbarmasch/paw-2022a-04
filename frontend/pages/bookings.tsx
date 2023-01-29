import Layout from '../layouts/Main';
import Footer from '../components/footer';
import Breadcrumb from '../components/breadcrumb';
import BookingsContent from "../components/bookings-content";
import {useEffect, useState} from "react";
import {useRouter} from "next/router";

const Bookings = () => {
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
        <Layout>
            <Breadcrumb text={"All Bookings"}/>
            <section className="products-page">
                <div className="container">
                    <BookingsContent userId={userId}/>
                </div>
            </section>
            <Footer/>
        </Layout>
    )
}

export default Bookings