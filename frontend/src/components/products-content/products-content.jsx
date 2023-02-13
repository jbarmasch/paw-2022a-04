import useSwr from 'swr';
import EventItem from './event-item';
import ContentLoading from './content-loading';
import { useEffect, useState } from "react";
import { Controller, useForm } from "react-hook-form";
import { Link, useLocation, useHistory } from 'react-router-dom'
import queryString from 'query-string'
import { server } from "../../utils/server";
import { parseLink } from "../../utils/pages";
import i18n from '../../i18n'
import Pagination from '@mui/material/Pagination';
import Select, { SelectChangeEvent } from '@mui/material/Select';
import FormControlLabel from '@mui/material/FormControlLabel';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import MenuItem from '@mui/material/MenuItem';
import FormGroup from '@mui/material/FormGroup';
import Checkbox from '@mui/material/Checkbox';
import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';
import FormHelperText from '@mui/material/FormHelperText';
import { useFindPath } from '../header'

const fetcher = (...args) => fetch(...args).then((res) => res.json())

const fetcherHeaders = (...args) => fetch(...args).then((res) => {
    if (res.status == 200) {
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

function Page({ data, aux, setAux }) {
    if (!data) return <ContentLoading />
    data = data.data

    Promise.resolve(data).then((x) => {
        setAux(x)
        console.log(x)
    })

    return (
        <>
            {aux &&
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
                        />
                    ))}
                </section>
            }
        </>
    );
}

const EventsContent = () => {
    const history = useHistory()
    const path = useFindPath();

    const { search } = useLocation()
    const values = queryString.parse(search)

    const [pageIndex, setPageIndex] = useState(1);
    const [orderProductsOpen, setOrderProductsOpen] = useState(false);
    const [filtersOpen, setFiltersOpen] = useState(true);
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

    const getFilters = () => {
        let filters = ""
        // console.log(tagsArr)
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
        // console.log(locationsArr)
        if (locationsArr?.length > 0) {
            let aux = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            // console.log(locationsArr)
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
        // console.log(typesArr)
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
        return filters
    }

    const { data: filters, mutate, error: errorFilters } = useSwr(
        `${server}/api/filters${getFilters()}`
        , fetcher)

    const [initialTypes, setInitialTypes] = useState([]);

    useEffect(() => {
        if (firstLoad && (values.tags || values.types || values.locations || values.soldOut || values.order || values.noTickets)) {
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
            setFirstLoad(false)
        }
    }, [values.tags, values.locations, values.types, values.order, values.soldOut, values.noTickets, firstLoad])

    useEffect(() => {
        if (!firstLoad && (typesArr || locationsArr || tagsArr || order || soldOut || noTickets)) {
            console.log("ENTRO")
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
            if (noTickets) {
                query = `${query}&noTickets=${noTickets}`
            }
            if (values?.search) {
                query = `${query}&search=${values.search}`
            }
            let url = `/events?page=1${query}`
            console.log("PATH" + path + search)
            let oldPath = path + search
            if (oldPath == url) {
                return
            }
            history.push(url)
        }
    }, [typesArr, locationsArr, tagsArr, order, soldOut, noTickets, firstLoad])

    const { register, handleSubmit, control, watch, formState: { errors } } = useForm();

    let filtersStr = ""
    if (values.page || values.search || values.types || values.locations || values.tags || values.order || values.soldOut || values.noTickets) {
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
    }

    let links

    const { data: dataEvents, error: errorEvents } = useSwr(`${server}/api/events${filtersStr}`, fetcherHeaders)

    if (errorFilters || errorEvents) return <p>No data</p>
    if (!filters || !dataEvents) return <ContentLoading />

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

    const addQueryParams = () => { }

    const handlePageChange = (e, page) => {
        setPageIndex(page)
        console.log(search)
        let str
        if (search.includes("page")) {
            str = search.replace(new RegExp("[0-9]"), page)
        } else {
            str = `/events?page=${page}`
        }
        history.replace(str)
    }

    const style = {
        control: base => ({
            ...base,
            border: "revert",
            background: "transparent",
            boxShadow: "1px 1px transparent",
            height: "42px",
        })
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
        <section className="products-content">
            <div className="event-page">
                <form className="event-filter">
                    {/* <div className="products-content__intro">
                         <h2>{i18n.t("filters")}</h2> 
    </div>*/}
                    <Controller
                        name="location"
                        rules={{ required: i18n.t('fieldRequired') }}
                        control={control}
                        defaultValue={location}
                        render={({ field: { onChange }, fieldState }) => {
                            let arry = []
                            values?.locations?.split(",").map((x) => {
                                let cto = locationList.find((c) => {
                                    return c.value == x
                                })
                                if (cto) {
                                    arry.push(cto)
                                }
                            });
                            const currentSelection = arry

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
                                <FormControl className="filter-input">
                                    <Autocomplete
                                        multiple
                                        disablePortal
                                        id="location-autocomplete"
                                        options={locationList}
                                        value={currentSelection}
                                        onChange={handleSelectChange}
                                        noOptionsText={i18n.t("autocompleteNoOptions")}
                                        isOptionEqualToValue={(option, value) => { return option.value === value.value }}
                                        renderInput={(params) => <TextField {...params} label={i18n.t("create.location")} error={!!fieldState.error} />}
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

                    <Controller
                        control={control}
                        defaultValue={''}
                        name="types"
                        render={({ field: { onChange, value, name, ref } }) => {
                            const currentSelection = (value) => {
                                let arr = values?.types?.split(",")
                                if (arr) {
                                    return !!arr.find((c) => {
                                        return c == value
                                    })
                                } else {
                                    return false
                                }
                            }

                            const handleSelectChange = (e, selectedOption) => {
                                setFirstLoad(false)
                                onChange(selectedOption);
                                setType(e.target.value);
                                console.log(typesArr)
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
                                        {typeList && typeList.length > 0 && <h3 className="filter-subtitle">{i18n.t("filter.types")}</h3>}
                                        {typeList &&
                                            typeList.map(
                                                (x) => (
                                                    <div className="filter-checkbox" key={x.value}>
                                                        <FormControlLabel sx={{ margin: 0 }} control={<Checkbox sx={{ padding: "3px" }} value={x.value} checked={currentSelection(x.value)} />} label={x.label + " (" + x.amount + ")"} onChange={handleSelectChange} />
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
                        render={({ field: { onChange, value, name, ref } }) => {
                            const currentSelection = (value) => {
                                let arr = values?.tags?.split(",")
                                if (arr) {
                                    return !!arr.find((c) => {
                                        return c == value
                                    })
                                } else {
                                    return false
                                }
                            }

                            const handleSelectChange = (e, selectedOption) => {
                                setFirstLoad(false)
                                onChange(selectedOption);
                                console.log(e.target.value)
                                console.log(selectedOption)
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
                                        {tagList && tagList.length > 0 && <h3 className="filter-subtitle">{i18n.t("filter.tags")}</h3>}
                                        {tagList &&
                                            tagList.map(
                                                (x) => (
                                                    <div className="filter-checkbox" key={x.value}>
                                                        <FormControlLabel sx={{ margin: 0 }} control={<Checkbox sx={{ padding: "3px" }} value={x.value} checked={currentSelection(x.value)} />} label={x.label + " (" + x.amount + ")"} onChange={handleSelectChange} />
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
                                    render={({ field: { onChange, value, name, ref } }) => {
                                        console.log(soldOut)
                                        const currentSelection = soldOut ? soldOut === "true" : false
                                        console.log(currentSelection)

                                        const handleSelectChange = (e, selectedOption) => {
                                            setFirstLoad(false)
                                            onChange(selectedOption);
                                            setSoldOut(String(selectedOption));
                                        };

                                        return (
                                            <div className="filter-checkbox">
                                                <FormControlLabel sx={{ margin: 0 }} control={<Checkbox sx={{ padding: "3px" }} value={soldOut} checked={currentSelection} />} label={i18n.t("filter.soldOut") + " (" + soldOutCount + ")"} onChange={handleSelectChange} />
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
                                    render={({ field: { onChange, value, name, ref } }) => {
                                        // const currentSelection = noTickets ? noTickets === value : false
                                        const currentSelection = noTickets ? noTickets === "true" : false

                                        const handleTicketChange = (e, selectedOption) => {
                                            setFirstLoad(false)
                                            onChange(selectedOption);
                                            setNoTickets(String(selectedOption));
                                        };

                                        return (
                                            <div className="filter-checkbox">
                                                <FormControlLabel sx={{ margin: 0 }} control={<Checkbox sx={{ padding: "3px" }} value={noTickets} checked={currentSelection} />} label={i18n.t("filter.noTickets") + " (" + noTicketsCount + ")"} onChange={handleTicketChange} />
                                            </div>
                                        );
                                    }}
                                />
                            }


                        </FormGroup>
                        }
                </form>

                <div>
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
                                    render={({ field: { onChange, value, name, ref } }) => {
                                        const currentSelection = orderList.find(
                                            (c) => c.value === value
                                        );

                                        const handleSelectChange = (selectedOption) => {
                                            setFirstLoad(false)
                                            onChange(selectedOption.target.value);
                                            setOrder(selectedOption.target.value);
                                        };

                                        return (
                                            <FormControl>
                                                <InputLabel className="order-label" id="order-select-label">{i18n.t("filter.sortBy")}</InputLabel>
                                                <Select
                                                    className="order-select"
                                                    labelId="order-select-label"
                                                    id="order-select"
                                                    value={order ? order : "DATE_ASC"}
                                                    onChange={handleSelectChange}
                                                    input={<OutlinedInput className="order-label" label={i18n.t("filter.sortBy")} />}
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
                            {dataEvents && <Page data={dataEvents} aux={child} setAux={setChild} />}
                        </div>
                        <div className="pagination">
                            <Pagination count={Number(links && links.last ? links.last?.page : 0)} showFirstButton showLastButton page={values?.page ? Number(values?.page) : pageIndex} onChange={handlePageChange} />
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
};

export default EventsContent;
