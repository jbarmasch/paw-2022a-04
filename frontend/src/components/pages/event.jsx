import { useState } from 'react';
import Footer from '../footer';
import Layout from '../layout';
// import ProductsFeatured from '../products-featured';
// import Description from '../product-single/description';
import { server } from '../../utils/server';
import { getPrice } from '../../utils/price';

import useSwr from "swr";
import ProductItemLoading from "../products-content/item-loading";
// import Content from "../product-single/content";
import Select from "react-select";
// import {postData} from "../../utils/services";
import { useForm, Controller } from "react-hook-form";
import * as React from "react";
import i18n from '../../i18n'
import { Link, useHistory, useLocation } from 'react-router-dom'
import useFindPath from '../header'
import { useEffect } from "react";
import { checkLogin } from '../../utils/auth'
import Paper from '@mui/material/Paper';
import Chip from '@mui/material/Chip';
import Button from '@mui/material/Button';
import SimilarEvents from '../products-content/similar-events'
import RecommendedEvents from '../products-content/recommended-events'

const Event = (props) => {
    const prevLocation = useLocation();

    const [showBlock, setShowBlock] = useState('description');

    const fetcher = (url) => fetch(url).then((res) => res.json());

    const { register, handleSubmit, control, watch, formState: { errors } } = useForm();

    const history = useHistory();
    let path = useFindPath();


    let accessToken;
    let refreshToken;

    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
        refreshToken = localStorage.getItem("Refresh-Token");
        // pendingBook = localStorage.getItem("PendingBook");
    }

    // useEffect(() => {
    //     if (!pendingBook || !accessToken) {
    //         return
    //     }

    //     const fetchData = async (accessToken, pendingBook) => {
    //         const response = await fetch(`${server}/api/events/${props.match.params.id}/bookings`, {
    //             method: 'POST',
    //             headers: {
    //                 'Content-Type': 'application/json',
    //                 'Authorization': `Bearer ${accessToken}`
    //             },
    //             body: pendingBook
    //         });

    //         let json = await response; 
    //     }

    //     fetchData(accessToken, pendingBook)
    // }, [pendingBook, accessToken])

    const { data: event, error: errorData } = useSwr(props.match.params.id ? `${server}/api/events/${props.match.params.id}` : null, fetcher)
    const { data: tickets, error: errorTickets } = useSwr(event ? `${event.tickets}` : null, fetcher)
    const { data: aux, error: error } = useSwr(event ? `${event.image}` : null, fetcher)
    const { data: organizer, error: errorOrganizer } = useSwr(event ? `${event.organizer}` : null, fetcher)
    const [location, setLocation] = useState(tickets ? new Array(tickets.length) : []);

    if (error || errorData || errorOrganizer) return <p>{i18n.t("noData")}</p>
    if (!aux || !event || !organizer) return <ProductItemLoading />

    const style = {
        control: base => ({
            ...base,
            border: "revert",
            background: "transparent",
            boxShadow: "1px 1px transparent",
            height: "42px",
        })
    }

    const getArray = (max, left) => {
        max = max <= left ? max : left
        let start = 1;
        let ages = [];
        while (start <= max) {
            ages.push({
                value: start,
                label: start
            })
            start++;
        }
        return ages;
    }

    const onSubmit = async (data) => {
        let isLogged = await checkLogin(accessToken, refreshToken)
        console.log(isLogged)

        console.log(data)
        delete data.location
        let i = 0
        let auxi = {
            bookings: []
        }

        for (let x in data) {
            let qtyAux = location[i++]
            if (qtyAux) {
                auxi.bookings.push({
                    ticketId: data[x],
                    qty: qtyAux
                })
            }
        }

        if (!isLogged) {
            // localStorage.setItem('PendingBook', JSON.stringify(auxi))
            history.push(`/login?redirectTo=${prevLocation.pathname}`);
            return
        }

        const response = await fetch(`${server}/api/events/${props.match.params.id}/bookings`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(auxi)
        });

        let json = await response;

        if (json.status == 201) {
            history.push({
                pathname: `/thank-you`,
                state: {
                    booking: json.headers.get("Location"),
                    eventId: props.match.params.id
                }
            });
        }
    }

    console.log("MAYORES DE" + event.minAge)
    console.log("MAYORES DE" + event)

    return (
        <Layout>

            <section className="product-single">
                <div className="container">
                    <div className="event-content">
                        <img className="event-image" src={`data:image/png;base64,${aux.image}`} alt="Event" />
                        <Paper className="event-info" elevation={2}>
                            <ul>
                                <li>
                                    <h4>{i18n.t("event.name")}</h4>
                                    <span>{event.name}</span>
                                </li>
                                <li>
                                    <h4>{i18n.t("event.description")}</h4>
                                    <span>{event.description}</span>
                                </li>
                                <li>
                                    <h4>{i18n.t("event.minPrice")}</h4>
                                    <span>{event.minPrice}</span>
                                </li>
                                <li>
                                    <h4>{i18n.t("event.type")}</h4>
                                    <span>{event.type.name}</span>
                                </li>
                                <li>
                                    <h4>{i18n.t("event.location")}</h4>
                                    <span>{event.location.name}</span>
                                </li>
                                <li>
                                    <h4>{i18n.t("event.tags")}</h4>
                                    {event.tags && event.tags.map((t) => 
                                        // <Link className="pointer" to={`/events?tags=${t.id}`}>
                                            <Chip label={t.name} key={t.id} onClick={() => history.push(`/events?page=1&tags=${t.id}`)}/>
                                        // </Link>
                                    )}
                                    {/* <span>{event.tags}</span> */}
                                </li>
                                <li>
                                    <h4>{i18n.t("event.organizer")}</h4>
                                    <Link className="underline" to={`/organizers/${organizer.id}`}>{organizer.username}</Link>
                                </li>
                                {event.minAge &&
                                    <li>
                                        <h4>{i18n.t("event.minAge")}</h4>
                                        <span>{i18n.t("event.minAgeText")} {event.minAge}</span>
                                    </li>
                                }
                                </ul>
                        </Paper>
                        <div>&nbsp;</div>
                        {/*<Image src={`data:image/png;base64,${aux.image}`} className="product-gallery__image" layout="raw" width={"400px"} height={"400px"}/>*/}
                        {/* <Content product={event}/> */}
                        <form className="form" onSubmit={handleSubmit(onSubmit)}>
                            {tickets && (
                                <div>
                                    <table className="ticket-table">
                                        <thead>
                                            <tr>
                                                <th>{i18n.t("event.ticket")}</th>
                                                <th>{i18n.t("event.price")}</th>
                                                <th>{i18n.t("event.quantity")}</th>
                                            </tr>
                                        </thead>
                                        {tickets.map((item) => (
                                            <tbody key={item.ticketId}>
                                                <tr>
                                                    <td>{item.ticketName}</td>
                                                    <td>{getPrice(item.price, false)}</td>
                                                    <td>
                                                        <input {...register("ticketId" + item.ticketId, { required: true })} className={"casper"} value={item.ticketId} />
                                                        <Controller
                                                            control={control}
                                                            name="location"
                                                            render={({ field: { onChange, value, name, ref } }) => {
                                                                const list = getArray(item.maxPerUser, item.qty - item.booked)
                                                                const currentSelection = list.find(
                                                                    (c) => c.value === value
                                                                );

                                                                const handleSelectChange = (selectedOption) => {
                                                                    onChange(selectedOption);
                                                                    let value = -1
                                                                    for (let i = 0; i < tickets.length; i++) {
                                                                        if (tickets[i].ticketId === item.ticketId) {
                                                                            value = i;
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (value == -1) {
                                                                        alert('que cambias')
                                                                    }
                                                                    location[value] = selectedOption.value
                                                                    setLocation(location);
                                                                };

                                                                return (
                                                                    <>
                                                                        <label hidden htmlFor="location-input">{i18n.t("event.selectQty")}</label>
                                                                        <Select
                                                                            className="form__input"
                                                                            id={"qty-input-" + item.ticketIdid}
                                                                            value={currentSelection}
                                                                            name={name}
                                                                            options={list}
                                                                            onChange={handleSelectChange}
                                                                            placeholder={i18n.t("event.selectQty")}
                                                                            styles={style}
                                                                        />
                                                                    </>
                                                                );
                                                            }}
                                                            rules={{
                                                                required: true
                                                            }}
                                                        />
                                                        {errors.location?.type && <span>{i18n.t("fieldRequired")}</span>}
                                                    </td>
                                                </tr>
                                            </tbody>
                                        ))}
                                    </table>
                                </div>
                            )}

                            {/* <button type="submit" className="btn btn--rounded btn--yellow btn-submit">{i18n.t("login.signIn")}</button> */}
                            <Button color="secondary" variant="contained" type="submit">{i18n.t("book")}</Button>
                        </form>
                    </div>



                    {/*
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

                    </div> */}
                </div>
            </section>
            {/* <RecommendedEvents id={event.id} />
            <SimilarEvents id={event.id} /> */}
        </Layout>
    );
}

export default Event
