import {Link} from 'react-router-dom';
import useSwr from 'swr';
import UserLoading from './user-loading';
import {getPrice} from "../../utils/price";
import {useEffect, useState} from "react";
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardHeader from '@mui/material/CardHeader';
import Button from '@mui/material/Button';
import Rating from '@mui/material/Rating';
import i18n from "../../i18n"

const fetcher = (url) => fetch(url).then((res) => { if (res.status != 204) return res.json()})
// .then((res) => res.json());

const UserItem = ({id, username, rating, votes, events}) => {
    const { data : aux, isLoading, error : error } = useSwr(events, fetcher)

    if (error) {
        console.log(error)
        return <p></p>
    }

    // if (!isLoading) {
    //     console.log(aux)
    // }

    return (
        <Card>
            <CardHeader title={username}/>
            <CardContent>
                <span className="user-rating">{rating}<Rating value={rating} readOnly size="small"/> ({votes})</span>
            </CardContent>
            <CardActions>
                {(!isLoading && aux) ? <Link to={`/events?page=1&userId=${id}`}><Button size="small">{i18n.t("organizer.seeEvents")}</Button></Link> : <Button disabled size="small">{i18n.t("organizer.noEvents")}</Button>}
            </CardActions>
        </Card>
    )
};

        {/* <div className="product-item">
            <div className="product__image">
                <Link to={`/organizers/${id}`}>
                    <span>{username}</span>
                </Link>
            </div>

            <div className="product__description">
                <h3>{rating} ({votes})</h3>
            </div>
    </div> */}


export default UserItem