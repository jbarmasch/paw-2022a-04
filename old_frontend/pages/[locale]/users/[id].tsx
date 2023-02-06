import {useState} from 'react';
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
import {TicketType} from "../../../types";
import Select from "react-select";
import {postData} from "../../../utils/services";
import { useForm, Controller } from "react-hook-form";
import * as React from "react";
// import {FormattedMessage, useIntl} from "react-intl";
import { useTranslation } from 'next-i18next'
import { getStaticPaths, makeStaticProps } from '../../../utils/get-static'
import Link from '../../../components/Link'

const Profile = () => {
    const { t } = useTranslation(['common'])
    
    const fetcher = (url: string) => fetch(url).then((res) => res.json());

    const router = useRouter();

    const { data : user, error : error } = useSwr(router.query.id ? `${server}/api/users/${router.query.id}` : null, fetcher)

    if (error) return <p>No data</p>
    if (!user) return <ProductItemLoading/>


    return (
        <Layout t={t}>
            <Breadcrumb text={"User"}/>

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

            <Footer/>
        </Layout>
    );
}

export default Profile

const getStaticProps = makeStaticProps(['common'])
export { getStaticPaths, getStaticProps }
