import {BookingType, TicketBookingType} from 'types';
import Link from 'next/link';
import useSwr from "swr";
import {server} from "../../utils/server";
import {getPrice} from "../../utils/price";

const fetcher = (...args) => fetch(...args).then((res) => res.json())

const BookingItem = ({code, event, rating, ticketBookings, confirmed}: BookingType) => {
    const {data, error} = useSwr(`${event}`, fetcher);

    if (error) return <p>Loading...</p>
    if (!data) return <p>No data</p>

    // console.log(data)

    return (
        <Link className="product-item border pointer" href={"/bookings/" + code}>
            <div>
                <div className="product__description">
                    <h3>{data.name}</h3>
                    <div className={"product__price " + (confirmed ? 'product__price--discount' : '')}>
                        <h4>{rating}</h4>
                    </div>

                    {ticketBookings &&
                        <table className="ticket-table">
                            <thead>
                            <tr>
                                <th>Ticket</th>
                                <th>Price</th>
                                <th>Qty</th>
                            </tr>
                            </thead>
                            {ticketBookings.map((item: TicketBookingType) => (
                                <tbody key={item.ticket.ticketId}>
                                <tr>
                                    <td>{item.ticket.ticketName}</td>
                                    <td className="right-text">{getPrice(item.ticket.price)}</td>
                                    <td className="right-text">{item.qty}</td>
                                </tr>
                                </tbody>
                            ))
                            }
                        </table>
                    }
                </div>
            </div>
        </Link>
    )
};


export default BookingItem