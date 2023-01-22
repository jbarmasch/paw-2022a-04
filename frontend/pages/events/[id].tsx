import {useState} from 'react';
import Footer from '../../components/footer';
import Layout from '../../layouts/Main';
import Breadcrumb from '../../components/breadcrumb';
import ProductsFeatured from '../../components/products-featured';
import Description from '../../components/product-single/description';
import {server} from '../../utils/server';

import useSwr from "swr";
import Image from "next/image";
import ProductItemLoading from "../../components/product-item/loading";
import Content from "../../components/product-single/content";
import {useRouter} from "next/router";
import {TicketType} from "../../types";
import Select from "react-select";
import {postData} from "../../utils/services";
import { useForm, Controller } from "react-hook-form";
import * as React from "react";
import {FormattedMessage, useIntl} from "react-intl";


const Product = () => {
    const intl = useIntl();
    const [showBlock, setShowBlock] = useState('description');

    const fetcher = (url: string) => fetch(url).then((res) => res.json());

    const router = useRouter();

    const { register, handleSubmit, control, watch, formState: { errors } } = useForm();

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

    const getPrice = (price) => {
        return price > 0 ? "$" + price : "Free";
    }


    const onSubmit = async (data) => {
        delete data.location
        let i = 0
        let tickets = []

        for (let x in data) {
            tickets.push({
                id: data[x],
                qty: location[i++]
            })
        }

        console.log(tickets)

        const res = await postData(`${server}/api/tickets`, tickets);

        let json = await res.json();
    }

    return (
        <Layout>
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
                                    <th><FormattedMessage id={"event.ticket"}/></th>
                                    <th><FormattedMessage id={"event.price"}/></th>
                                    <th><FormattedMessage id={"event.quantity"}/></th>
                                </tr>
                                </thead>
                                {tickets.map((item: TicketType) => (
                                    <tbody key={item.id}>
                                    <tr>
                                        <td>{item.ticketName}</td>
                                        <td>{getPrice(item.price)}</td>
                                        <td>
                                            <input {...register("ticketId" + item.id, { required: true })} className={"casper"} value={item.id}/>
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
                                                            if (tickets[i].id === item.id) {
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
                                                            <label hidden htmlFor="location-input"><FormattedMessage id={"event.selectQty"}/></label>
                                                            <Select
                                                                className="form__input"
                                                                id={"qty-input-" + item.id}
                                                                value={currentSelection}
                                                                name={name}
                                                                options={list}
                                                                onChange={handleSelectChange}
                                                                placeholder={intl.formatMessage({id: "event.selectQty"})}
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
