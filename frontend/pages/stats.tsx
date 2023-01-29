import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import useSwr from 'swr';
import {server} from "../utils/server";
import {UserStats} from 'types';

const Stats = () => {
    const fetcher = (...args) => fetch(...args).then((res) => res.json())

    const router = useRouter();

    const [userId, setUserId] = useState("");
    const [isLoading, setIsLoading] = useState(true);

    const { data: data, error: error } = useSwr(userId ? `${server}/api/users/${userId}` : null, fetcher)

    if (error) return <p>No data</p>
    if (!data) return <p>Loading!</p>

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
        <>
            {!data &&
                <p>Loading!</p>
            }

            {data &&
                <section className="products-list">
                    {data.map((item: UserStats) => (
                        // <UserStats
                        //     key={item.id}
                        //     bookingMade={item.bookingsMade}
                        //     eventsAttended={item.eventsAttended}
                        //     favLocation={item.favLocation}
                        //     favType={item.favType}
                        // />
                    ))}
                </section>
            }
        </>
    )
}

export default Stats;