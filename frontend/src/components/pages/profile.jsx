import {server, fetcherWithBearer} from '../../utils/server';
import useSwr from "swr";
import i18n from '../../i18n'
import landingImage from '../../assets/images/intro.jpg'
import Layout from '../layout'
import {useAuth} from '../../utils/useAuth';
import Rating from "@mui/material/Rating";
import {LoadingPage} from "../../utils/loadingPage";
import {StyledTableCell, StyledTableRow} from "../../utils/styleTable";
import TableHead from "@mui/material/TableHead";
import TableBody from "@mui/material/TableBody";
import Table from "@mui/material/Table";
import TableContainer from "@mui/material/TableContainer";
import Paper from "@mui/material/Paper";
import { LazyLoadImage } from 'react-lazy-load-image-component';

const Profile = (props) => {
    const {user} = useAuth();

    let accessToken;
    let isOrganizer;
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
        isOrganizer = localStorage.getItem("roles").includes("ROLE_CREATOR");
    }

    const {data: userStats, isLoading: statsLoading, error: errorStats} =
        useSwr(user ? [`${server}/api/users/${user.id}/stats`, accessToken] : null, fetcherWithBearer)

    const {data: organizerData, isLoading: organizerDataLoading, error: errorOrganizerData} =
        useSwr(user && isOrganizer ? [`${server}/api/organizers/${user.id}`, accessToken] : null, fetcherWithBearer)

    const {data: organizerStats, isLoading: organizerStatsLoading, error: errorOrganizerStats} =
        useSwr(user && isOrganizer ? [`${server}/api/organizers/${user.id}/stats`, accessToken] : null, fetcherWithBearer)

    if (!user || (user && (statsLoading || (isOrganizer && (organizerStatsLoading || organizerDataLoading))))) {
        return <LoadingPage/>
    }

    return (
        <Layout>
            <section className="profile-page">
                <div style={{position: "relative"}}>
                <LazyLoadImage
                            className="profile-intro-image"
                            component="img"
                            height="50vh"
                            width="100vw"
                            src={landingImage}
                            alt={i18n.t("landing")}
                        />
                    
                    <div className="profile-title">
                        <div className="center column">

                            <h3 className="profile-name">{user.username}</h3>
                            {!errorOrganizerData && organizerData &&
                                <span className="user-rating profile-rating">{organizerData.rating}<Rating value={organizerData.rating} readOnly size="small"/> ({organizerData.votes})</span>
                            }
                            {(!errorStats && userStats) &&
                            <TableContainer component={Paper} className="marg-top marg-bot">
                            <Table size="small">
                                <TableHead>
                                <StyledTableRow>
                                    <StyledTableCell>{i18n.t("stats.stats")}</StyledTableCell>
                                    <StyledTableCell></StyledTableCell>
                                </StyledTableRow>
                                </TableHead>
                                <TableBody>
                                <StyledTableRow>
                                    <StyledTableCell>{i18n.t("stats.ticketsBooked")}</StyledTableCell>
                                    <StyledTableCell className="right-text">{userStats.eventsAttended}</StyledTableCell>
                                </StyledTableRow>
                                <StyledTableRow>
                                    <StyledTableCell>{i18n.t("stats.favType")}</StyledTableCell>
                                    <StyledTableCell className="right-text">{userStats.bookingsMade}</StyledTableCell>
                                </StyledTableRow>
                                <StyledTableRow>
                                    <StyledTableCell>{i18n.t("stats.eventsAttended")}</StyledTableCell>
                                    <StyledTableCell className="right-text">{userStats.favLocation.name}</StyledTableCell>
                                </StyledTableRow>
                                <StyledTableRow>
                                    <StyledTableCell>{i18n.t("stats.favLocation")}</StyledTableCell>
                                    <StyledTableCell className="right-text">{userStats.favType.name}</StyledTableCell>
                                </StyledTableRow>
                                </TableBody>
                            </Table>
                            </TableContainer>
                            }
                            {(!errorOrganizerStats && organizerStats) &&
                                <TableContainer component={Paper} className="marg-top marg-bot">
                                    <Table size="small">
                                        <TableHead>
                                            <StyledTableRow>
                                                <StyledTableCell>{i18n.t("stats.stats")}</StyledTableCell>
                                                <StyledTableCell></StyledTableCell>
                                            </StyledTableRow>
                                        </TableHead>
                                        <TableBody>
                                            <StyledTableRow>
                                                <StyledTableCell>{i18n.t("stats.eventsCreated")}</StyledTableCell>
                                                <StyledTableCell className="right-text">{organizerStats.eventsCreated}</StyledTableCell>
                                            </StyledTableRow>
                                            <StyledTableRow>
                                                <StyledTableCell>{i18n.t("stats.bookingsGotten")}</StyledTableCell>
                                                <StyledTableCell className="right-text">{organizerStats.bookingsGotten}</StyledTableCell>
                                            </StyledTableRow>
                                            <StyledTableRow>
                                                <StyledTableCell>{i18n.t("stats.popularEvent")}</StyledTableCell>
                                                <StyledTableCell className="right-text">{organizerStats.popularEvent.name}</StyledTableCell>
                                            </StyledTableRow>
                                            <StyledTableRow>
                                                <StyledTableCell>{i18n.t("stats.attendance")}</StyledTableCell>
                                                <StyledTableCell className="right-text">{organizerStats.attendance}</StyledTableCell>
                                            </StyledTableRow>
                                            <StyledTableRow>
                                                <StyledTableCell>{i18n.t("stats.income")}</StyledTableCell>
                                                <StyledTableCell className="right-text">{organizerStats.income}</StyledTableCell>
                                            </StyledTableRow>
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                            }
                        </div>
                    </div>
                </div>
            </section>
        </Layout>
    );
}

export default Profile;
