import {Link} from 'react-router-dom';
import {getPrice} from "../../utils/price";
import Card from "@mui/material/Card";
import CardActionArea from "@mui/material/CardActionArea";
import i18n from "../../i18n";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";
import LocalOfferRoundedIcon from "@mui/icons-material/LocalOfferRounded";
import LocationOnRoundedIcon from "@mui/icons-material/LocationOnRounded";
import LocalActivityRoundedIcon from "@mui/icons-material/LocalActivityRounded";
import CalendarMonthRoundedIcon from "@mui/icons-material/CalendarMonthRounded";
import {ParseDateTime} from "../events-content/event-item";
import { LazyLoadImage } from 'react-lazy-load-image-component';

const MyEventItem = ({id, name, minPrice, location, type, date, image, soldOut}) => {
    return (
        <Link to={`/my-events/${id}`}>
            <Card className="event-card">
                <CardActionArea className="event-card-action">
                    <div className="event-card-container">
                        <LazyLoadImage
                            className="event-card-image"
                            component="img"
                            height="300px"
                            width="300px"
                            src={image}
                            alt={i18n.t("event.event")}
                        />
                        {!!soldOut && <span className="event-card-image-sold-out">{i18n.t("event.soldOut")}</span>}
                        {(minPrice === -1 && !(!!soldOut)) && <span className="event-card-image-no-tickets">{i18n.t("event.noTickets")}</span>}
                        {new Date(date) < Date.now() && <span className="event-card-image-over">{i18n.t("event.over")}</span>}
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