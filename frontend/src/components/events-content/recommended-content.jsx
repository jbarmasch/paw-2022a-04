{/*
import useSwr from 'swr';
import EventItem from './event-item';
import ContentLoading from './content-loading';
import {server} from "../../utils/server";

const fetcher = (...args) => fetch(...args).then((res) => res.json())

function Page() {
    const {data: dataUp, error: errorUp} = useSwr(`${server}/api/events/upcoming`, fetcher)
    const {data: dataFew, error: errorFew} = useSwr(`${server}/api/events/few-tickets`, fetcher)

    if (errorUp || errorFew) return <p>No data</p>
    if (!dataUp || !dataFew) return <ContentLoading/>

    return (
        <>
            <section className="event-list">
                {dataUp.map((item) => (
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
                ))}
                {dataFew.map((item) => (
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
                ))}
            </section>
        </>
    );
}

const EventsContent = () => {
    return (
        <section className="products-content">
            <div className="products-content__intro">
                <h2>Men's Tops <span>(133)</span></h2>
            </div>
            <div>
                <div
                    className="event-discovery flex w-full flex-1 flex-col items-center justify-center px-20 text-center">
                    <Page/>
                </div>
            </div>
        </section>
    );
};

export default EventsContent; */}
