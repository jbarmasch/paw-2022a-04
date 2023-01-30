import {UserStats} from "../../types";
import Link from 'next/link';
import useSwr from "swr";
import {server} from "../../utils/server";
import {getPrice} from "../../utils/price";
import i18n from "../../utils/i18n"

// const fetcher = (...args) => fetch(...args).then((res) => res.json())

const UserStatsItem = ({bookingsMade, eventsAttended, favLocation, favType}: UserStats) => {
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
                            <th>{i18n.t("stats.stats")}</th>
                            <th/>
                        </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>{i18n.t("stats.eventsAttended")}</td>
                                <td>{eventsAttended}</td>
                            </tr>
                            <tr>
                                <td>{i18n.t("stats.ticketsBooked")}</td>
                                <td>{bookingsMade}</td>
                            </tr>
                            <tr>
                                <td>{i18n.t("stats.favType")}</td>
                                <td>{favType.name}</td>
                            </tr>
                            <tr>
                                <td>{i18n.t("stats.favLocation")}</td>
                                <td>{favLocation.name}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        // </Link>
    )
};


export default UserStatsItem