import Layout from '../layout';
import BookingsContent from "../bookings-content/bookings-content";
import {useEffect, useState} from "react";
import {server} from '../../utils/server';
import i18n from '../../i18n'
import {Link, useHistory, useLocation} from 'react-router-dom'
import { useFindPath } from '../header';

const Bookings = () => {
    let path = useFindPath();
    const history = useHistory();
    const prevLocation = useLocation();

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
                history.push(`/login?redirectTo=${prevLocation.pathname}`);
            }
        }

        if (accessToken && refreshToken) {
            fetchData(accessToken, refreshToken, path)
            const userId = localStorage.getItem("User-ID");
            setUserId(userId)
            setIsLoading(false)
        } else {
            const pathname = path
            if (path !== "/login") {
                history.push(`/login?redirectTo=${prevLocation.pathname}`);
            }
        }
    }, [accessToken, refreshToken, prevLocation]);

    if (isLoading) {
        return <></>
    }

    return (
        <Layout>
            <section className="products-page">
                <div className="container">
                    <BookingsContent userId={userId}/>
                </div>
            </section>
        </Layout>
    )
}

export default Bookings
