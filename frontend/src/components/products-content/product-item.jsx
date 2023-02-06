import {Link} from 'react-router-dom'
import useSwr from 'swr';
import ProductItemLoading from './item-loading';
import {getPrice} from "../../utils/price";
import {useEffect, useState} from "react";
import i18n from '../../i18n'

const ProductItem = ({discount, image, id, name, minPrice, currentPrice}) => {
    const fetcher = (url) => fetch(url).then((res) => res.json());

    const { data : aux, error : error } = useSwr(image, fetcher)

    if (error) return <p>No data</p>
    if (!aux) return <ProductItemLoading/>

    return (
        <div className="product-item">
            <div className="product__image">
                <Link to={`/events/${id}`} >
                    {/* {eventImage && <img className={"pointer"} src={`data:image/png;base64,${eventImage}`} alt="Event image"/>} */}
                    <img className={"pointer"} src={`data:image/png;base64,${aux.image}`} alt="Event image"/>
                    {/*<Image src={`data:image/png;base64,${aux.image}`} layout="raw" width={"100%"} height={"100%"} className="pointer"/>*/}
                </Link>
            </div>

            <div className="product__description">
                <h3>{name}</h3>
                <div className={"product__price " + (discount ? 'product__price--discount' : '')}>
                    <h4>{getPrice(currentPrice)}</h4>
                </div>
            </div>
        </div>
    )
};


export default ProductItem