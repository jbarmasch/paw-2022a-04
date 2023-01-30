import Link from 'next/link';
import useSwr from 'swr';
import Image from "next/image";
import {ProductTypeList} from 'types';
import ProductItemLoading from './../product-item/loading';
import {getPrice} from "../../utils/price";
import {useEffect, useState} from "react";


const ProductItem = ({discount, image, id, name, minPrice, currentPrice, t}: ProductTypeList) => {
    const fetcher = (url: string) => fetch(url).then((res) => res.json());

    const { data : aux, error : error } = useSwr(image, fetcher)
    // let [eventImage, setEventImage] = useState(null)

    // useEffect(() => {
    //     fetch(image)
    //         .then(response => response.json())
    //         .then(data => {
    //             setEventImage(data.image)
    //         })
    // },[])

    if (error) return <p>No data</p>
    if (!aux) return <ProductItemLoading/>

    // console.log(t("event.free"))

    return (
        <div className="product-item">
            <div className="product__image">
                <Link href={`/events/${id}`} >
                    {/* {eventImage && <img className={"pointer"} src={`data:image/png;base64,${eventImage}`} alt="Event image"/>} */}
                    <img className={"pointer"} src={`data:image/png;base64,${aux.image}`} alt="Event image"/>
                    {/*<Image src={`data:image/png;base64,${aux.image}`} layout="raw" width={"100%"} height={"100%"} className="pointer"/>*/}
                </Link>
            </div>

            <div className="product__description">
                <h3>{name}</h3>
                <div className={"product__price " + (discount ? 'product__price--discount' : '')}>
                    <h4>{getPrice(currentPrice, t("event.free"), t("event.noTickets"))}</h4>
                </div>
            </div>
        </div>
    )
};


export default ProductItem