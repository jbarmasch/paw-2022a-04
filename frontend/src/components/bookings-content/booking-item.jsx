import Link from 'react-router-dom';
import useSwr from "swr";
import {server} from "../../utils/server";
import {getPrice} from "../../utils/price";
import { confirmAlert } from 'react-confirm-alert';
import 'react-confirm-alert/src/react-confirm-alert.css';
import BookingLoading from './item-loading'
import i18n from '../../i18n'

const fetcher = (...args) => fetch(...args).then((res) => res.json())

const BookingItem = ({code, event, rating, ticketBookings, confirmed, mutate}) => {
    const {data, error} = useSwr(`${event}`, fetcher);

    if (error) return <p>Loading...</p>
    if (!data) return <BookingLoading/>

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

    let submit = () => {
        confirmAlert({
          title: 'Confirm to delete',
          buttons: [
            {
              label: 'Yes',
              onClick: async () => {
                // TODO: hacer refresh con variable
                let res = await fetch(`${server}/api/bookings/${code}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
                })
                let c = await res;
                mutate()
            }
            },
            {
              label: 'No',
            }
          ]
        });
    };

    return (
        <div className="booking-item border">
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
        </div>
    )
};


export default BookingItem