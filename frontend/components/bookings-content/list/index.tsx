import useSwr from 'swr';
import BookingItem from '../../booking-item';
import ProductsLoading from './loading';
import {server} from '../../../utils/server';
import {useState} from "react";
import {BookingType} from "../../../types";

const fetcher = (...args) => fetch(...args).then((res) => res.json())

function Page({index, userId}) {
    const {data, error} = useSwr(`${server}/api/bookings?page=${index}&userId=${userId}`, fetcher);

    if (error) return <p>Loading...</p>
    if (!data) return <p>No data</p>

    return (
        <>
            {!data &&
                <ProductsLoading/>
            }

            {data &&
                <section className="products-list">
                    {data.map((item: BookingType) => (
                        <BookingItem
                            code={item.code}
                            id={item.id}
                            event={item.event}
                            rating={item.rating}
                            confirmed={item.confirmed}
                            ticketBookings={item.ticketBookings}
                            userId={userId}
                            key={item.id}
                        />
                    ))}
                </section>
            }
        </>
    );
}

const BookingsContent = ({userId}) => {
    const [pageIndex, setPageIndex] = useState(1);

    return (
        <div>
            <div className="event-discovery flex w-full flex-1 flex-col items-center justify-center px-20 text-center">
                <Page index={pageIndex} userId={userId}/>
                <div style={{ display: 'none' }}><Page index={pageIndex + 1} userId={userId}/></div>
            </div>
            <button className="pag-button" onClick={() => setPageIndex(pageIndex <= 1 ? pageIndex : pageIndex - 1)}>&laquo;</button>
            <button className="pag-button" onClick={() => setPageIndex(pageIndex + 1)}>&raquo;</button>
        </div>
    );
};

export default BookingsContent