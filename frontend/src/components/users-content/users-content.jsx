import useSwr from 'swr';
import {server} from '../../utils/server';
import {parseLink} from '../../utils/pages';
import UserItem from "./user-item";
import {useState, useEffect} from "react";
import Pagination from '@mui/material/Pagination';
import ContentLoading from './content-loading';
import {useHistory, useLocation} from 'react-router-dom'
import {Controller, useForm} from "react-hook-form";
import i18n from '../../i18n'
import Select from '@mui/material/Select';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import MenuItem from '@mui/material/MenuItem';
import TextField from '@mui/material/TextField';
import InputAdornment from '@mui/material/InputAdornment';
import queryString from 'query-string'
import SearchRoundedIcon from '@mui/icons-material/SearchRounded';
import NoOrganizersContent from "./no-users-content";

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
                <section className="user-list">
                    {aux.map((item) => (
                        <UserItem
                            key={item.id}
                            id={item.id}
                            username={item.username}
                            rating={item.rating}
                            votes={item.votes}
                            events={item.events}
                        />
                    ))}
                </section>) ||
                <NoOrganizersContent/>
            }
        </>
    );
}

const UsersContent = () => {
    const history = useHistory()
    const {search, pathname} = useLocation()
    const values = queryString.parse(search)

    const [query, setQuery] = useState()
    const [order, setOrder] = useState();
    const [pageIndex, setPageIndex] = useState(1);
    const [child, setChild] = useState();
    const [firstLoad, setFirstLoad] = useState(true)

    useEffect(() => {
        if (firstLoad && (values.search || values.order)) {
            setOrder(values?.order)
            setQuery(values?.search)
            setFirstLoad(false)
        }
    }, [values.search, values.order, values.soldOut, firstLoad])

    useEffect(() => {
        if (!firstLoad && (search || order)) {
            setPageIndex(1)
            let queryUrl = ""
            if (order?.length > 0) {
                queryUrl = `${queryUrl}&order=${order}`
            }
            if (query?.length > 0) {
                queryUrl = `${queryUrl}&search=${query}`
            }
            let url = `/organizers?page=1${queryUrl}`
            let oldPath = pathname + search
            if (oldPath === url) {
                return
            }
            history.push(url)
        }
    }, [query, order, firstLoad])

    const {handleSubmit, control, getValues, formState: {errors}} = useForm();

    let filtersStr = ""
    if (values.page || values.search || values.order) {
        if (values.page) {
            filtersStr = `?page=${values.page}`
        } else {
            filtersStr = `?page=1`
        }
        if (values.search) {
            filtersStr = `${filtersStr}&search=${values.search}`
        }
        if (values.order) {
            filtersStr = `${filtersStr}&order=${values.order}`
        }
    }

    let links

    const {data, error} = useSwr(`${server}/api/organizers${filtersStr}`, fetcherHeaders)

    if (error) {
        history.push("/404");
        return
    }

    if (!data) return <ContentLoading/>

    links = parseLink(data.headers.get("Link"))

    const handlePageChange = (e, page) => {
        setPageIndex(page)
        let str
        if (search.includes("page")) {
            str = search.replace(new RegExp("[0-9]"), page)
        } else {
            str = `/organizers?page=${page}`
        }
        history.replace(str)
    }

    let orderList = []
    orderList.push({
        value: "RATING_ASC",
        label: "Rating ascendente"
    })
    orderList.push({
        value: "RATING_DESC",
        label: "Rating descendente"
    })
    orderList.push({
        value: "USERNAME_ASC",
        label: "Usuario ascendente"
    })
    orderList.push({
        value: "USERNAME_DESC",
        label: "Usuario descendente"
    })

    const onSubmit = (data) => {
        setFirstLoad(false);
        setQuery(getValues("search"));
    }

    return (
        <section className="users-content products-content">
            <div className="products-content__intro">
                <h2>{i18n.t("organizer.organizers")}</h2>

                <form onSubmit={handleSubmit(onSubmit)} className="search-users">
                        <Controller
                            name="search"
                            control={control}
                            defaultValue={query ? query : ""}
                            render={({field, fieldState}) => {
                                return (
                                    <FormControl>
                                        <TextField id="min-price-input" label={i18n.t("organizer.searchOrganizer")}
                                                   variant="standard" size="small"
                                                   error={!!fieldState.error}
                                                   value={query ? query : ""}
                                                   {...field}
                                                   InputProps={{
                                                       endAdornment: (
                                                           <InputAdornment position="end">
                                                               <SearchRoundedIcon/>
                                                           </InputAdornment>
                                                       ),
                                                   }}
                                        />
                                    </FormControl>
                                );
                            }}
                        />

                </form>


                <form className={`products-content__filter}`}>

                    <div className="products__filter__select">

                        <Controller
                            control={control}
                            defaultValue={'USERNAME_ASC'}
                            name="order"
                            render={({field: {onChange, value, name, ref}}) => {
                                const handleSelectChange = (selectedOption) => {
                                    setFirstLoad(false)
                                    onChange(selectedOption.target.value);
                                    setOrder(selectedOption.target.value);
                                };

                                return (
                                    <FormControl>
                                        <InputLabel className="order-label"
                                                    id="order-select-label">{i18n.t("filter.sortBy")}</InputLabel>
                                        <Select
                                            className="order-select"
                                            labelId="order-select-label"
                                            id="order-select"
                                            value={order ? order : "USERNAME_ASC"}
                                            onChange={handleSelectChange}
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
                <div>
                    {data && <Page data={data} aux={child} setAux={setChild}/>}
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

export default UsersContent
  