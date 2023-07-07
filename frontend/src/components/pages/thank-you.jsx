import landingImage from '../../assets/images/intro.jpg'
import {useHistory, useLocation} from 'react-router-dom'
import {fetcher} from "../../utils/server";
import Layout from "../layout"
import SimilarEvents from '../events-content/similar-events';
import RecommendedEvents from '../events-content/recommended-events';
import i18n from '../../i18n'
import useSwr from 'swr';
import Paper from "@mui/material/Paper";
import TableContainer from "@mui/material/TableContainer";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableBody from "@mui/material/TableBody";
import {getPrice} from "../../utils/price";
import {StyledTableRow, StyledTableCell} from "../../utils/styleTable";
import {ParseDateTime} from "../events-content/event-item";
import LocationOnRoundedIcon from '@mui/icons-material/LocationOnRounded';
import LocalActivityRoundedIcon from '@mui/icons-material/LocalActivityRounded';
import CalendarMonthRoundedIcon from '@mui/icons-material/CalendarMonthRounded';
import {LoadingPage} from "../../utils/loadingPage";

const ThankYou = () => {
    const {state} = useLocation()
    const history = useHistory()

    if (!state) {
        history.push("/")
    }

    let booking = state.booking
    let event = state.event

    const {data, isLoading, error} = useSwr(`${booking}`, fetcher)

    if (error) {
        history.push("/404");
        return;
    }

    if (isLoading) {
        return <LoadingPage/>
    }

    let total = 0;
    const calcPrice = (price, qty) => {
        total += price * qty;
    }

    return (
        <Layout>
            <section className="thank-you-page">
                <div style={{position: "relative"}}>
                    <img className="thank-you-image" alt="Landing" src={landingImage}/>
                    <h2 className="thank-you-content">{i18n.t("thankYou.phrase")}</h2>
                </div>
                <div className="horizontal center">
                    <img className="booking-qr" src={`data:image/png;base64,${data.image}`} alt="QR"/>
                    <Paper className={"thank-you-item"}>

                        <div className={"vertical thank-you-info"}>
                            <h3>{event.name}</h3>
                            <span><LocationOnRoundedIcon className="event-info-icons"/>{event.location.name}</span>
                            <span><LocalActivityRoundedIcon className="event-info-icons"/>{event.type.name}</span>
                            <span><CalendarMonthRoundedIcon className="event-info-icons"/>{ParseDateTime(event.date)}</span>
                        </div>

                        <div>
                        <TableContainer component={Paper}>
                            <Table className="booking-table" size="small" aria-label="simple table">
                                <TableHead>
                                    <TableRow>
                                        <StyledTableCell>{i18n.t("bookings.ticket")}</StyledTableCell>
                                        <StyledTableCell align="right">{i18n.t("bookings.qty")}</StyledTableCell>
                                        <StyledTableCell align="right">{i18n.t("bookings.price")}</StyledTableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {data.ticketBookings.map((item) => (
                                        <StyledTableRow key={item.ticket.ticketId}>
                                            <StyledTableCell>{item.ticket.ticketName}</StyledTableCell>
                                            <StyledTableCell align="right">{item.qty}</StyledTableCell>
                                            <StyledTableCell
                                                align="right">{getPrice(item.ticket.price * item.qty, false)}</StyledTableCell>
                                            {calcPrice(item.ticket.price, item.qty)}
                                        </StyledTableRow>
                                    ))
                                    }
                                </TableBody>
                            </Table>
                        </TableContainer>
                        <div className="booking-total">
                            <span>Total:</span>
                            <span>${total}</span>
                        </div>
                        </div>
                    </Paper>
                </div>
                <SimilarEvents id={event.id}/>
                <RecommendedEvents id={event.id}/>
            </section>
        </Layout>
    )
}

export default ThankYou;
