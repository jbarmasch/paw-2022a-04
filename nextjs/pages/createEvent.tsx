import * as React from 'react';
import type {NextPage} from 'next';
import Head from 'next/head';
import { ParsedUrlQuery } from 'querystring';
import { useForm } from "react-hook-form";

interface Event {
    name: string,
    attendance: number,
    date: string,
    description: string,
    image: string,
    location: string,
    maxCapacity: number,
    minPrice: number,
    organizer: string,
    self: string,
    tags: object,
    tickets: string,
    type: string
}

interface EventProps {
    data: Event
    dataAux: string
}

interface Params extends ParsedUrlQuery {
    id: string,
}

import Image from 'next/image'
const Event: NextPage<EventProps> = (props) => {
    const { register, handleSubmit, watch, formState: { errors } } = useForm();
    const onSubmit = data => console.log(data);

    return (
        /* "handleSubmit" will validate your inputs before invoking "onSubmit" */
        <form onSubmit={handleSubmit(onSubmit)}>
            {/* register your input into the hook by invoking the "register" function */}
            <input {...register("name", { required: true, maxLength: 10})} type="text" />
            {errors.name?.type === 'required' && <span>This field is required</span>}
            {errors.name?.type === 'maxLength' && <span>This field is invalid</span>}

            {/* include validation with required or other standard HTML validation rules */}
            <input {...register("attendance", { required: true })} />
            {/* errors will return when field validation fails  */}
            {errors.attendance && <span>This field is required</span>}

            <input type="submit" />
        </form>
    );
};

export default Event;
