import useSwr from 'swr';
import EventItem from './event-item';
import ContentLoading from './content-loading';
import {useEffect, useState} from "react";
import Select from "react-select";
import {Controller, useForm} from "react-hook-form";
import {Link, useLocation, useHistory} from 'react-router-dom'
import queryString from 'query-string'
import {server} from "../../utils/server";
import i18n from '../../i18n'

const fetcher = (...args) => fetch(...args).then((res) => res.json())

function Page(index) {
    const { search } = useLocation()
    const values = queryString.parse(search)

    let data
    let error

    let filters = ""
    if (values.search || values.types || values.locations || values.tags) {
        if (values.search) {
            filters = `&search=${values.search}`
        }
        if (values.types) {
            values.types.split(",").forEach((x) => {
                filters = `${filters}&types=${x}`
            })
        }
        if (values.locations) {
            values.locations.split(",").forEach((x) => {
                filters = `${filters}&locations=${x}`
            })
        }
        if (values.tags) {
            values.tags.split(",").forEach((x) => {
                filters = `${filters}&tags=${x}`
            })
        }
    }

    console.log(index.index)
    const { data: dataAux, error: errorAux } = useSwr(`${server}/api/events?page=${index.index}${filters}`, fetcher)
    data = dataAux
    error = errorAux

    if (error) return <p>No data</p>
    if (!data) return <ContentLoading/>

    return (
        <>
            {data &&
                <section className="event-list">
                    {data.map((item) => (
                        <EventItem
                            id={item.id}
                            name={item.name}
                            minPrice={item.minPrice}
                            color={item.color}
                            currentPrice={item.minPrice}
                            key={item.id}
                            image={item.image}
                        />
                    ))}
                </section>
            }
        </>
    );
}

const EventsContent = () => {
    const history = useHistory()

    const { search } = useLocation()
    const values = queryString.parse(search)

    const [pageIndex, setPageIndex] = useState(1);
    const [orderProductsOpen, setOrderProductsOpen] = useState(false);
    const [filtersOpen, setFiltersOpen] = useState(true);
    const [location, setLocation] = useState([]);
    const [tag, setTag] = useState([]);
    const [firstLoad, setFirstLoad] = useState(true)
    const [typesArr, setTypesArr] = useState();
    const [locationsArr, setLocationsArr] = useState();
    const [tagsArr, setTagsArr] = useState();

    const getFilters = () => {
        let filters = ""
        console.log(tagsArr)
        if (tagsArr) {
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
        console.log(locationsArr)
        if (locationsArr) {
            let aux = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            console.log(locationsArr)
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
        console.log(typesArr)
        if (typesArr) {
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
        return filters
    }

    const { data : filters, mutate, error : errorFilters } = useSwr(
         `${server}/api/filters${getFilters()}`
        , fetcher)

    const [initialTypes, setInitialTypes] = useState([]);

    useEffect(() => {
        if (firstLoad && (values.tags || values.types || values.locations)) {
            setTagsArr(values?.tags)
            setLocationsArr(values?.locations)
            setTypesArr(values?.types)
            setFirstLoad(false)
        }
    }, [values.tags, values.locations, values.types, firstLoad])

    useEffect(() => {
        if (!firstLoad && (typesArr || locationsArr || tagsArr)) {
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
            console.log(query)
            let url = `/events?page=1${query}`
            history.push(url)
        }
    }, [typesArr, locationsArr, tagsArr, firstLoad])

    const { register, handleSubmit, control, watch, formState: { errors } } = useForm();

    if (errorFilters) return <p>No data</p>
    if (!filters) return <p>Loading...</p>

    let locationList = []
    filters.locations.entry.forEach(x => locationList.push({
        value: x.key.id,
        label: x.key.name + " (" + x.value + ")"
    }))

    let tagList = []
    filters.tags.entry.forEach(x => tagList.push({
        value: x.key.id,
        label: x.key.name + " (" + x.value + ")"
    }))

    let typeList = []
    filters.types.entry.forEach(x => typeList.push({
        value: x.key.id,
        label: x.key.name + " (" + x.value + ")"
    }))

    const addQueryParams = () => {}

    const style = {
        control: base => ({
            ...base,
            border: "revert",
            background: "transparent",
            boxShadow: "1px 1px transparent",
            height: "42px",
        })
    }
    return (
        <section className="products-content">
            <form className="products-filter" onChange={addQueryParams}>
                <button type="button"
                        onClick={() => setFiltersOpen(!filtersOpen)}
                        className={`products-filter__menu-btn ${filtersOpen ? 'products-filter__menu-btn--active' : ''}`}>
                    Add Filter <i className="icon-down-open"></i>
                </button>

                <div className={`products-filter__wrapper ${filtersOpen ? 'products-filter__wrapper--open' : ''}`}>
                    <div className="products-filter__block">
                        <Controller
                            control={control}
                            name="type"
                            render={({ field: { onChange, value, name, ref } }) => {
                                let arry = []
                                values?.types?.split(",").map((x) => {
                                    let cto = typeList.find((c) => {
                                        console.log(x)
                                        console.log(c)
                                        return c.value == x
                                    })
                                    if (cto) {
                                        arry.push(cto)
                                    }
                                });
                                const currentSelection = arry
                                // setTypesArr(router?.query?.types)

                                const handleSelectChange = (selectedOption) => {
                                    setFirstLoad(false)
                                    console.log("TENGOOO" + selectedOption)
                                    onChange(selectedOption);
                                    setLocation(selectedOption);
                                    let aux = []
                                    selectedOption.forEach((x) => {
                                        aux.push(x.value)
                                    })
                                    setTypesArr(aux);
                                };

                                return (
                                    <>
                                        <label hidden htmlFor="type-input">Select type: </label>
                                        <Select
                                            className="form__input"
                                            id="type-input"
                                            value={currentSelection}
                                            name={name}
                                            isMulti
                                            options={typeList}
                                            onChange={handleSelectChange}
                                            placeholder={i18n.t("create.type")}
                                            styles={style}
                                        />
                                    </>
                                );
                            }}
                            rules={{
                                required: true
                            }}
                        />
                        {/*<button type="button">Product type</button>*/}
                        {/*<div className="products-filter__block__content">*/}
                        {/*    {productsTypes.map(type => (*/}
                        {/*        <Checkbox*/}
                        {/*            key={type.id}*/}
                        {/*            name="product-type"*/}
                        {/*            label={type.name}*/}
                        {/*        />*/}
                        {/*    ))}*/}
                        {/*</div>*/}
                    </div>

                    <div className="products-filter__block">
                        <Controller
                            control={control}
                            name="location"
                            render={({ field: { onChange, value, name, ref } }) => {
                                let arrx = []
                                values?.locations?.split(",").map((x) => {
                                    let cto = locationList.find((c) => {
                                        return c.value == x
                                    })
                                    if (cto) {
                                        arrx.push(cto)
                                    }
                                });
                                const currentSelection = arrx
                                // setLocationsArr(router?.query?.locations)

                                const handleSelectChange = (selectedOption) => {
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
                                        <label hidden htmlFor="location-input">Select location: </label>
                                        <Select
                                            className="form__input"
                                            id="location-input"
                                            value={currentSelection}
                                            name={name}
                                            isMulti
                                            options={locationList}
                                            onChange={handleSelectChange}
                                            placeholder={i18n.t("create.location")}
                                            styles={style}
                                        />
                                    </>
                                );
                            }}
                            rules={{
                                required: true
                            }}
                        />
                        {/*<button type="button">Price</button>*/}
                        {/*<div className="products-filter__block__content">*/}
                        {/*    <Range min={0} max={20} defaultValue={[3, 10]} tipFormatter={value => `${value}%`}/>*/}
                        {/*</div>*/}
                    </div>

                    <div className="products-filter__block">
                        <Controller
                            control={control}
                            name="tags"
                            render={({ field: { onChange, value, name, ref } }) => {
                                let arr = []
                                values?.tags?.split(",").map((x) => {
                                    let cto = tagList.find((c) => {
                                        return c.value == x
                                    })
                                    if (cto) {
                                        arr.push(cto)
                                    }
                                });
                                const currentSelection = arr
                                // setTagsArr(router?.query?.tags)

                                const handleSelectChange = (selectedOption) => {
                                    setFirstLoad(false)
                                    onChange(selectedOption);
                                    setTag(selectedOption);
                                    let aux = []
                                    selectedOption.forEach((x) => {
                                        aux.push(x.value)
                                    })
                                    setTagsArr(aux);
                                };

                                return (
                                    <>
                                        <label hidden htmlFor="tag-input">Select tags: </label>
                                        <Select
                                            className="form__input"
                                            id="tag-input"
                                            isMulti
                                            value={currentSelection}
                                            name={name}
                                            options={tagList}
                                            onChange={handleSelectChange}
                                            placeholder={i18n.t("create.tags")}
                                            styles={style}
                                        />
                                    </>
                                );
                            }}
                            rules={{
                                required: true
                            }}
                        />
                        {/*<button type="button">Size</button>*/}
                        {/*<div className="products-filter__block__content checkbox-square-wrapper">*/}
                        {/*    {productsSizes.map(type => (*/}
                        {/*        <Checkbox*/}
                        {/*            type="square"*/}
                        {/*            key={type.id}*/}
                        {/*            name="product-size"*/}
                        {/*            label={type.label}/>*/}
                        {/*    ))}*/}
                        {/*</div>*/}
                    </div>

                    <div className="products-filter__block">
                        {/*<button type="button">Color</button>*/}
                        {/*<div className="products-filter__block__content">*/}
                        {/*    <div className="checkbox-color-wrapper">*/}
                        {/*        {productsColors.map(type => (*/}
                        {/*            <CheckboxColor key={type.id} valueName={type.color} name="product-color"*/}
                        {/*                           color={type.color}/>*/}
                        {/*        ))}*/}
                        {/*    </div>*/}
                        {/*</div>*/}
                    </div>

                    {/* <button type="submit" className="btn btn-submit btn--rounded btn--yellow">Apply</button> */}
                    {/* <button type="submit" className="">Apply</button> */}
                </div>
            </form>

            <div className="products-content__intro">
                <h2>Men's Tops <span>(133)</span></h2>
                <button type="button" onClick={() => setOrderProductsOpen(!orderProductsOpen)}
                        className="products-filter-btn"><i className="icon-filters"></i></button>
                <form className={`products-content__filter ${orderProductsOpen ? 'products-order-open' : ''}`}>
                    <div className="products__filter__select">
                        <h4>Sort by: </h4>
                        <div className="select-wrapper">
                            <select>
                                <option>{i18n.t("filter.username")}</option>
                                <option>{i18n.t("filter.rating")}</option>
                            </select>
                        </div>
                    </div>
                    <div className="products__filter__select">
                        <h4>Order: </h4>
                        <div className="select-wrapper">
                            <select>
                                <option>{i18n.t("filter.ascending")}</option>
                                <option>{i18n.t("filter.descending")}</option>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div>
                <div className="event-discovery flex w-full flex-1 flex-col items-center justify-center px-20 text-center">
                    <Page index={pageIndex}/>
                    <div style={{ display: 'none' }}><Page index={pageIndex + 1}/></div>
                </div>
                <button className="pag-button" onClick={() => setPageIndex(pageIndex <= 1 ? pageIndex : pageIndex - 1)}>&laquo;</button>
                <button className="pag-button" onClick={() => setPageIndex(pageIndex + 1)}>&raquo;</button>
            </div>
        </section>
    );
};

export default EventsContent;
