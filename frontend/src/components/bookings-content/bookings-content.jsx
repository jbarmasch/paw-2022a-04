import useSwr from 'swr';
import BookingItem from './booking-item';
import BookingsLoading from './bookings-loading';
import {server} from '../../utils/server';
import {useState} from "react";
import {parseLink} from '../../utils/pages';
import Pagination from '@mui/material/Pagination';
import {useLocation, useHistory} from 'react-router-dom'
import queryString from 'query-string'
import i18n from '../../i18n'
import NoBookingsContent from "./no-bookings-content";

const fetcherHeaders = (args) => fetch(args[0], {
    method: 'GET',
    headers: {
        'Authorization': `Bearer ${args[1]}`
    },
}).then((res) => {
    if (res.status === 200) {
        return {
            headers: res.headers,
            data: res.json()
        }
    } else {
        return {
            headers: res.headers,
            data: []
        }
    }
})

function Page({data, aux, setAux, mutate, userId}) {
    if (!data) return <BookingsLoading/>
    data = data.data

    Promise.resolve(data).then((x) => {
        setAux(x)
    })

    return (
        <>
            {(aux && aux.length > 0 &&
                <section>
                    {aux.map((item) => (
                        <BookingItem
                            image={item.image}
                            code={item.code}
                            id={item.id}
                            event={item.event}
                            rating={item.rating}
                            confirmed={item.confirmed}
                            ticketBookings={item.ticketBookings}
                            userId={userId}
                            key={item.id}
                            mutate={mutate}
                        />
                    ))}
                </section>) || <NoBookingsContent/>
            }
        </>
    );
}

const BookingsContent = ({userId}) => {
    const [pageIndex, setPageIndex] = useState(1);
    const [child, setChild] = useState();
    const history = useHistory()
    const {search} = useLocation()
    const values = queryString.parse(search)

    let links

    let accessToken;
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
    }

    let {data, isLoading, mutate, error} = useSwr([
        `${server}/api/bookings?page=${pageIndex}&userId=${userId}`,
        accessToken
    ], fetcherHeaders);

    if (error) {
        history.push("/404");
        return
    }

    if (!data || isLoading) {
        return <BookingsLoading/>
    }

    links = parseLink(data.headers.get("Link"))

    const handlePageChange = (e, page) => {
        setPageIndex(page)
        history.replace(`/bookings?page=${page}`)
    }

    return (
        <section className="users-content products-content">
            <div className="products-content__intro">
                <h2>{i18n.t("bookings.title")}</h2>
            </div>
            <div>
                <div>
                    {data && <Page data={data} aux={child} setAux={setChild} mutate={mutate} userId={userId}/>}
                </div>
                {links.last?.page && links.last?.page > 1 &&
                    <div className="pagination">
                        <Pagination count={Number(links && links.last ? links.last?.page : 0)} showFirstButton
                                            showLastButton page={values?.page ? Number(values?.page) : pageIndex}
                                            onChange={handlePageChange}/>
                    </div>
                }
            </div>
        </section>
    );
};

export default BookingsContent
  