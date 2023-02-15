import {Link} from 'react-router-dom';
import useSwr from 'swr';
import MyEventLoading from './my-event-loading';
import {getPrice} from "../../utils/price";
import Card from "@mui/material/Card";
import CardActionArea from "@mui/material/CardActionArea";
import CardMedia from "@mui/material/CardMedia";
import i18n from "../../i18n";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";
import LocalOfferRoundedIcon from "@mui/icons-material/LocalOfferRounded";
import {fetcher} from '../../utils/server'
import LocationOnRoundedIcon from "@mui/icons-material/LocationOnRounded";
import LocalActivityRoundedIcon from "@mui/icons-material/LocalActivityRounded";
import CalendarMonthRoundedIcon from "@mui/icons-material/CalendarMonthRounded";
import {ParseDateTime} from "../events-content/event-item";

const MyEventItem = ({id, name, minPrice, location, type, date, image, soldOut, organizer}) => {
    const {data: aux, isLoading, error: error} = useSwr(image, fetcher)

    if (error) return <p>No data</p>
    if (isLoading) return <MyEventLoading/>

    return (
        <Link to={`/my-events/${id}`}>
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
                    </CardContent>
                </CardActionArea>
            </Card>
        </Link>
    )
};


export default MyEventItem