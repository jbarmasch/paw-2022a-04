import {useEffect, useMemo, useState} from 'react';
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
import {BookingType, NewTicketType, TicketType} from "../../../types";
import BookingItem from "../../../components/booking-item";
import TableRows from "../../../components/table-rows"
import * as React from "react";
import {useForm, useFieldArray, Controller} from "react-hook-form";
import {toInteger} from "lodash";
import {retry} from "@reduxjs/toolkit/query";
import { postData } from 'utils/services';
import {quicksand} from '../../_app';
import { useTranslation } from 'next-i18next'
import { getStaticPaths, makeStaticProps } from '../../../utils/get-static'
import Link from '../../../components/Link'

const isEqualsJson = (oldTicket, newTicket) => {
    let oldKeys = Object.keys(oldTicket);
    let newKeys = Object.keys(newTicket);

    return oldKeys.length === newKeys.length && Object.keys(oldTicket).every(key => {
        console.log(key)
        // if (key == "starting" && (obj1[key] == null || obj2[key] == null)) {
        //     return true;
        // }
        // if (key == "until" && (obj1[key] == null || obj2[key] == null)) {
        //     return true;
        // }
        return oldTicket[key] == newTicket[key]
    });
}

const getMinValue = (ticket) => {
    return ticket.hasOwnProperty("booked") && ticket.booked > 0 ? ticket.booked : 1
}

const Product = () => {
    const { t } = useTranslation(['common'])

    const [showBlock, setShowBlock] = useState('description');

    const fetcher = (url: string) => fetch(url).then((res) => res.json());

    const router = useRouter();

    const [rowsData, setRowsData] = useState([]);
    const [index, setIndex] = useState(0);

    // const { register, handleSubmit, control, watch, formState: { errors } } = useForm();

    const { data : event, error : errorData } = useSwr(router.query.id ? `${server}/api/events/${router.query.id}` : null, fetcher)
    const { data : tickets, error : errorTickets } = useSwr(event ? `${event.tickets}` : null, fetcher)
    const { data : aux, error : error } = useSwr(event ? `${event.image}` : null, fetcher)

    const { register, control, handleSubmit, reset, watch, setValue, getValues, formState: {errors} } = useForm({
        defaultValues: {
            tickets: [{
                // id: "",
                ticketName: "",
                price: "",
                qty: "",
                // booked: "",
                maxPerUser: "",
                starting: "",
                until: ""
            }]
        }
    });

    const { fields, append, prepend, remove, swap, move, insert } = useFieldArray(
        {
            control,
            name: "tickets"
        }
    );

    let accessToken: string | null
    let refreshToken: string | null
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
        refreshToken = localStorage.getItem("Refresh-Token");
    }

    useEffect(() => {
        setValue("tickets", tickets);
    }, [tickets]);

    useEffect(() => {
        const fetchData = async (accessToken, refreshToken, pathname) => {
            let res = fetch(`${server}/api/users/test`, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
            })

            let aux = await res;
            if (aux.status == 200) {
                return;
            }

            res = fetch(`${server}/api/users/test`, {
                headers: {
                    'Authorization': `Bearer ${refreshToken}`
                },
            })

            aux = await res;
            if (aux.status == 200) {
                localStorage.setItem("Access-Token", aux.headers.get("Access-Token"))
                return;
            }

            if (router.pathname !== "/login") {
                router.push({
                    pathname: '/login',
                    query: {pathname: pathname},
                }, '/login');
            }
        }

        if (accessToken && refreshToken) {
            fetchData(accessToken, refreshToken, router.pathname)
        } else {
            const pathname = router.pathname
            if (router.pathname !== "/login") {
                router.push({
                    pathname: '/login',
                    query: {pathname: pathname},
                }, '/login');
            }
        }
    }, [accessToken, refreshToken]);

    if (error || errorData) return <p>No data</p>
    if (!aux || !event || (!errorTickets && !tickets)) return <ProductItemLoading/>

    const onSubmit = async (data) => {
        console.log(data)

        console.log(rowsData)

        for (const x of rowsData) {
            await fetch(`${server}/api/tickets/${x}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
            })
        }

        // aux.eventId = router.query.id

        // console.log(aux)
        let res
        let auxi = {
            tickets: []
        }

        for (const d of data.tickets) {
            if (d.ticketId) {
                const ticket = tickets.find(x => {
                    return x.ticketId == d.ticketId
                })

                console.log(ticket)
                console.log(d)

                const defaultValues = {
                    ticketId: ticket.ticketId,
                    self: ticket.self,
                    bookings: ticket.bookings,
                    event: ticket.event,
                    ticketName: ticket.ticketName,
                    price: ticket.price,
                    qty: ticket.qty,
                    booked: ticket.booked,
                    maxPerUser: ticket.maxPerUser,
                    starting: ticket.starting ? ticket.starting : "",
                    until: ticket.until ? ticket.starting : ""
                }

                const newTicket: Required<NewTicketType> = { ...defaultValues};

                console.log(newTicket)

                let aux = JSON.parse(JSON.stringify(d));

                if (!isEqualsJson(d, newTicket)) {
                    console.log("equals")
                    res = await fetch(`${server}/api/tickets/${d.ticketId}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${accessToken}`
                        },
                        body: JSON.stringify(aux)
                    })
                }
            }
            else {
                auxi.tickets.push(d)
            }
        }

        let aux = JSON.parse(JSON.stringify(auxi));

        res = await fetch(`${server}/api/events/${router.query.id}/tickets`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(aux)
        })

        let json = await res;
    }

    return (
        <Layout t={t}>
            <Breadcrumb text={"My Events"}/>

            <section className="product-single">
                <div className="container">
                    <div className="product-single__content">
                        <img className={"product-gallery__image"} src={`data:image/png;base64,${aux.image}`} alt="My event image"/>
                        <Content product={event}/>
                    </div>

                    <form className="form" onSubmit={handleSubmit(onSubmit)}>
                        <table className="ticket-table ticket-table-input">
                            <thead>
                            <tr>
                                <th>Ticket name</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Max p/ user</th>
                                <th>Starting</th>
                                <th>Until</th>
                                <th>
                                    <button
                                        type="button"
                                        onClick={() => {
                                            if (fields.length >= 5) {
                                                alert("Te quedaste sin tickets hermano")
                                                return;
                                            }
                                            append({
                                                // id: "",
                                                ticketName: "",
                                                price: "",
                                                qty: "",
                                                // booked: "",
                                                maxPerUser: "",
                                                starting: "",
                                                until: ""
                                            });
                                        }}
                                    >
                                        Add
                                    </button>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                                {fields.map((item, index) => {
                                    return (
                                        <tr key={item.id}>
                                            <td>
                                                <input
                                                    className={quicksand.className}
                                                    defaultValue={`${item.ticketName}`}
                                                    {...register(`tickets[${index}].ticketName`, { required: true })}
                                                    type="text"
                                                />
                                                {errors.tickets?.at(index)?.ticketName?.type === 'required' && <span>This field is required</span>}
                                            </td>
                                            <td>
                                                <input
                                                    defaultValue={`${item.price}`}
                                                    {...register(`tickets[${index}].price`, { required: true, min: 0 })}
                                                    type="number"
                                                />
                                                {errors.tickets?.at(index)?.price?.type === 'required' && <span>This field is required</span>}
                                                {errors.tickets?.at(index)?.price?.type === 'min' && <span>MIN!!!!</span>}
                                            </td>
                                            <td>
                                                <input
                                                    defaultValue={`${item.qty}`}
                                                    {...register(`tickets[${index}].qty`, {
                                                        required: true,
                                                        min: getMinValue(item)
                                                    }
                                                    )}
                                                    type="number"
                                                />
                                                {errors.tickets?.at(index)?.qty?.type === 'required' && <span>This field is required</span>}
                                                {errors.tickets?.at(index)?.qty?.type === 'min' && <span>MIN!!!!</span>}
                                            </td>
                                            <td>
                                                <input
                                                    defaultValue={`${item.maxPerUser}`}
                                                    {...register(`tickets[${index}].maxPerUser`, { required: true, min: 1, max: 10 })}
                                                    type="number"
                                                />
                                                {errors.tickets?.at(index)?.maxPerUser?.type === 'required' && <span>This field is required</span>}
                                                {errors.tickets?.at(index)?.maxPerUser?.type === 'min' && <span>MIN!</span>}
                                                {errors.tickets?.at(index)?.maxPerUser?.type === 'max' && <span>MAX!</span>}
                                            </td>
                                            <td>
                                                <input
                                                    defaultValue={`${item.starting}`}
                                                    {...register(`tickets[${index}].starting`)}
                                                    type="datetime-local"
                                                />
                                                {errors.tickets?.at(index)?.starting?.type && <span>This field is required</span>}
                                            </td>
                                            <td>
                                                <input
                                                    defaultValue={`${item.until}`}
                                                    {...register(`tickets[${index}].until`, {
                                                        validate: () => {
                                                            if (getValues(`tickets[${index}].starting`) && getValues(`tickets[${index}].until`)) {
                                                                return new Date(getValues(`tickets[${index}].starting`)).getTime() < new Date(getValues(`tickets[${index}].until`)).getTime()
                                                            }
                                                            return true;
                                                        }
                                                    })}
                                                    type="datetime-local"
                                                />
                                                {errors.tickets?.at(index)?.until?.type === 'required' && <span>This field is required</span>}
                                                {errors.tickets?.at(index)?.until?.type === 'validate' && <span>STARTING > UNTIL GILASTRUM</span>}
                                            </td>

                                            <td>
                                                <button type="button" onClick={() => {
                                                    if (item.ticketId) {
                                                        console.log(item.ticketId)
                                                        rowsData.push(item.ticketId)
                                                        setRowsData(rowsData)
                                                    }
                                                    remove(index)
                                                }}>
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </table>
                        <input type="submit" />
                    </form>

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

export default Product

const getStaticProps = makeStaticProps(['common'])
export { getStaticPaths, getStaticProps }
