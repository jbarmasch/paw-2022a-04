import {useState} from 'react';
import Footer from '../../../components/footer';
import Layout from '../../../layouts/Main';
import Breadcrumb from '../../../components/breadcrumb';
import ProductsFeatured from '../../../components/products-featured';
import Description from '../../../components/product-single/description';
import {server} from '../../../utils/server';

import useSwr from "swr";
import Image from "next/image";
import ProductItemLoading from "../../../components/product-item/loading";
import Content from "../../../components/product-single/content";
import {useRouter} from "next/router";
import {BookingType, TicketBookingType, TicketType} from "../../../types";
import BookingItem from "../../../components/booking-item";

import { useTranslation } from 'next-i18next'
import { getStaticPaths, makeStaticProps } from '../../../utils/get-static'
import Link from '../../../components/Link'

const Booking = () => {
    const { t } = useTranslation(['common'])

    const [showBlock, setShowBlock] = useState('description');

    const fetcher = (url: string) => fetch(url).then((res) => res.json());

    const router = useRouter();

    const { data : booking, error : errorData } = useSwr(router.query.id ? `${server}/api/bookings/${router.query.id}` : null, fetcher)
    // const { data : tickets, error : errorTickets } = useSwr(event ? `${event.tickets}` : null, fetcher)
    // const { data : aux, error : error } = useSwr(event ? `${event.image}` : null, fetcher)

    if (errorData) return <p>No data</p>
    if (!booking) return <ProductItemLoading/>

    console.log(booking)

    // @ts-ignore
    const total = booking.ticketBookings.reduce((accum: number, item: TicketBookingType) => accum + item.qty * item.ticket.price, 0)

    return (
        <Layout t={t}>
            <Breadcrumb text={t("bookings.booking")}/>

            <section className="product-single">
                <div className="container">
                    <div className="product-single__content">
                        <img className={"product-gallery__image"} src={`data:image/png;base64,${booking.image}`} alt="Booking image"/>
                        {/*<Image src={`data:image/png;base64,${booking.image}`} className="product-gallery__image" layout="raw" width={"300px"} height={"300px"}/>*/}
                        {/*<Content product={event}/>*/}
                    </div>
                    <div>
                        <table className="ticket-table">
                            <thead>
                            <tr>
                                <th>Ticket name</th>
                                <th>Price</th>
                                <th>Quantity</th>
                            </tr>
                            </thead>
                            {booking.ticketBookings && booking.ticketBookings.map((item: TicketBookingType) => (
                                <tbody key={item.ticket.ticketName}>
                                <tr>
                                    <td>{item.ticket.ticketName}</td>
                                    <td>${item.ticket.price}</td>
                                    <td>{item.qty}</td>
                                </tr>
                                </tbody>
                            ))}
                        </table>
                        Total: ${total}
                    </div>

                    <div className="product-single__info">
                        <div className="product-single__info-btns">
                            <button type="button" onClick={() => setShowBlock('description')}
                                    className={`btn btn--rounded ${showBlock === 'description' ? 'btn--active' : ''}`}>Description
                            </button>
                            <button type="button" onClick={() => setShowBlock('reviews')}
                                    className={`btn btn--rounded ${showBlock === 'reviews' ? 'btn--active' : ''}`}>Reviews
                                (2)
                            </button>
                        </div>

                        <Description show={showBlock === 'description'}/>
                        {/*<Reviews product={product} show={showBlock === 'reviews'}/>*/}
                    </div>
                </div>
            </section>

            <div className="product-single-page">
                <ProductsFeatured/>
            </div>
            <Footer/>
        </Layout>
    );
}

export default Booking

const getStaticProps = makeStaticProps(['common'])
export { getStaticPaths, getStaticProps }
