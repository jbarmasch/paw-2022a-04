import useSwr from 'swr';
import BookingItem from './booking-item';
import BookingsLoading from './bookings-loading';
import {server} from '../../utils/server';
import {useState} from "react";
import {parseLink} from '../../utils/pages';
import Pagination from '@mui/material/Pagination';
import {useLocation, useHistory} from 'react-router-dom'
import queryString from 'query-string'
import {Controller, useForm} from "react-hook-form";
import i18n from '../../i18n'
import Select from '@mui/material/Select';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import MenuItem from '@mui/material/MenuItem';
import NoBookingsContent from "./no-bookings-content";

const fetcherHeaders = (...args) => fetch(...args).then((res) => {
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
    const [order, setOrder] = useState("DATE_ASC")
    const [child, setChild] = useState();
    const history = useHistory()
    const {search} = useLocation()
    const values = queryString.parse(search)

    let links
    const {control, formState: {errors}} = useForm();

    // TODO: tiene headers
    let {data, mutate, error} = useSwr(`${server}/api/bookings?page=${pageIndex}&userId=${userId}`, fetcherHeaders);

    if (error) {history.push("/404"); return}
    if (!data) return <BookingsLoading/>

    links = parseLink(data.headers.get("Link"))

    const handlePageChange = (e, page) => {
        setPageIndex(page)
        history.replace(`/bookings?page=${page}`)
    }

    let orderList = []
    orderList.push({
        value: "DATE_ASC",
        label: "Fecha ascendente"
    })
    orderList.push({
        value: "DATE_DESC",
        label: "Fecha descendente"
    })

    return (
        <section className="users-content products-content">
            <div className="products-content__intro">
                <h2>{i18n.t("bookings.title")}</h2>
            </div>
            <div>
                <div>
                    {data && <Page data={data} aux={child} setAux={setChild} mutate={mutate} userId={userId}/>}
                </div>
                <div className="pagination">
                <Pagination count={Number(links && links.last ? links.last?.page : 0)} showFirstButton
                                        showLastButton page={values?.page ? Number(values?.page) : pageIndex}
                                        onChange={handlePageChange}/>
                </div>
            </div>
        </section>
    );
};

export default BookingsContent
  