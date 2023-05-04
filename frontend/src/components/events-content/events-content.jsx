import useSwr from 'swr';
import EventItem from './event-item';
import NoEventsContent from './no-events-content';
import ContentLoading from './content-loading';
import {useEffect, useState} from "react";
import {Controller, useForm} from "react-hook-form";
import {useLocation, useHistory} from 'react-router-dom'
import queryString from 'query-string'
import {server} from "../../utils/server";
import {parseLink} from "../../utils/pages";
import i18n from '../../i18n'
import Pagination from '@mui/material/Pagination';
import Select from '@mui/material/Select';
import FormControlLabel from '@mui/material/FormControlLabel';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import MenuItem from '@mui/material/MenuItem';
import FormGroup from '@mui/material/FormGroup';
import Checkbox from '@mui/material/Checkbox';
import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';
import InputAdornment from '@mui/material/InputAdornment';
import FormHelperText from '@mui/material/FormHelperText';
import Button from '@mui/material/Button';
import {useFindPath} from '../header'

const fetcher = (...args) => fetch(...args, {
    headers: {
        "Accept-Language": i18n.language
    }
}).then((res) => res.json())

const fetcherHeaders = (...args) => fetch(...args, {
    headers: {
        "Accept-Language": i18n.language
    }
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

function Page({data, aux, setAux}) {
    if (!data) return <ContentLoading/>
    data = data.data

    Promise.resolve(data).then((x) => {
        setAux(x)
    })

    return (
        <>
            {(aux && aux.length > 0 &&
                <section className="event-list">
                    {aux.map((item) => (
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
                    ))}
                </section>) ||
                <NoEventsContent/>
            }
        </>
    );
}

const EventsContent = () => {
    const history = useHistory()
    const path = useFindPath();

    const {search} = useLocation()
    const values = queryString.parse(search)

    const [pageIndex, setPageIndex] = useState(1);
    const [orderProductsOpen, setOrderProductsOpen] = useState(false);
    const [location, setLocation] = useState([]);
    const [tag, setTag] = useState([]);
    const [type, setType] = useState([]);
    const [firstLoad, setFirstLoad] = useState(true)
    const [typesArr, setTypesArr] = useState();
    const [locationsArr, setLocationsArr] = useState();
    const [tagsArr, setTagsArr] = useState();
    const [child, setChild] = useState();
    const [order, setOrder] = useState();
    const [soldOut, setSoldOut] = useState();
    const [noTickets, setNoTickets] = useState();
    const [userId, setUserId] = useState();
    const [minPrice, setMinPrice] = useState();
    const [maxPrice, setMaxPrice] = useState();

    const getFilters = () => {
        let filters = ""
        if (tagsArr?.length > 0) {
            let aux = '?'
            let auxi = tagsArr
            if (!(auxi.constructor === Array)) {
                if (auxi.includes(",")) {
                    auxi = auxi.split(",")
                    auxi.forEach((x) => {
                        filters = `${filters}${aux}tags=${x}`
                        aux = '&'
                    })
                } else {
                    filters = `${filters}${aux}tags=${auxi}`
                }
            } else {
                auxi.forEach((x) => {
                    filters = `${filters}${aux}tags=${x}`
                    aux = '&'
                })
            }
        }
        if (locationsArr?.length > 0) {
            let aux = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            let auxi = locationsArr
            if (!(auxi.constructor === Array)) {
                if (auxi.includes(",")) {
                    auxi = auxi.split(",")
                    auxi.forEach((x) => {
                        filters = `${filters}${aux}locations=${x}`
                        aux = '&'
                    })
                } else {
                    filters = `${filters}${aux}locations=${auxi}`
                }
            } else {
                auxi.forEach((x) => {
                    filters = `${filters}${aux}locations=${x}`
                    aux = '&'
                })
            }
        }
        if (typesArr?.length > 0) {
            let aux = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            let auxi = typesArr
            if (!(auxi.constructor === Array)) {
                if (auxi.includes(",")) {
                    auxi = auxi.split(",")
                    auxi.forEach((x) => {
                        filters = `${filters}${aux}types=${x}`
                        aux = '&'
                    })
                } else {
                    filters = `${filters}${aux}types=${auxi}`
                }
            } else {
                auxi.forEach((x) => {
                    filters = `${filters}${aux}types=${x}`
                    aux = '&'
                })
            }
        }
        if (values.search) {
            let aux = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            filters = `${filters}${aux}search=${values.search}`
        }
        if (values.minPrice) {
            let aux = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            filters = `${filters}${aux}minPrice=${values.minPrice}`
        }
        if (values.maxPrice) {
            let aux = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            filters = `${filters}${aux}maxPrice=${values.maxPrice}`
        }
        if (values.order) {
            let aux = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            filters = `${filters}${aux}order=${values.order}`
        }
        if (values.soldOut) {
            let aux = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            filters = `${filters}${aux}soldOut=${values.soldOut}`
        }
        if (values.noTickets) {
            let aux = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            filters = `${filters}${aux}noTickets=${values.noTickets}`
        }
        if (values.userId) {
            let aux = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            filters = `${filters}${aux}userId=${values.userId}`
        }
        let aux = '?'
        if (filters.length > 0) {
            aux = '&'
        }
        filters = `${filters}${aux}locale=${i18n.language}`
        return filters
    }

    const {data: filters, error: errorFilters} = useSwr(
        `${server}/api/filters${getFilters()}`
        , fetcher)

    useEffect(() => {
        if ((values.tags || values.types || values.locations || values.soldOut || values.order || values.noTickets || values.userId || values.minPrice || values.maxPrice)) {
            if (values.tags && values.tags.constructor !== Array) {
                setTagsArr(values?.tags.split(","))
            } else {
                setTagsArr(values?.tags)
            }
            if (values.locations && values.locations.constructor !== Array) {
                setLocationsArr(values?.locations.split(","))
            } else {
                setLocationsArr(values?.locations)
            }
            if (values.types && values.types.constructor !== Array) {
                setTypesArr(values?.types.split(","))
            } else {
                setTypesArr(values?.types)
            }
            setOrder(values?.order)
            setSoldOut(values?.soldOut)
            setNoTickets(values?.noTickets)
            setUserId(values?.userId)
            setMinPrice(values?.minPrice)
            setMaxPrice(values?.maxPrice)
            setFirstLoad(false)
        }
    }, [values.tags, values.locations, values.types, values.order, values.soldOut, values.noTickets, values.userId, values.minPrice, values.maxPrice, firstLoad])

    useEffect(() => {
        if (!firstLoad && (typesArr || locationsArr || tagsArr || order || soldOut || noTickets || userId || minPrice || maxPrice)) {
            setPageIndex(1)
            let query = ""
            if (typesArr?.length > 0) {
                query = `${query}&types=`
                if (typesArr.constructor === Array) {
                    query = query + typesArr.join(",")
                } else if (typesArr) {
                    query = query + [typesArr]
                }
            }
            if (locationsArr?.length > 0) {
                query = `${query}&locations=`
                if (locationsArr.constructor === Array) {
                    query = query + locationsArr.join(",")
                } else {
                    query = query + [locationsArr]
                }
            }
            if (tagsArr?.length > 0) {
                query = `${query}&tags=`
                if (tagsArr.constructor === Array) {
                    query = query + tagsArr.join(",")
                } else {
                    query = query + [tagsArr]
                }
            }
            if (order?.length > 0) {
                query = `${query}&order=${order}`
            }
            if (soldOut) {
                query = `${query}&soldOut=${soldOut}`
            }
            console.log(noTickets)
            if (noTickets) {
                query = `${query}&noTickets=${noTickets}`
            }
            if (values?.search) {
                query = `${query}&search=${values.search}`
            }
            if (values?.userId) {
                query = `${query}&userId=${values.userId}`
            }
            if (minPrice) {
                query = `${query}&minPrice=${minPrice}`
            }
            if (maxPrice) {
                query = `${query}&maxPrice=${maxPrice}`
            }
            let url = `/events?page=1${query}`
            let oldPath = path + search
            if (oldPath === url) {
                return
            }
            history.push(url)
        }
    }, [typesArr, locationsArr, tagsArr, order, soldOut, noTickets, minPrice, maxPrice, firstLoad])

    const {handleSubmit, control, getValues, formState: {errors}} = useForm();

    let filtersStr = ""
    if (values.page || values.search || values.types || values.locations || values.tags || values.order || values.soldOut || values.noTickets || values.userId || values.minPrice || values.maxPrice) {
        if (values.page) {
            filtersStr = `?page=${values.page}`
        } else {
            filtersStr = `?page=1`
        }
        if (values.search) {
            filtersStr = `${filtersStr}&search=${values.search}`
        }
        if (values.types) {
            values.types.split(",").forEach((x) => {
                filtersStr = `${filtersStr}&types=${x}`
            })
        }
        if (values.locations) {
            values.locations.split(",").forEach((x) => {
                filtersStr = `${filtersStr}&locations=${x}`
            })
        }
        if (values.tags) {
            values.tags.split(",").forEach((x) => {
                filtersStr = `${filtersStr}&tags=${x}`
            })
        }
        if (values.order) {
            filtersStr = `${filtersStr}&order=${values.order}`
        }
        if (values.soldOut) {
            filtersStr = `${filtersStr}&soldOut=${values.soldOut}`
        }
        if (values.noTickets) {
            filtersStr = `${filtersStr}&noTickets=${values.noTickets}`
        }
        if (values.userId) {
            filtersStr = `${filtersStr}&userId=${values.userId}`
        }
        if (values.minPrice) {
            filtersStr = `${filtersStr}&minPrice=${values.minPrice}`
        }
        if (values.maxPrice) {
            filtersStr = `${filtersStr}&maxPrice=${values.maxPrice}`
        }
    }

    let links

    const {data: dataEvents, error: errorEvents} = useSwr(`${server}/api/events${filtersStr}`, fetcherHeaders)

    if (errorFilters || errorEvents) {
        history.push("/404");
        return
    }

    if (!filters || !dataEvents) return <ContentLoading/>

    links = parseLink(dataEvents.headers.get("Link"))

    let locationList = []
    filters.locations.entry.forEach(x => locationList.push({
        value: x.key.id,
        label: x.key.name + " (" + x.value + ")",
    }))

    let tagList = []
    let tagListAux = []
    filters.tags.entry.forEach(x => {
        tagList.push({
            value: x.key.id,
            label: x.key.name,
            amount: x.value
        })
        tagListAux.push(x.key.id)
    })

    let typeList = []
    let typeListAux = []
    filters.types.entry.forEach(x => {
        typeList.push({
            value: x.key.id,
            label: x.key.name,
            amount: x.value
        })
        typeListAux.push(x.key.id)
    })
    let soldOutCount = filters.soldOut;
    let noTicketsCount = filters.noTickets;

    const handlePageChange = (e, page) => {
        setPageIndex(page)
        let str
        if (search.includes("page")) {
            str = search.replace(new RegExp("[0-9]"), page)
        } else {
            str = `/events?page=${page}`
        }
        history.replace(str)
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

    const onSubmit = (data) => {
        setFirstLoad(false);
        setMaxPrice(getValues("maxPrice"));
        setMinPrice(getValues("minPrice"))
    }

    const clearFilters = () => {
        history.replace("/events?page=1")
        setNoTickets(undefined)
        setMinPrice(undefined)
        setMaxPrice(undefined)
        setTagsArr(undefined)
        setLocationsArr(undefined)
        setTypesArr(undefined)
        setSoldOut(undefined)
        setUserId(undefined)
    }

    return (
        <section className="products-content">
            <div className="event-page">
                <div className="event-filter">
                    <form>
                        <Controller
                            name="location"
                            control={control}
                            defaultValue={location}
                            render={({field: {onChange}, fieldState}) => {
                                let arrAux = []
                                values?.locations?.split(",").forEach((x) => {
                                    let cto = locationList.find((c) => {
                                        return c.value === Number(x)
                                    })
                                    if (cto) {
                                        arrAux.push(cto)
                                    }
                                });
                                const currentSelection = arrAux

                                const handleSelectChange = (event, selectedOption) => {
                                    setFirstLoad(false)
                                    onChange(selectedOption);
                                    setLocation(selectedOption);
                                    let aux = []
                                    selectedOption.forEach((x) => {
                                        aux.push(x.value)
                                    })
                                    setLocationsArr(aux);
                                };

                                return (
                                    <>
                                        {locationList && locationList.length > 0 &&
                                            <FormControl className="filter-input">
                                                <Autocomplete
                                                    multiple
                                                    disablePortal
                                                    id="location-autocomplete"
                                                    options={locationList}
                                                    value={currentSelection}
                                                    onChange={handleSelectChange}
                                                    noOptionsText={i18n.t("autocompleteNoOptions")}
                                                    isOptionEqualToValue={(option, value) => {
                                                        return option.value === value.value
                                                    }}
                                                    renderInput={(params) => <TextField {...params}
                                                                                        label={i18n.t("filter.locations")}
                                                                                        error={!!fieldState.error}/>}
                                                />

                                                {fieldState.error ? (
                                                    <FormHelperText error>
                                                        {fieldState.error?.message}
                                                    </FormHelperText>
                                                ) : null}
                                            </FormControl>
                                        }
                                    </>
                                );
                            }}
                        />

                        <Controller
                            control={control}
                            defaultValue={''}
                            name="types"
                            render={({field: {onChange}}) => {
                                const currentSelection = (value) => {
                                    let arr = values?.types?.split(",")
                                    if (arr) {
                                        return !!arr.find((c) => {
                                            return Number(c) === value
                                        })
                                    } else {
                                        return false
                                    }
                                }

                                const handleSelectChange = (e, selectedOption) => {
                                    setFirstLoad(false)
                                    onChange(selectedOption);
                                    setType(e.target.value);
                                    if (selectedOption) {
                                        if (typesArr) {
                                            setTypesArr((array) => [...array, e.target.value]);
                                        } else {
                                            setTypesArr([e.target.value])
                                        }
                                    } else {
                                        if (typesArr) {
                                            if (typesArr.constructor === Array) {
                                                setTypesArr(typesArr.filter(item => {
                                                    return item !== e.target.value && typeListAux.includes(Number(item))
                                                }));
                                            } else {
                                                setTypesArr([])
                                            }
                                        }
                                    }
                                };

                                return (
                                    <>
                                        <FormGroup>
                                            {typeList && typeList.length > 0 &&
                                                <h3 className="filter-subtitle">{i18n.t("filter.types")}</h3>}
                                            {typeList &&
                                                typeList.map(
                                                    (x) => (
                                                        <div className="filter-checkbox" key={x.value}>
                                                            <FormControlLabel sx={{margin: 0}}
                                                                              control={<Checkbox sx={{padding: "3px"}}
                                                                                                 value={x.value}
                                                                                                 checked={currentSelection(x.value)}/>}
                                                                              label={x.label + " (" + x.amount + ")"}
                                                                              onChange={handleSelectChange}/>
                                                        </div>
                                                    ))}
                                        </FormGroup>
                                    </>
                                );
                            }}
                        />


                        <Controller
                            control={control}
                            defaultValue={''}
                            name="tags"
                            render={({field: {onChange, value, name, ref}}) => {
                                const currentSelection = (value) => {
                                    let arr = values?.tags?.split(",")
                                    if (arr) {
                                        return !!arr.find((c) => {
                                            return Number(c) === value
                                        })
                                    } else {
                                        return false
                                    }
                                }

                                const handleSelectChange = (e, selectedOption) => {
                                    setFirstLoad(false)
                                    onChange(selectedOption);
                                    setTag(e.target.value);
                                    if (selectedOption) {
                                        if (tagsArr) {
                                            setTagsArr((array) => [...array, e.target.value]);
                                        } else {
                                            setTagsArr([e.target.value])
                                        }
                                    } else {
                                        if (tagsArr) {
                                            if (tagsArr.constructor === Array) {
                                                setTagsArr(tagsArr.filter(item => {
                                                    return item !== e.target.value && tagListAux.includes(Number(item))
                                                }));
                                            } else {
                                                setTagsArr([])
                                            }
                                        }
                                    }
                                };

                                return (
                                    <>
                                        <FormGroup>
                                            {tagList && tagList.length > 0 &&
                                                <h3 className="filter-subtitle">{i18n.t("filter.tags")}</h3>}
                                            {tagList &&
                                                tagList.map(
                                                    (x) => (
                                                        <div className="filter-checkbox" key={x.value}>
                                                            <FormControlLabel sx={{margin: 0}}
                                                                              control={<Checkbox sx={{padding: "3px"}}
                                                                                                 value={x.value}
                                                                                                 checked={currentSelection(x.value)}/>}
                                                                              label={x.label + " (" + x.amount + ")"}
                                                                              onChange={handleSelectChange}/>
                                                        </div>
                                                    ))}
                                        </FormGroup>
                                    </>
                                );
                            }}
                        />
                        {(noTicketsCount > 0 || soldOutCount > 0) &&
                            <FormGroup>
                                <h3 className="filter-subtitle">{i18n.t("filter.advancedOptions")}</h3>

                                {soldOutCount > 0 &&
                                    <Controller
                                        control={control}
                                        defaultValue={''}
                                        name="soldout"
                                        render={({field: {onChange}}) => {
                                            const currentSelection = values?.soldOut === "true"

                                            const handleSelectChange = (e, selectedOption) => {
                                                setFirstLoad(false)
                                                onChange(selectedOption);
                                                setSoldOut(String(selectedOption));
                                            };

                                            return (
                                                <div className="filter-checkbox">
                                                    <FormControlLabel sx={{margin: 0}}
                                                                      control={<Checkbox sx={{padding: "3px"}}
                                                                                         value={soldOut}
                                                                                         checked={currentSelection}/>}
                                                                      label={i18n.t("filter.soldOut") + " (" + soldOutCount + ")"}
                                                                      onChange={handleSelectChange}/>
                                                </div>
                                            );
                                        }}
                                    />
                                }
                                {noTicketsCount > 0 &&
                                    <Controller
                                        control={control}
                                        defaultValue={''}
                                        name="ticketless"
                                        render={({field: {onChange}}) => {
                                            const currentSelection = values?.noTickets === "true"

                                            const handleTicketChange = (e, selectedOption) => {
                                                setFirstLoad(false)
                                                onChange(selectedOption);
                                                setNoTickets(String(selectedOption));
                                            };

                                            return (
                                                <div className="filter-checkbox">
                                                    <FormControlLabel sx={{margin: 0}}
                                                                      control={<Checkbox sx={{padding: "3px"}}
                                                                                         value={noTickets}
                                                                                         checked={currentSelection}/>}
                                                                      label={i18n.t("filter.noTickets") + " (" + noTicketsCount + ")"}
                                                                      onChange={handleTicketChange}/>
                                                </div>
                                            );
                                        }}
                                    />
                                }


                            </FormGroup>
                        }
                    </form>


                    <form onSubmit={handleSubmit(onSubmit)}>
                        {((minPrice || maxPrice) || (links.last?.page && links.last?.page > 0)) &&
                            <>
                        <h3 className="filter-subtitle">{i18n.t("filter.price")}</h3>

                        <div className="filter-horizontal">
                            <Controller
                                name="minPrice"
                                rules={{
                                    validate: {
                                        min: (x) => {
                                            return x >= 0 || i18n.t("filter.minPriceError")
                                        }
                                    }
                                }}
                                control={control}
                                defaultValue={minPrice ? minPrice : ""}
                                render={({field, fieldState}) => {
                                    return (
                                        <FormControl sx={{width: 120}}>
                                            <TextField id="min-price-input" label={i18n.t("filter.minPrice")}
                                                       variant="outlined" size="small"
                                                       error={!!fieldState.error}
                                                       value={minPrice ? minPrice : ""}
                                                       {...field}
                                                       InputProps={{
                                                           startAdornment: <InputAdornment
                                                               position="start">$</InputAdornment>,
                                                           inputMode: 'numeric', pattern: '[0-9]*'
                                                       }}
                                            />
                                            {fieldState.error ? (
                                                <FormHelperText error>
                                                    {fieldState.error?.message}
                                                </FormHelperText>
                                            ) : null}
                                        </FormControl>
                                    );
                                }}
                            />
                            <span>-</span>
                            <Controller
                                name="maxPrice"
                                control={control}
                                rules={{
                                    validate: {
                                        range: () => {
                                            let minPrice = getValues("minPrice")
                                            let maxPrice = getValues("maxPrice")
                                            if (minPrice && maxPrice) {
                                                return minPrice < maxPrice || i18n.t("filter.rangePriceError")
                                            }
                                            return true;
                                        },
                                        min: (x) => {
                                            return x >= 0 || i18n.t("filter.maxPriceError")
                                        }
                                    }
                                }}
                                defaultValue={maxPrice ? maxPrice : ""}
                                render={({field, fieldState}) => {
                                    return (
                                        <FormControl sx={{width: 120}}>
                                            <TextField id="min-price-input" label={i18n.t("filter.maxPrice")}
                                                       variant="outlined" size="small"
                                                       error={!!fieldState.error}
                                                       value={maxPrice ? maxPrice : ""}
                                                       {...field}
                                                       InputProps={{
                                                           startAdornment: <InputAdornment
                                                               position="start">$</InputAdornment>,
                                                           inputMode: 'numeric', pattern: '[0-9]*'
                                                       }}
                                            />
                                            {fieldState.error ? (
                                                <FormHelperText error>
                                                    {fieldState.error?.message}
                                                </FormHelperText>
                                            ) : null}
                                        </FormControl>
                                    );
                                }}
                            />

                        </div>
                            </>
                        }
                        <div className="space-around">
                            <Button onClick={clearFilters} variant="text">{i18n.t("filter.clear")}</Button>
                            <Button type="submit" variant="contained">{i18n.t("filter.apply")}</Button>
                        </div>
                    </form>
                </div>
                <div className="full-width">
                    <div className="products-content__intro">
                        <h2>{i18n.t("filter.title")}</h2>
                        <button type="button" onClick={() => setOrderProductsOpen(!orderProductsOpen)}
                                className="products-filter-btn"><i className="icon-filters"></i></button>
                        <form className={`products-content__filter ${orderProductsOpen ? 'products-order-open' : ''}`}>
                            <div className="products__filter__select">
                                <Controller
                                    control={control}
                                    defaultValue={'DATE_ASC'}
                                    name="order"
                                    render={({field: {onChange}}) => {
                                        const handleSelectChange = (selectedOption) => {
                                            setFirstLoad(false)
                                            onChange(selectedOption.target.value);
                                            setOrder(selectedOption.target.value);
                                        };

                                        return (
                                            <>
                                            {links.last?.page && links.last?.page > 0 &&
                                            <FormControl>
                                                <InputLabel className="order-label"
                                                            id="order-select-label">{i18n.t("filter.sortBy")}</InputLabel>
                                                <Select
                                                    className="order-select"
                                                    labelId="order-select-label"
                                                    id="order-select"
                                                    value={order ? order : "DATE_ASC"}
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
                                            }
                                            </>
                                        );
                                    }}
                                />
                            </div>
                        </form>
                    </div>
                    <div>
                        <div
                            className="event-discovery flex w-full flex-1 flex-col items-center justify-center px-20 text-center">
                            {dataEvents && <Page data={dataEvents} aux={child} setAux={setChild}/>}
                        </div>
                        {links.last?.page && links.last?.page > 1 &&
                            <div className="pagination">
                                <Pagination count={Number(links && links.last ? links.last?.page : 0)} showFirstButton
                                            showLastButton page={values?.page ? Number(values?.page) : pageIndex}
                                            onChange={handlePageChange}/>
                            </div>
                        }
                    </div>
                </div>
            </div>
        </section>
    );
};

export default EventsContent;
