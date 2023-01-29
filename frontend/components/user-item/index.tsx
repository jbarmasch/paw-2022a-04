import Link from 'next/link';
import useSwr from 'swr';
import Image from "next/image";
import {UserTypeList} from 'types';
import ProductItemLoading from './../product-item/loading';
import {getPrice} from "../../utils/price";
import {useEffect, useState} from "react";


const UserItem = ({id, username, rating, votes}: UserTypeList) => {

    return (
        <div className="product-item">
            <div className="product__image">
                <Link href={`/users/${id}`}>
                    <span>{username}</span>
                </Link>
            </div>

            <div className="product__description">
                <h3>{rating} ({votes})</h3>
            </div>
        </div>
    )
};


export default UserItem