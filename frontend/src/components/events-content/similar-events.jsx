import EventItem from './event-item';
import EventItemLoading from './item-loading';
import useSwr from 'swr';
import {server, fetcher} from '../../utils/server';
import i18n from '../../i18n'
import {useHistory} from "react-router-dom";

const SimilarEvents = ({id}) => {
    const history = useHistory()
    const {data: aux, isLoading, error} = useSwr(`${server}/api/events?similar=${id}`, fetcher)

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
            {aux && aux.length > 0 &&
                <div className="container">
                    <h4 className="featured-title">{i18n.t(aux.length > 1 ? "event.similarPl" : "event.similarSi")}</h4>
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
                                organizer={item.organizer}
                            />
                        )}
                    </div>
                </div>
            }
        </>
    );
}

export default SimilarEvents;