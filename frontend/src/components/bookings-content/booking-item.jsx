import Link from 'react-router-dom';
import useSwr from "swr";
import { server } from "../../utils/server";
import { getPrice } from "../../utils/price";
import { confirmAlert } from 'react-confirm-alert';
import 'react-confirm-alert/src/react-confirm-alert.css';
import BookingLoading from './item-loading'
import i18n from '../../i18n'
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import Table from '@mui/material/Table';
import TableRow from '@mui/material/TableRow';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableHead from '@mui/material/TableHead';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import { styled } from '@mui/material/styles';
import Dialog from '@mui/material/Dialog';
import Rating from '@mui/material/Rating';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {useState} from "react";

const fetcher = (...args) => fetch(...args).then((res) => res.json())

const BookingItem = ({ code, event, rating, ticketBookings, confirmed, mutate }) => {
    const [open, setOpen] = useState(false);

    const { data, error } = useSwr(`${event}`, fetcher);

    if (error) return <p>Loading...</p>
    if (!data) return <BookingLoading />

    let total = 0;

    const calcPrice = (price, qty) => {
        total += price * qty;
    }

    let accessToken;
    let refreshToken;
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
        refreshToken = localStorage.getItem("Refresh-Token");
    }

    const StyledTableRow = styled(TableRow)(({ theme }) => ({
        '&:nth-of-type(even)': {
            backgroundColor: theme.palette.action.hover,
        },
        '&:last-child td, &:last-child th': {
            border: 0,
        },
    }));

    const StyledTableCell = styled(TableCell)(({ theme }) => ({
        [`&.${tableCellClasses.head}`]: {
          // backgroundColor: "#343434",
          // backgroundColor: theme.palette.secondary.main,
          backgroundColor: "#cacaca",
          color: theme.palette.common.black,
          fontSize: "14px",
          fontWeight: "500"
        },
        [`&.${tableCellClasses.body}`]: {
          fontSize: 14,
        },
      }));

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
                        let c = await res;
                        mutate() 
        setOpen(!open)
    }

    const isBefore = (date) => {
        let evDate = new Date(date);
        console.log(evDate)
        return evDate < Date.now();
    }

    return (
        <Paper className="booking-item" elevation={2}>
            <div className="booking-content">
                <h3>{data.name}</h3>
                <div className="booking-rating">
                    <h4>{rating}</h4>
                </div>
            </div>
            <div className="booking-tickets">
                {ticketBookings &&
                    <>
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
                                {ticketBookings.map((item) => (
                                    <StyledTableRow key={item.ticket.ticketId} >
                                        <StyledTableCell >{item.ticket.ticketName}</StyledTableCell >
                                        <StyledTableCell align="right">{item.qty}</StyledTableCell >
                                        <StyledTableCell align="right">{getPrice(item.ticket.price, false)}</StyledTableCell >
                                        {calcPrice(item.ticket.price, item.qty)}
                                    </StyledTableRow >
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
                {isBefore(data.date) ? <div className="vertical">{i18n.t("bookings.rate")}:<Rating name="org-rating" defaultValue={rating} precision={0.5} /></div> : <Button variant="contained" color="error" onClick={submit}>{i18n.t("bookings.cancel")}</Button>}
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
                {/*<DialogContent>
                <DialogContentText id="alert-dialog-description">
                    {i18n.t("bookings.cancelMessage")}
                </DialogContentText>
                </DialogContent>*/}
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

{/* <div className="booking-item border">
        <div className="booking-content">
            <h3>{data.name}</h3>
            <div className="booking-rating">
                <h4>{rating}</h4>
            </div>
        </div>
        <div className="product__description booking-tickets">
            {ticketBookings &&
                <table className="ticket-table booking-table">
                    <thead>
                    <tr>
                        <th>Ticket</th>
                        <th>Qty</th>
                        <th>Price</th>
                    </tr>
                    </thead>
                    {ticketBookings.map((item) => (
                        <tbody key={item.ticket.ticketId}>
                        <tr>
                            <td>{item.ticket.ticketName}</td>
                            <td className="right-text">{item.qty}</td>
                            <td className="right-text">{getPrice(item.ticket.price)}</td>
                        </tr>
                        {calcPrice(item.ticket.price, item.qty)}
                        </tbody>
                    ))
                    }
                </table>
            }
            <div className="booking-total">
                <span>Total:</span>
                <span>${total}</span>
            </div>
        </div>
        <div className="booking-action">
            <button onClick={submit}>Confirm dialog</button>
        </div>
        </div> */}

export default BookingItem