import useSwr from 'swr';
import EventItem from './event-item';
import ContentLoading from './content-loading';
import {useEffect, useState} from "react";
import Select from "react-select";
import {Controller, useForm} from "react-hook-form";
import {Link, useLocation, useHistory} from 'react-router-dom'
import queryString from 'query-string'
import {server} from "../../utils/server";
import i18n from '../../i18n'

const fetcher = (...args) => fetch(...args).then((res) => res.json())

function Page() {
    const { data: dataUp, error: errorUp } = useSwr(`${server}/api/events/upcoming`, fetcher)
    const { data: dataFew, error: errorFew } = useSwr(`${server}/api/events/few-tickets`, fetcher)

    if (errorUp || errorFew) return <p>No data</p>
    if (!dataUp || !dataFew) return <ContentLoading/>

    return (
        <>
            <section className="products-list">
                {dataUp.map((item) => (
                    <EventItem
                        id={item.id}
                        name={item.name}
                        minPrice={item.minPrice}
                        color={item.color}
                        currentPrice={item.minPrice}
                        key={item.id}
                        image={item.image}
                    />
                ))}
                {dataFew.map((item) => (
                    <EventItem
                        id={item.id}
                        name={item.name}
                        minPrice={item.minPrice}
                        color={item.color}
                        currentPrice={item.minPrice}
                        key={item.id}
                        image={item.image}
                    />
                ))}
            </section>
        </>
    );
}

const EventsContent = () => {
    return (
        <section className="products-content">
            <div className="products-content__intro">
                <h2>Men's Tops <span>(133)</span></h2>
            </div>
            <div>
                <div className="event-discovery flex w-full flex-1 flex-col items-center justify-center px-20 text-center">
                    <Page/>
                </div>
            </div>
        </section>
    );
};

export default EventsContent;
