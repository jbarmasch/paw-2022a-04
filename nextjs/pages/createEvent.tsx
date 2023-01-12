import * as React from 'react';
import type {NextPage} from 'next';
import Head from 'next/head';
import { ParsedUrlQuery } from 'querystring';
import Select, {ActionMeta, InputActionMeta} from "react-select";
import { useForm, Controller } from "react-hook-form";

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

interface ICategory {
    value: string;
    label: string;
}

// @ts-ignore
const fetcher = (...args) => fetch(...args).then((res) => res.json())

import Image from 'next/image'
import useSWR from "swr";
import {useState} from "react";
import {Label} from "@mui/icons-material";

const Event: NextPage<EventProps> = (props) => {
    const { data : locations, error : errorData } = useSWR(`${process.env.API_URL}/locations`, fetcher)
    const [location, setLocation] = useState<string[]>([]);
    const { data : tags, error : errorlocation } = useSWR(`${process.env.API_URL}/tags`, fetcher)
    const [tag, setTag] = useState([]);
    const { data : types, error : errorType } = useSWR(`${process.env.API_URL}/types`, fetcher)
    const [type, setType] = useState([]);

    const { register, handleSubmit, control, watch, formState: { errors } } = useForm();
    // const onSubmit = data => console.log(data);

    const onSubmit = async (data) => {
        console.log(data)

        // const JSONdata = JSON.stringify(data)

        const res = await fetch(process.env.API_URL + "/events", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                // 'Authorization': `Bearer ${token}`
            },
            body: data
        })

        let json = await res.json();
    }

    if (errorData || errorlocation || errorType) return <p>Loading...</p>
    if (!locations || !tags || !types) return <p>No data</p>

    let locationList: any[] = []
    locations.forEach(x => locationList.push({
        value: x.id,
        label: x.name
    }))

    let tagList: any[] = []
    tags.forEach(x => tagList.push({
        value: x.id,
        label: x.name
    }))

    let typeList: any[] = []
    types.forEach(x => typeList.push({
        value: x.id,
        label: x.name
    }))


    const handleLocationChange = (event) => {
        console.log(location)
        setLocation(event);
    };

    // const filterOption = (option, inputValue: string): boolean =>
    //     (option.label.toString().match(inputValue) || []).length > 0;

    return (
        /* "handleSubmit" will validate your inputs before invoking "onSubmit" */
        <form onSubmit={handleSubmit(onSubmit)}>
            {/* register your input into the hook by invoking the "register" function */}
            <label htmlFor="name-input">Name: </label>
            <input id="name-input" {...register("name", { required: true, maxLength: 10})} type="text" placeholder="Event"/>
            {errors.name?.type === 'required' && <span>This field is required</span>}
            {errors.name?.type === 'maxLength' && <span>This field is invalid</span>}

            <label htmlFor="description-input">Description: </label>
            <input id="description-input" {...register("description", {required: false, maxLength: 100})} type="text" placeholder="Event description"/>
            {errors.description?.type === 'maxLength' && <span>This field is invalid</span>}

            {/* <label for="location-input">location: </label>
            <select id="location-input" {...register("location", {required: true})} placeholder="location">
                {locations.map((location) => (
                    <option value={location.id}>{location.name}</option>
                ))}
            </select> */}

            <Controller
                control={control}
                name="location"
                render={({ field: { onChange, value, name, ref } }) => {
                    const currentSelection = locationList.find(
                        (c) => c.value === value
                    );

                    const handleSelectChange = (selectedOption) => {
                        onChange(selectedOption);
                        console.log(selectedOption)
                        setLocation(selectedOption);
                    };

                    return (
                        <>
                        <label htmlFor="location-input">Select location: </label>
                        <Select
                            id="location-input"
                            value={currentSelection}
                            name={name}
                            options={locationList}
                            onChange={handleSelectChange}
                        />
                        </>
                    );
                }}
                rules={{
                    required: true
                }}
            />
            {errors.location?.type && <span>This field is required</span>}

            <Select
                id="type-input"
                instanceId="type-input"
                name="type"
                className="basic-multi-select"
                classNamePrefix="select"
                options={typeList}
                onChange={handleLocationChange}
                placeholder="Select type"/>

            <Select
                id="tags-input"
                instanceId="tags-input"
                name="tags"
                isMulti
                className="basic-multi-select"
                classNamePrefix="select"
                options={tagList}
                onChange={handleLocationChange}
                placeholder="Select tags"/>

            <br/>
            <br/>

            <input type="checkbox"/>

            <br/>
            <br/>

            <select {...register("minAge")}/>

            <br/>

            {/* include validation with required or other standard HTML validation rules */}
            <input {...register("attendance", { required: true })} />
            {/* errors will return when field validation fails  */}
            {errors.attendance && <span>This field is required</span>}

            <input type="file" name="image-input"/>

            <input type="submit" />
        </form>
    );
};

export default Event;
