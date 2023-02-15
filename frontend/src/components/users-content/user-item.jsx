import {Link} from 'react-router-dom';
import useSwr from 'swr';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardHeader from '@mui/material/CardHeader';
import Button from '@mui/material/Button';
import Rating from '@mui/material/Rating';
import i18n from "../../i18n"
import {fetcher} from "../../utils/server";
import UserLoading from "./user-loading";

const UserItem = ({id, username, rating, votes, events}) => {
    const {data: aux, isLoading, error} = useSwr(events, fetcher)

    if (error) {
        return <p></p>
    }

    if (isLoading) return <UserLoading/>

    return (
        <Card>
            <CardHeader title={username}/>
            <CardContent>
                <span className="user-rating">{rating}<Rating value={rating} readOnly size="small"/> ({votes})</span>
            </CardContent>
            <CardActions>
                {(aux && aux.length > 0) ? <Link to={`/events?page=1&userId=${id}`}><Button
                        size="small">{i18n.t("organizer.seeEvents")}</Button></Link> :
                    <Button disabled size="small">{i18n.t("organizer.noEvents")}</Button>}
            </CardActions>
        </Card>
    )
};

export default UserItem