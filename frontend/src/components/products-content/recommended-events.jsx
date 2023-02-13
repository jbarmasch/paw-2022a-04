import EventItem from './event-item';
import EventItemLoading from './item-loading';
import useSwr from 'swr';
import { server } from '../../utils/server';
import i18n from '../../i18n'


const fetcher = (url) => fetch(url).then((res) => res.json());

const RecommendedEvents = ({ id }) => {
    console.log(id)
    const { data: aux, error: error } = useSwr(`${server}/api/events/${id}/recommended`, fetcher)

    if (error) return
    if (!aux) return (
        <div className="container">
            <div className="event-list featured-list">
                {[...Array(12)].map((e, i) => <EventItemLoading key={i} />)}
            </div>
        </div>
    )

    return (
        <>
            {aux.length > 0 &&
                <div className="container">
                    <h4 className="featured-title">{i18n.t(aux.length > 1 ? "event.recommendedPl" : "event.recommendedSi")}</h4>
                    <div className="event-list featured-list">
                        {aux.map((item) =>
                            <EventItem
                                id={item.id}
                                name={item.name}
                                minPrice={item.minPrice}
                                location={item.location}
                                type={item.type}
                                date={item.date}
                                key={item.id}
                                image={item.image}
                            />
                        )}
                    </div>
                </div>
            }</>
    );
}

export default RecommendedEvents;