import Link from 'next/link';
import useSwr from 'swr';
import Image from "next/image";
import {ProductTypeList} from 'types';
import ProductItemLoading from './../product-item/loading';
import {getPrice} from "../../utils/price";

const fetcher = (url: string) => fetch(url).then((res) => res.json());

const MyEventItem = ({discount, image, id, name, minPrice, currentPrice}: ProductTypeList) => {
    const { data : aux, error : error } = useSwr(image, fetcher)

    if (error) return <p>No data</p>
    if (!aux) return <ProductItemLoading/>

    return (
        <div className="product-item">
            <div className="product__image">
                <Link href={`/my-events/${id}`} >
                        <img className={"pointer"} src={`data:image/png;base64,${aux.image}`} alt="My event image"/>
                        {/*<Image src={`data:image/png;base64,${aux.image}`} width={"100%"} height={"100%"} className="pointer" alt="My event image"/>*/}
                </Link>
            </div>

            <div className="product__description">
                <h3>{name}</h3>
                <div className={"product__price"}>
                    <h4>{getPrice(currentPrice)}</h4>
                </div>
            </div>
        </div>
    )
};


export default MyEventItem