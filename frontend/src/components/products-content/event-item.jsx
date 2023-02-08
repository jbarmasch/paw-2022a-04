import {Link} from 'react-router-dom'
import useSwr from 'swr';
import EventItemLoading from './item-loading';
import {getPrice} from "../../utils/price";
import {useEffect, useState} from "react";
import i18n from '../../i18n'
import Card from '@mui/material/Card';
import CardActionArea from '@mui/material/CardActionArea';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import Button from '@mui/material/Button';

const EventItem = ({discount, image, id, name, minPrice, currentPrice}) => {
    const fetcher = (url) => fetch(url).then((res) => res.json());

    const { data : aux, error : error } = useSwr(image, fetcher)

    if (error) return <p>No data</p>
    if (!aux) return <EventItemLoading/>

    const getHeight = () => {
        const el = document.getElementsByClassName("event-card")[0];
        if (el)
            return el.offsetWidth;
        return 250;
    }

    return (
        <Link to={`/events/${id}`}>
        <Card className="event-card">
      <CardActionArea className="event-card-action">
        <CardMedia
            className="event-card-image"
            component="img"
            // height="140"
            // height={getHeight()}
            image={`data:image/png;base64,${aux.image}`}
            alt="green iguana"
        />
        <CardContent>
            <h3>{name}</h3>
            {getPrice(minPrice)}
        </CardContent>
      </CardActionArea>
    </Card>
    </Link>

    )
};

        {/* <div className="product-item">
             <div className="product__image">
                 <Link to={`/events/${id}`} >
                     {/* {eventImage && <img className={"pointer"} src={`data:image/png;base64,${eventImage}`} alt="Event image"/>} 
                     <img className={"pointer"} src={`data:image/png;base64,${aux.image}`} alt="Event"/>
                     {/*<Image src={`data:image/png;base64,${aux.image}`} layout="raw" width={"100%"} height={"100%"} className="pointer"/>
                 </Link>
             </div>
             <div className="product__description">
                 <h3>{name}</h3>
                 <div className={"product__price " + (discount ? 'product__price--discount' : '')}>
                     <h4>{getPrice(currentPrice)}</h4>
                 </div>
             </div>
         </div> */}


export default EventItem