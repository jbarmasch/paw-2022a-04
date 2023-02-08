import Layout from '../layout';
import MyEventsContent from "../my-events-content/my-events-content";
import ContentLoading from "../my-events-content/content-loading";
// import ProductsFilter from "../components/products-filter";
import {useEffect, useState} from "react";
import i18n from '../../i18n'
import {Link, useHistory} from 'react-router-dom'
import useFindPath from '../header'
import {server} from '../../utils/server'


const MyEvents = () => {
    const history = useHistory();
    let path = useFindPath();

    const [isLoading, setIsLoading] = useState(true);
    const [userId, setUserId] = useState(true);

    let accessToken;
    let refreshToken;
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

            if (path !== "/login") {
                history.push({
                    pathname: '/login',
                    query: {pathname: pathname},
                }, '/login');
            }
        }

        if (accessToken && refreshToken) {
            fetchData(accessToken, refreshToken, path)
            const userId = localStorage.getItem("User-ID");
            console.log(userId)
            setUserId(userId)
            setIsLoading(false)
        } else {
            const pathname = path
            if (path !== "/login") {
                history.push({
                    pathname: '/login',
                    query: {pathname: pathname},
                }, '/login');
            }
        }
    }, [accessToken, refreshToken]);
    
    if (isLoading) {
        return <ContentLoading/>
    }

    return (
        <Layout>
            <section className="products-page">
                <div className="container">
                    {/*<ProductsFilter/>*/}
                    <MyEventsContent userId={userId}/>
                </div>
            </section>
        </Layout>
    )
}

export default MyEvents;
