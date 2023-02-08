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


const UserItem = ({id, username, rating, votes}) => {

    return (

        <Card>
            <CardHeader title={username}/>
            <CardContent>
                {rating} ({votes})
            </CardContent>
            <CardActions>
                <Button size="small">Learn More</Button>
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