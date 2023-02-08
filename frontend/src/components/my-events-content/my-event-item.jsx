import {Link} from 'react-router-dom';
import useSwr from 'swr';
import MyEventLoading from './my-event-loading';
import {getPrice} from "../../utils/price";

const fetcher = (url) => fetch(url).then((res) => res.json());

const MyEventItem = ({discount, image, id, name, minPrice, currentPrice}) => {
    const { data : aux, error : error } = useSwr(image, fetcher)

    if (error) return <p>No data</p>
    // TODO: change to isLoading
    if (!aux) return <MyEventLoading/>

    return (
        <div className="product-item">
            <div className="product__image">
                <Link to={`/my-events/${id}`} >
                        <img className={"pointer"} src={`data:image/png;base64,${aux.image}`} alt="My event"/>
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