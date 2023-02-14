import Skeleton from '@mui/material/Skeleton';
import Box from '@mui/material/Box';


const BookingItemLoading = () => (
    <>
        <Box className="booking-item">
            <div className="booking-content">
                <Skeleton width="100px" height="40px"/>
            </div>
            <div className="booking-tickets">
                <Skeleton variant="rectangular" width="100%" height="100%"/>
            </div>
            <div className="booking-action">
            <Skeleton variant="rectangular" width="80%" height="40%"/>
            </div>
        </Box>
    </>
);
 
{/*

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
                <DialogActions>
                <Button onClick={submit}>{i18n.t("bookings.cancel")}</Button>
                <Button onClick={askFor} autoFocus>
                    {i18n.t("bookings.accept")}
                </Button>
                </DialogActions>
            </Dialog>
        </Paper>

*/}

export default BookingItemLoading