import {server, fetcher} from '../../utils/server';
import useSwr from "swr";
import i18n from '../../i18n'
import landingImage from '../../assets/images/intro.jpg'
import Layout from '../layout'
import {useAuth} from '../../utils/useAuth';
import Rating from "@mui/material/Rating";
import {LoadingPage} from "../../utils/loadingPage";

const Profile = (props) => {
    const {user} = useAuth();

    // const {data: userAux, isLoading, error} = useSwr(`${server}/api/users/${user.id}`, fetcher)

    const {data: userStats, isLoading: statsLoading, error: errorStats} = useSwr(user ? `${server}/api/users/${user.id}/stats` : null, fetcher)

    if (errorStats) return <p>No data</p>
    if (statsLoading || !user) return <LoadingPage/>

    return (
        <Layout>
            <section className="profile-page">
                <div style={{position: "relative"}}>
                    <img className="profile-intro-image" alt="Landing" src={landingImage}/>

                    <div className="profile-title">
                        <div className="center column">

                            <h3 className="profile-name">{user.username}</h3>
                            <h3 className="profile-mail">{user.mail}</h3>
                            <span className="user-rating profile-rating">{user.rating}<Rating value={user.rating} readOnly size="small"/> ({user.votes})</span>

                            <table className="ticket-table">
                                <thead>
                                <tr>
                                    <th>{i18n.t("stats.stats")}</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>{i18n.t("stats.ticketsBooked")}</td>
                                    <td className="right-text">{userStats.eventsAttended}</td>
                                </tr>
                                <tr>
                                    <td>{i18n.t("stats.favType")}</td>
                                    <td className="right-text">{userStats.bookingsMade}</td>
                                </tr>
                                <tr>
                                    <td>{i18n.t("stats.eventsAttended")}</td>
                                    <td className="right-text">{userStats.favLocation.name}</td>
                                </tr>
                                <tr>
                                    <td>{i18n.t("stats.favLocation")}</td>
                                    <td className="right-text">{userStats.favType.name}</td>
                                </tr>
                                </tbody>
                            </table>

                        </div>
                    </div>
                </div>
            </section>
        </Layout>
    );
}

export default Profile;
