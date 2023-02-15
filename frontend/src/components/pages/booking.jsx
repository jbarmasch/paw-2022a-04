import landingImage from '../../assets/images/intro.jpg'
import {useHistory, useLocation} from 'react-router-dom'
import {server, fetcher} from "../../utils/server";
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
import Button from "@mui/material/Button";
import {getPrice} from "../../utils/price";
import {StyledTableRow, StyledTableCell} from "../../utils/styleTable";
import {ParseDateTime} from "../events-content/event-item";
import LocationOnRoundedIcon from '@mui/icons-material/LocationOnRounded';
import LocalActivityRoundedIcon from '@mui/icons-material/LocalActivityRounded';
import CalendarMonthRoundedIcon from '@mui/icons-material/CalendarMonthRounded';
import {useAuth} from "../../utils/useAuth";
import {useEffect, useState} from "react";
import {LoadingPage} from "../../utils/loadingPage";

const Booking = (props) => {
    let {user} = useAuth()

    const history = useHistory();
    const [bouncer, setBouncer] = useState(false)

    useEffect(() => {
        if (user === undefined) {
            return
        }
       if (user.role !== "ROLE_BOUNCER") {
           history.push("/404")
           return
       }
        setBouncer(true)
    }, [user]);

    let accessToken;
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
    }

    const {data: booking, mutate, isLoading, error} = useSwr(`${server}/api/bookings/${props.match.params.code}`, fetcher)
    const {data: event, isLoading: isLoadingEvent, error: errorEvent} = useSwr(booking ? booking.event : null, fetcher)

    if (!bouncer) {
        return <LoadingPage/>
    }

    if (error || errorEvent) return <p>No data</p>
    if (isLoading || isLoadingEvent) return <LoadingPage/>

    let total = 0;
    const calcPrice = (price, qty) => {
        total += price * qty;
        return;
    }

    const confirmBooking = async () => {
        const res = await fetch(`${server}/api/bookings/${props.match.params.code}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify({confirmed: true})
        })

        let json = await res;

        if (json.status != 202) {
            return;
        }

        mutate()
    }

    const invalidateBooking = async () => {
        const res = await fetch(`${server}/api/bookings/${props.match.params.code}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify({confirmed: false})
        })

        let json = await res;

        if (json.status != 202) {
            return;
        }

        mutate()
    }

    return (
        <Layout>
            <section className="thank-you-page">
                <div style={{position: "relative"}}>
                    <img className="thank-you-image booking-intro" alt="Landing" src={landingImage}/>
                </div>
                <div className="horizontal center flex-wrap">
                    <img className="booking-qr" src={`data:image/png;base64,${booking.image}`} alt="Event"/>
                <div>
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
                                    {booking.ticketBookings.map((item) => (
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
                        <div className="booking-total marg-top">
                            <span>Total:</span>
                            <span>${total}</span>
                        </div>

                        </div>
                    </Paper>
<div className="center marg-top">
    {booking.confirmed ?  <Button color="primary" onClick={invalidateBooking} variant="contained">{i18n.t("bookings.invalidate")}</Button> :
        <Button color="secondary" onClick={confirmBooking} variant="contained">{i18n.t("bookings.confirm")}</Button>}
</div>
                </div>
                </div>
            </section>
        </Layout>
    )
}

export default Booking;
