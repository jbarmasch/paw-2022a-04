import {BookingType, TicketBookingType} from 'types';
import Link from 'next/link';
import useSwr from "swr";
import {server} from "../../utils/server";
import {getPrice} from "../../utils/price";
import { confirmAlert } from 'react-confirm-alert';
import 'react-confirm-alert/src/react-confirm-alert.css';

const fetcher = (...args) => fetch(...args).then((res) => res.json())

const BookingItem = ({code, event, rating, ticketBookings, confirmed, t, mutate}: BookingType) => {
    const {data, error} = useSwr(`${event}`, fetcher);

    if (error) return <p>Loading...</p>
    if (!data) return <p>No data</p>

    // console.log(data)

    let total = 0;

    const calcPrice = (price, qty) => {
        total += price * qty;
    }

    let accessToken: string | null
    let refreshToken: string | null
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
        refreshToken = localStorage.getItem("Refresh-Token");
    }

    // const options = {
    //     title: 'Title',
    //     message: 'Message',
    //     buttons: [
    //       {
    //         label: 'Yes',
    //         onClick: () => alert('Click Yes')
    //       },
    //       {
    //         label: 'No',
    //         onClick: () => alert('Click No')
    //       }
    //     ],
    //     closeOnEscape: true,
    //     closeOnClickOutside: true,
    //     keyCodeForClose: [8, 32],
    //     willUnmount: () => {},
    //     afterClose: () => {},
    //     onClickOutside: () => {},
    //     onKeypress: () => {},
    //     onKeypressEscape: () => {},
    //     overlayClassName: ""
    //   };

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
        // confirmAlert(options);
        // confirmAlert({
        //     customUI: ({ onClose }) => {
        //       return (
        //         <div className='custom-ui'>
        //           <h1>Are you sure?</h1>
        //           <p>You want to delete this file?</p>
        //           <button onClick={onClose}>No</button>
        //           <button
        //             onClick={() => {
        //               handleClickDelete();
        //               onClose();
        //             }}
        //           >
        //             Yes, Delete it!
        //           </button>
        //         </div>
        //       );
        //     }
        //   });
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
                        {ticketBookings.map((item: TicketBookingType) => (
                            <tbody key={item.ticket.ticketId}>
                            <tr>
                                <td>{item.ticket.ticketName}</td>
                                <td className="right-text">{item.qty}</td>
                                <td className="right-text">{getPrice(item.ticket.price, t("event.free"), "")}</td>
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
                {/* <button onClick={openModal} >
                    Cancelar
                </button> */}
                <button onClick={submit}>Confirm dialog</button>
            </div>

        {/* <Confirm
            isOpen={open}
            title="do stuff"
            content={ct}
            onConfirm={handleConfirm}
            onClose={handleDialogClose}
            confirm="Yep"
            cancel="Nope"
        /> */}
        </div>
    )
};


export default BookingItem