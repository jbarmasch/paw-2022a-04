import {useState} from 'react';
import Layout from '../layout';
// import ProductsFeatured from '../../../components/products-featured';
// import Description from '../../../components/product-single/description';
import {server} from '../../utils/server';
import useSwr from "swr";
import UserLoading from "../users-content/user-loading";
// import Content from "../../../components/product-single/content";
import Select from "react-select";
// import {postData} from "../../utils/services";
import { useForm, Controller } from "react-hook-form";
import * as React from "react";
// import {FormattedMessage, useIntl} from "react-intl";
import i18n from '../../i18n'
import {Link} from 'react-router-dom'

const Organizer = (props) => {
    const fetcher = (url) => fetch(url).then((res) => res.json());
    const { data : user, error : error } = useSwr(props.match.params.id ? `${server}/api/users/${props.match.params.id}` : null, fetcher)

    if (error) return <p>No data</p>
    if (!user) return <UserLoading/>


    return (
        <Layout>
            <section className="product-single">
                <div className="container">
                    <div className="product-single__info">
                        <div className="product-single__info-btns">
                            <span>{user.username}</span>
                            <br/>
                            <span>{user.rating} ({user.votes})</span>
                        </div>

                    </div>
                </div>
            </section>
        </Layout>
    );
}

export default Organizer