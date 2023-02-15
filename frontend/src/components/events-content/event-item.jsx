import {Link, useHistory} from 'react-router-dom'
import useSwr from 'swr';
import EventItemLoading from './item-loading';
import {getPrice} from "../../utils/price";
import i18n from '../../i18n'
import {fetcher} from "../../utils/server";
import Card from '@mui/material/Card';
import CardActionArea from '@mui/material/CardActionArea';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import CardHeader from '@mui/material/CardHeader';
import LocalOfferRoundedIcon from '@mui/icons-material/LocalOfferRounded';
import LocationOnRoundedIcon from '@mui/icons-material/LocationOnRounded';
import LocalActivityRoundedIcon from '@mui/icons-material/LocalActivityRounded';
import CalendarMonthRoundedIcon from '@mui/icons-material/CalendarMonthRounded';
import PersonRoundedIcon from '@mui/icons-material/PersonRounded';

export const ParseDateTime = (datetime) => {
    const date = new Date(datetime);
    const dateTimeFormat = new Intl.DateTimeFormat(i18n.language, {
        year: 'numeric',
        month: 'numeric',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
    });
    return dateTimeFormat.format(date);
}

const EventItem = ({id, name, minPrice, location, type, date, image, soldOut, organizer}) => {
    const history = useHistory()

    const {data: aux, isLoading: isLoadingImg, error: errorImage} = useSwr(image, fetcher)
    const {data: org, isLoading, error: errorOrganizer} = useSwr(organizer, fetcher)

    if (errorImage || errorOrganizer) {
        history.push("/404");
        return
    }

    if (isLoading || isLoadingImg) return <EventItemLoading/>

    return (
        <Link to={`/events/${id}`}>
            <Card className="event-card">
                <CardActionArea className="event-card-action">
                    <div className="event-card-container">
                        <CardMedia
                            className="event-card-image"
                            component="img"
                            image={`data:image/png;base64,${aux.image}`}
                            alt="green iguana"
                        />
                        {!!soldOut && <span className="event-card-image-sold-out">{i18n.t("event.soldOut")}</span>}
                    </div>
                    <CardHeader className="event-card-header" title={name}/>
                    <CardContent className="event-card-content">
                        <span><LocalOfferRoundedIcon className="event-info-icons"/>{getPrice(minPrice)}</span>
                        <span><LocationOnRoundedIcon className="event-info-icons"/>{location.name}</span>
                        <span><LocalActivityRoundedIcon className="event-info-icons"/>{type.name}</span>
                        <span><CalendarMonthRoundedIcon className="event-info-icons"/>{ParseDateTime(date)}</span>
                        <span><PersonRoundedIcon className="event-info-icons"/>{org.username}</span>
                    </CardContent>
                </CardActionArea>
            </Card>
        </Link>

    )
};

export default EventItem