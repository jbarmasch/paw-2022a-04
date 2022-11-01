import * as React from 'react';
import type {NextPage} from 'next';
import Head from 'next/head';
import { ParsedUrlQuery } from 'querystring';
import { useForm } from "react-hook-form";

// @ts-ignore
const UserForm: NextPage<> = (props) => {
    const { register, handleSubmit, formState: { errors } } = useForm();
    const onSubmit = async data => {
        const JSONdata = JSON.stringify(data)
        const endpoint = 'http://localhost:8080/createUser' + '?' + new URLSearchParams(JSONdata)

        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            body: null,
        }

        const response = await fetch(endpoint, options)
        const result = await response.json()
        alert(`${result.data}`)
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <input {...register("username", { required: true, maxLength: 10})} type="text"/>
            {errors.username?.type === 'required' && <span>This field is required</span>}
            {errors.username?.type === 'maxLength' && <span>This field is invalid</span>}

            <input {...register("password", { required: true })} />
            {errors.password && <span>This field is required</span>}

            <input type="submit"/>
        </form>
    );
};

export default UserForm;
