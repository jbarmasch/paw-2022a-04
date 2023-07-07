import {server, fetcher} from '../../utils/server';
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

    const {data: userStats, isLoading: statsLoading, error: errorStats} = useSwr(user ? `${server}/api/users/${user.id}/stats` : null, fetcher)

    if (statsLoading || !user) {
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
                            <h3 className="profile-mail">{user.mail}</h3>
                            <span className="user-rating profile-rating">{user.rating}<Rating value={user.rating} readOnly size="small"/> ({user.votes})</span>
                            {(userStats && !errorStats) &&
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

                        </div>
                    </div>
                </div>
            </section>
        </Layout>
    );
}

export default Profile;
