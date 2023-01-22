import Layout from '../layouts/Main';
import Link from 'next/link';
import {server} from '../utils/server';

import * as React from 'react';
import Select from "react-select";
import { useForm, Controller } from "react-hook-form";
import {FormattedMessage, useIntl} from "react-intl";

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

// @ts-ignore
const fetcher = (...args) => fetch(...args).then((res) => res.json())

// import useSWR from "swr";
import useSWRImmutable from 'swr/immutable'

import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import Breadcrumb from "../components/breadcrumb";
// import Checkbox from "../components/products-filter/form-builder/checkbox";

const CreateEvent = () => {
    const intl = useIntl()
    const router = useRouter()

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

    const { data : locations, error : errorData } = useSWRImmutable(`${server}/api/locations`, fetcher)
    const [location, setLocation] = useState<string[]>([]);
    const { data : tags, error : errorlocation } = useSWRImmutable(`${server}/api/tags`, fetcher)
    const [tag, setTag] = useState([]);
    const { data : types, error : errorType } = useSWRImmutable(`${server}/api/types`, fetcher)
    const [type, setType] = useState([]);

    const { register, handleSubmit, control, watch, formState: { errors } } = useForm();
    const [active, setActive] = useState(false)

    let start = 14;
    let ages = [{}];
    while (start <= 27) {
        ages.push({
            value: start,
            label: start
        })
        start++;
    }
    // console.log(ages)
    // const placeholder = intl.formatMessage({id: 'home.page'});
    // console.log(placeholder)

    const onSubmit = async (data) => {

        console.log(data)

        let aux: any[] = []
        data.tags.forEach(x => aux.push(x.value))

        // [0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2}T[0-9]{1,2}:[0-9]{1,2}
        let auxi = new Date(data.date).toISOString().slice(0, -3)

        let obj = {
            name: data.name,
            description: data.description,
            location: data.location.value,
            type: data.type.value,
            tags: aux,
            date: auxi,
            hasMinAge: data.hasMinAge,
            minAge: data.minAge
        }

        console.log(obj)

        // const JSONdata = JSON.stringify(data)

        const res = await fetch(`${server}/api/events`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(obj)
        })
        // const res = await postData(`${server}/api/events`, obj);


        let json = await res;

        if (json.status == 201) {
            router.push("/my-events/" + json.headers.get("Location")?.split("/").slice(-1)[0])
        }
        // console.log(json.headers.get("Location"))
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

    const style = {
        control: base => ({
            ...base,
            border: "revert",
            background: "transparent",
            boxShadow: "1px 1px transparent",
            height: "42px",
        })
    }

    const handleLocationChange = (event) => {
        // console.log(location)
        setLocation(event);
    };

    return (
        <Layout>
            <Breadcrumb text={"Create Event"}/>

            <section className="form-page">
                <div className="container">
                    {/*<div className="back-button-section">*/}
                    {/*    <Link href="/events">*/}
                    {/*        <a><i className="icon-left"></i> Back to store</a>*/}
                    {/*    </Link>*/}
                    {/*</div>*/}

                    <div className="form-block">
                        <h2 className="form-block__title">Create event</h2>
                        <form className="form" onSubmit={handleSubmit(onSubmit)}>
                            <label htmlFor="name-input"><FormattedMessage id="create.name"/>: </label>
                            <input className="form__input input__text" id="name-input" {...register("name", { required: true, maxLength: 10})} type="text" placeholder={intl.formatMessage({id: "create.name"})}/>
                            {errors.name?.type === 'required' && <span>This field is required</span>}
                            {errors.name?.type === 'maxLength' && <span>This field is invalid</span>}

                            <label htmlFor="description-input">Description: </label>
                            <input className="form__input input__text" id="description-input" {...register("description", {required: false, maxLength: 100})} type="text" placeholder={intl.formatMessage({id: "create.description"})}/>
                            {errors.description?.type === 'maxLength' && <span>This field is invalid</span>}

                            <Controller
                                control={control}
                                name="location"
                                render={({ field: { onChange, value, name, ref } }) => {
                                    const currentSelection = locationList.find(
                                        (c) => c.value === value
                                    );

                                    const handleSelectChange = (selectedOption) => {
                                        onChange(selectedOption);
                                        // console.log(selectedOption)
                                        setLocation(selectedOption);
                                    };

                                    return (
                                        <>
                                            <label htmlFor="location-input">Select location: </label>
                                            <Select
                                                className="form__input"
                                                id="location-input"
                                                value={currentSelection}
                                                name={name}
                                                options={locationList}
                                                onChange={handleSelectChange}
                                                placeholder={intl.formatMessage({id: "create.location"})}
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

                            {/*<Select*/}
                            {/*    id="type-input"*/}
                            {/*    instanceId="type-input"*/}
                            {/*    name="type"*/}
                            {/*    className="form__input"*/}
                            {/*    classNamePrefix="select"*/}
                            {/*    options={typeList}*/}
                            {/*    onChange={handleLocationChange}*/}
                            {/*    placeholder="Select type"*/}
                            {/*    styles={style}/>*/}

                            <Controller
                                control={control}
                                name="type"
                                render={({ field: { onChange, value, name, ref } }) => {
                                    const currentSelection = locationList.find(
                                        (c) => c.value === value
                                    );

                                    const handleSelectChange = (selectedOption) => {
                                        onChange(selectedOption);
                                        // console.log(selectedOption)
                                        setLocation(selectedOption);
                                    };

                                    return (
                                        <>
                                            <label htmlFor="type-input">Select type: </label>
                                            <Select
                                                className="form__input"
                                                id="type-input"
                                                value={currentSelection}
                                                name={name}
                                                options={typeList}
                                                onChange={handleSelectChange}
                                                placeholder={intl.formatMessage({id: "create.type"})}
                                                styles={style}
                                            />
                                        </>
                                    );
                                }}
                                rules={{
                                    required: true
                                }}
                            />
                            {errors.type?.type && <span>This field is required</span>}

                            {/*<Select*/}
                            {/*    id="tags-input"*/}
                            {/*    instanceId="tags-input"*/}
                            {/*    name="tags"*/}
                            {/*    isMulti*/}
                            {/*    className="form__input"*/}
                            {/*    classNamePrefix="select"*/}
                            {/*    options={tagList}*/}
                            {/*    onChange={handleLocationChange}*/}
                            {/*    placeholder="Select tags"*/}
                            {/*    styles={style}/>*/}

                            <Controller
                                control={control}
                                name="tags"
                                render={({ field: { onChange, value, name, ref } }) => {
                                    const currentSelection = locationList.find(
                                        (c) => c.value === value
                                    );

                                    const handleSelectChange = (selectedOption) => {
                                        onChange(selectedOption);
                                        // console.log(selectedOption)
                                        setLocation(selectedOption);
                                    };

                                    return (
                                        <>
                                            <label htmlFor="tags-input">Select tags: </label>
                                            <Select
                                                className="form__input"
                                                id="tags-input"
                                                isMulti
                                                value={currentSelection}
                                                name={name}
                                                options={tagList}
                                                onChange={handleSelectChange}
                                                placeholder={intl.formatMessage({id: "create.tags"})}
                                                styles={style}
                                            />
                                        </>
                                    );
                                }}
                                rules={{
                                    required: true
                                }}
                            />
                            {errors.tags?.type && <span>This field is required</span>}

                            <br/>

                            <input {...register("attendance", { required: true })} />
                            {errors.attendance && <span>This field is required</span>}

                            <input
                                type="datetime-local"
                                {...register("date", {
                                    valueAsDate: true,
                                    min: Date.now()
                                })}
                                min={Date.now()}
                            />

                            <div className="horizontal align-center">
                                <label><FormattedMessage id={"create.hasMin"}/></label>
                                {/*<input style={{appearance: "revert"}} type="checkbox" onClick={() => setActive(!active)}*/}
                                {/*       {...register("hasMinAge", { required: true })}/>*/}
                                <input style={{appearance: "revert"}} type="checkbox" onClick={() => setActive(!active)}/>
                                {/*<div onClick={() => setActive(!active)}>*/}
                                {/*    <Checkbox*/}
                                {/*        key="min_age"*/}
                                {/*        name="product-type"*/}
                                {/*        label="Min. age"*/}
                                {/*    />*/}
                                {/*</div>*/}

                                <Select
                                    id="age-input"
                                    instanceId="age-input"
                                    name="minAge"
                                    className="form__input"
                                    classNamePrefix="select"
                                    options={ages}
                                    onChange={handleLocationChange}
                                    placeholder={intl.formatMessage({id: "create.minAge"})}
                                    isDisabled={!active}
                                    styles={style}/>
                            </div>

                            {/*<input type="file" name="image-input"/>*/}
                            <button className="btn-submit" type="submit"><FormattedMessage id={"submit"}/></button>
                            {/*<input className={"btn-submit"} type="submit"/>*/}
                        </form>
                    </div>

                </div>
            </section>
        </Layout>
    )
}

export default CreateEvent
