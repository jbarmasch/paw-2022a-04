import EventItem from './event-item';
import EventItemLoading from './item-loading';
import useSwr from 'swr';
import {server, fetcher} from '../../utils/server';
import i18n from '../../i18n'
import {useHistory} from 'react-router-dom'

const UpcomingEvents = () => {
    const history = useHistory()
    const {data, isLoading, error} = useSwr(`${server}/api/events/upcoming`, fetcher)

    if (error) {
        history.push("/404")
        return
    }

    if (isLoading) return (
        <div className="container">
            <div className="event-list featured-list">
                {[...Array(4)].map((e, i) => <EventItemLoading key={i}/>)}
            </div>
        </div>
    )

    return (
        <>
            {data && data.length > 0 &&
                <div className="container">
                    <h4 className="featured-title">{i18n.t(data.length > 1 ? "event.upcomingPl" : "event.upcomingSi")}</h4>
                    <div className="event-list featured-list">
                        {data.map((item) =>
                            <EventItem
                                id={item.id}
                                name={item.name}
                                minPrice={item.minPrice}
                                location={item.location}
                                type={item.type}
                                date={item.date}
                                key={item.id}
                                image={item.image}
                                soldOut={item.soldOut}
                                organizer={item.organizer}
                            />
                        )}
                    </div>
                </div>
            }</>
    );
}

export default UpcomingEvents;