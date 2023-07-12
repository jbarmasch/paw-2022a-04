import useSwr from 'swr';
import ContentLoading from './content-loading';
import MyEventItem from './my-event-item';
import {server} from '../../utils/server';
import {useState} from "react";
import {parseLink} from '../../utils/pages';
import {useLocation, useHistory} from 'react-router-dom'
import queryString from 'query-string'
import Pagination from '@mui/material/Pagination';
import {Controller, useForm} from "react-hook-form";
import i18n from '../../i18n'
import Select from '@mui/material/Select';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import MenuItem from '@mui/material/MenuItem';
import NoEventsContent from '../events-content/no-events-content';

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

function Page({data, aux, setAux}) {
    if (!data) return <ContentLoading/>
    data = data.data

    Promise.resolve(data).then((x) => {
        setAux(x)
    })

    return (
        <>
            {(aux && aux.length > 0 &&
                <section className="event-list my-event-list">
                    {aux.map((item) => (
                        <MyEventItem
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
                    ))}
                </section>) ||
            <NoEventsContent/>
        }
        </>
    );
}

const MyEventsContent = ({userId}) => {
    const [pageIndex, setPageIndex] = useState(1);
    const [order, setOrder] = useState("DATE_ASC")
    const [child, setChild] = useState();
    const history = useHistory()
    const [orderProductsOpen, setOrderProductsOpen] = useState(false);
    const {search} = useLocation()
    const values = queryString.parse(search)

    let links

    const {control} = useForm();

    let page = values.page ? `${values.page}` : 1
    let orderFilter = values.order ? `&order=${values.order}` : ''

    let {
        data,
        mutate,
        isLoading,
        error
    } = useSwr(`${server}/api/events?page=${page}&userId=${userId}&noTickets=true&soldOut=true&showPast=true${orderFilter}`, fetcherHeaders);

    if (error) {
        history.push("/404");
        return
    }

    if (isLoading) {
        return <ContentLoading/>
    }

    links = parseLink(data.headers.get("Link"))

    const handlePageChange = (e, page) => {
        setPageIndex(page)
        let str
        if (search.includes("page")) {
            str = search.replace(new RegExp("[0-9]"), page)
        } else {
            str = `/my-events?page=${page}`
        }
        history.replace(str)
    }

    const handleOrderChange = (option) => {
        let value = option.target.value
        setOrder(value)
        let str = `/my-events?page=1&order=${value}`
        history.replace(str)
    }


    let orderList = []
    orderList.push({
        value: "DATE_ASC",
        label: i18n.t("order.dateAsc")
    })
    orderList.push({
        value: "DATE_DESC",
        label: i18n.t("order.dateDesc")
    })

    return (
        <section className="users-content products-content">
            <div className="products-content__intro">
                <h2>{i18n.t("myEvents.title")}</h2>
                <button type="button" onClick={() => setOrderProductsOpen(!orderProductsOpen)}
                        className="products-filter-btn"><i className="icon-filters"></i></button>
                <form className={`products-content__filter ${orderProductsOpen ? 'products-order-open' : ''}`}>
                    <div className="products__filter__select">
                        <Controller
                            control={control}
                            defaultValue={'DATE_ASC'}
                            name="order"
                            render={() => {
                                return (
                                    <FormControl>
                                        <InputLabel className="order-label"
                                                    id="order-select-label">{i18n.t("filter.sortBy")}</InputLabel>
                                        <Select
                                            className="order-select"
                                            labelId="order-select-label"
                                            id="order-select"
                                            value={order ? order : "DATE_ASC"}
                                            onChange={handleOrderChange}
                                            input={<OutlinedInput className="order-label"
                                                                  label={i18n.t("filter.sortBy")}/>}
                                        >
                                            {orderList.map((x) => (
                                                <MenuItem
                                                    key={x.value}
                                                    value={x.value}
                                                >
                                                    {x.label}
                                                </MenuItem>
                                            ))}
                                        </Select>
                                    </FormControl>
                                );
                            }}
                        />
                    </div>
                </form>
            </div>
            <div>
                <div className="event-discovery flex w-full flex-1 flex-col items-center justify-center px-20 text-center">
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

export default MyEventsContent