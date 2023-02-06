import {useState} from 'react';
import Footer from '../../../components/footer';
import Layout from '../../../layouts/Main';
import Breadcrumb from '../../../components/breadcrumb';
import ProductsFeatured from '../../../components/products-featured';
import Description from '../../../components/product-single/description';
import {server} from '../../../utils/server';
import {getPrice} from '../../../utils/price';

import useSwr from "swr";
import Image from "next/image";
import ProductItemLoading from "../../../components/product-item/loading";
import Content from "../../../components/product-single/content";
import {useRouter} from "next/router";
import {TicketType} from "../../../types";
import Select from "react-select";
import {postData} from "../../../utils/services";
import { useForm, Controller } from "react-hook-form";
import * as React from "react";
import { useTranslation } from 'next-i18next'
import { getStaticPaths, makeStaticProps } from '../../../utils/get-static'
import Link from '../../../components/Link'
import {useEffect} from "react";

const Event = () => {
    const { t } = useTranslation(['common'])

    const [showBlock, setShowBlock] = useState('description');

    const fetcher = (url: string) => fetch(url).then((res) => res.json());

    const router = useRouter();

    const { register, handleSubmit, control, watch, formState: { errors } } = useForm();

    let accessToken: string | null
    let refreshToken: string | null
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
        refreshToken = localStorage.getItem("Refresh-Token");
    }

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

    const { data : event, error : errorData } = useSwr(router.query.id ? `${server}/api/events/${router.query.id}` : null, fetcher)
    const { data : tickets, error : errorTickets } = useSwr(event ? `${event.tickets}` : null, fetcher)
    const { data : aux, error : error } = useSwr(event ? `${event.image}` : null, fetcher)
    const [location, setLocation] = useState<string[]>(tickets ? new Array<string>(tickets.length) : []);

    if (error || errorData) return <p>No data</p>
    if (!aux || !event) return <ProductItemLoading/>

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
        console.log(data)
        delete data.location
        let i = 0
        let auxi = {
            bookings: []
        }

        for (let x in data) {
            auxi.bookings.push({
                ticketId: data[x],
                qty: location[i++]
            })
        }

        console.log(auxi)
        console.log(auxi.bookings)

        const response = await fetch(`${server}/api/events/${router.query.id}/bookings`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(auxi)
        });

        let json = await response;
    }

    return (
        <Layout t={t}>
            <Breadcrumb text={"All Events"}/>

            <section className="product-single">
                <div className="container">
                    <div className="product-single__content">
                        <img className={"product-gallery__image"} src={`data:image/png;base64,${aux.image}`} alt="Event image"/>
                        {/*<Image src={`data:image/png;base64,${aux.image}`} className="product-gallery__image" layout="raw" width={"400px"} height={"400px"}/>*/}
                        <Content product={event}/>
                    </div>

                    <form className="form" onSubmit={handleSubmit(onSubmit)}>
                        {tickets && (
                            <div>
                            <table className="ticket-table">
                                <thead>
                                <tr>
                                    <th>{t("event.ticket")}</th>
                                    <th>{t("event.price")}</th>
                                    <th>{t("event.quantity")}</th>
                                </tr>
                                </thead>
                                {tickets.map((item: TicketType) => (
                                    <tbody key={item.ticketId}>
                                    <tr>
                                        <td>{item.ticketName}</td>
                                        <td>{getPrice(item.price, t("event.free"), t("event.noTickets"))}</td>
                                        <td>
                                            <input {...register("ticketId" + item.ticketId, { required: true })} className={"casper"} value={item.ticketId}/>
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
                                                            <label hidden htmlFor="location-input">{t("event.selectQty")}</label>
                                                            <Select
                                                                className="form__input"
                                                                id={"qty-input-" + item.ticketIdid}
                                                                value={currentSelection}
                                                                name={name}
                                                                options={list}
                                                                onChange={handleSelectChange}
                                                                placeholder={t("event.selectQty")}
                                                                styles={style}
                                                            />
                                                        </>
                                                    );
                                                }}
                                                rules={{
                                                    required: true
                                                }}
                                            />
                                            {errors.location?.type && <span>This field is required</span>}
                                        </td>
                                    </tr>
                                    </tbody>
                                ))}
                            </table>
                        </div>
                        )}

                        <button type="submit" className="btn btn--rounded btn--yellow btn-submit">{t("login.signIn")}</button>
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

export default Event

const getStaticProps = makeStaticProps(['common'])
export { getStaticPaths, getStaticProps }