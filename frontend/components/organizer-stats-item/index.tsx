import {OrganizerStats} from "../../types";
import useSwr from "swr";
import {server} from "../../utils/server";
import {getPrice} from "../../utils/price";
import Link from '../../components/Link'

// const fetcher = (...args) => fetch(...args).then((res) => res.json())

const OrganizerStatsItem = ({attendance, bookingsGotten, eventsCreated, income, popularEvent}: OrganizerStats, t) => {
    // const {data, error} = useSwr(`${event}`, fetcher);

    // if (error) return <p>Loading...</p>
    // if (!data) return <p>No data</p>

    return (
        // <Link className="product-item border pointer" href={"/bookings/" + code}>
            <div>
                <div className="product__description">

                    <table className="ticket-table">
                        <thead>
                        <tr>
                            <th>{t("stats.stats")}</th>
                            <th/>
                        </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>{t("stats.eventsAttended")}</td>
                                <td>{attendance}</td>
                            </tr>
                            <tr>
                                <td>{t("stats.ticketsBooked")}</td>
                                <td>{bookingsGotten}</td>
                            </tr>
                            <tr>
                                <td>{t("stats.favType")}</td>
                                <td>{eventsCreated}</td>
                            </tr>
                            <tr>
                                <td>{t("stats.favType")}</td>
                                <td>{income}</td>
                            </tr>
                            <tr>
                                <td>{t("stats.favLocation")}</td>
                                <td>{popularEvent.name}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        // </Link>
    )
};


export default OrganizerStatsItem
