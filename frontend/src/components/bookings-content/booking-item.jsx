import useSwr from "swr";
import {server, fetcher} from "../../utils/server";
import {getPrice} from "../../utils/price";
import BookingLoading from './item-loading'
import {useHistory} from "react-router-dom";
import i18n from '../../i18n'
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import Table from '@mui/material/Table';
import TableHead from '@mui/material/TableHead';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import {StyledTableRow, StyledTableCell} from "../../utils/styleTable";
import Dialog from '@mui/material/Dialog';
import Rating from '@mui/material/Rating';
import DialogActions from '@mui/material/DialogActions';
import DialogTitle from '@mui/material/DialogTitle';
import {useState} from "react";
import {ParseDateTime} from "../events-content/event-item";
import LocationOnRoundedIcon from "@mui/icons-material/LocationOnRounded";
import CalendarMonthRoundedIcon from "@mui/icons-material/CalendarMonthRounded";

const BookingItem = ({image, code, event, rating, ticketBookings, mutate}) => {
    const [open, setOpen] = useState(false);
    const {data, isLoading, error} = useSwr(`${event}`, fetcher);
    const [openQR, setOpenQR] = useState(false);
    const [newRating, setNewRating] = useState(rating)

    const history = useHistory();

    if (error) {
        history.push("/404")
        return <></>
    }

    if (isLoading) return <BookingLoading/>

    let total = 0;

    const calcPrice = (price, qty) => {
        total += price * qty;
    }

    let accessToken;
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
    }

    let submit = () => {
        setOpen(!open)
    };

    const askFor = async () => {
        let res = await fetch(`${server}/api/bookings/${code}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${accessToken}`
            },
        })
        await res;
        mutate()
        setOpen(!open)
    }

    const isBefore = (date) => {
        let evDate = new Date(date);
        return evDate < Date.now();
    }

    const handleClickOpenQR = () => {
        setOpenQR(true);
    };

    const handleCloseQR = () => {
        setOpenQR(false);
    };

    const rate = async (e, v) => {
        setNewRating(v)
        const res = await fetch(`${server}/api/events/${data.id}/rating`, {
            method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify({rating: v})
            })

            let json = await res;

            if (json.status !== 202) {
                return;
            }

            mutate()
    }

    return (
        <Paper className="booking-item" elevation={2}>
            <div>
            <img onClick={handleClickOpenQR} className="booking-qr-small pointer" src={`data:image/png;base64,${image}`} alt="QR"/>
            <Dialog
                open={openQR}
                onClose={handleCloseQR}
                aria-labelledby="responsive-dialog-title"
            >
                <img className="booking-qr" src={`data:image/png;base64,${image}`} alt="QR"/>
            </Dialog>
            </div>
            <div className="booking-content vertical">
                <h3>{data.name}</h3>
                <span><LocationOnRoundedIcon className="event-info-icons"/>{data.location.name}</span>
                <span><CalendarMonthRoundedIcon className="event-info-icons"/>{ParseDateTime(data.date)}</span>
            </div>
            <div className="booking-tickets">
                {ticketBookings &&
                    <>
                        <TableContainer component={Paper}>
                            <Table className="booking-table" size="small" aria-label="simple table">
                                <TableHead>
                                    <StyledTableRow>
                                        <StyledTableCell>{i18n.t("bookings.ticket")}</StyledTableCell>
                                        <StyledTableCell align="right">{i18n.t("bookings.qty")}</StyledTableCell>
                                        <StyledTableCell align="right">{i18n.t("bookings.price")}</StyledTableCell>
                                    </StyledTableRow>
                                </TableHead>
                                <TableBody>
                                    {ticketBookings.map((item) => (
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
                    </>
                }
            </div>
            <div className="booking-action">
                {isBefore(data.date) ?
                    <div className="vertical">{i18n.t("bookings.rate")}:
                        <Rating onChange={rate}
                        name="org-rating" value={newRating ? newRating : 0}/></div>
                    :
                    <Button variant="contained" color="error" onClick={submit}>{i18n.t("bookings.cancel")}</Button>}
            </div>
            <Dialog
                open={open}
                onClose={submit}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">
                    {i18n.t("bookings.cancelMessage")}
                </DialogTitle>
                <DialogActions>
                    <Button onClick={submit}>{i18n.t("bookings.cancel")}</Button>
                    <Button onClick={askFor} autoFocus>
                        {i18n.t("bookings.accept")}
                    </Button>
                </DialogActions>
            </Dialog>
        </Paper>
    )
};

export default BookingItem