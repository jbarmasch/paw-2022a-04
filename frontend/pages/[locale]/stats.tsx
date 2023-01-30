import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import useSwr from 'swr';
import {server} from "../../utils/server";
import {UserStats} from 'types';
import UserStatsItem from '../../components/stats-item';
import OrganizerStatsItem from '../../components/organizer-stats-item';
import Layout from '../../layouts/Main';
import Footer from '../../components/footer';
import { useTranslation } from 'next-i18next'
import { getStaticPaths, makeStaticProps } from '../../utils/get-static'
import Link from '../../components/Link'


const Stats = () => {
    const fetcher = (...args) => fetch(...args).then((res) => res.json())
    const { t } = useTranslation(['common'])

    const router = useRouter();

    const [userId, setUserId] = useState("");
    const [isLoading, setIsLoading] = useState(true);

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

    const { data: userStats, error: error } = useSwr(userId ? `${server}/api/users/${userId}/stats` : null, fetcher)
    const { data: organizerStats, error: errorStats } = useSwr(userId ? `${server}/api/users/${userId}/organizer-stats` : null, fetcher)

    if (error || errorStats) return <p>No data</p>
    if (!userStats || !organizerStats) return <p>Loading!</p>

    if (isLoading) {
        return <></>
    }

    return (
        <Layout t={t}>
                <section className="products-page">
                    <div className="container">
            {userStats && (
                        <UserStatsItem
                            bookingsMade={userStats.bookingsMade}
                            eventsAttended={userStats.eventsAttended}
                            favLocation={userStats.favLocation}
                            favType={userStats.favType}
                        />
            )}
            {organizerStats && (
                        <OrganizerStatsItem
                            attendance={organizerStats.attendance}
                            bookingsGotten={organizerStats.bookingsGotten}
                            eventsCreated={organizerStats.eventsCreated}
                            income={organizerStats.income}
                            popularEvent={organizerStats.popularEvent}
                            t={t}
                        />
            )}
                    </div>
                </section>
            <Footer/>
        </Layout>
    )
}

export default Stats;

const getStaticProps = makeStaticProps(['common'])
export { getStaticPaths, getStaticProps }
