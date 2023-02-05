import useSwr from 'swr';
import ProductItem from '../../product-item';
import ProductsLoading from './loading';
import {ProductTypeList} from 'types';
import {server} from '../../../utils/server';
import {useEffect, useState} from "react";
import Select from "react-select";
import {useRouter} from "next/router";
import Link from '../../../components/Link'
import {Controller, useForm} from "react-hook-form";

const fetcher = (...args) => fetch(...args).then((res) => res.json())

// const {createSliderWithTooltip} = Slider;
// const Range = createSliderWithTooltip(Slider.Range);

function Page({index, t}) {
    const router = useRouter();

    // useEffect(() => {
    //     if (router.query.search) {
    //         console.log(router.query.search)
    //     }
    // }, [router.query.search])

    let data
    let error

    if (router.query.search || router.query.types || router.query.locations || router.query.tags) {
        let filters = ""
        if (router.query.search) {
            filters = `&search=${router.query.search}`
        }
        if (router.query.types) {
            router.query.types.split(",").forEach((x) => {
                filters = `${filters}&types=${x}`
            })
        }
        if (router.query.locations) {
            router.query.locations.split(",").forEach((x) => {
                filters = `${filters}&locations=${x}`
            })
        }
        if (router.query.tags) {
            router.query.tags.split(",").forEach((x) => {
                filters = `${filters}&tags=${x}`
            })
        }
        const { data: dataAux, error: errorAux } = useSwr(filters ? `${server}/api/events?page=${index}${filters}` : null, fetcher)
        data = dataAux
        error = errorAux
    } else {
        const { data: dataAux, error: errorAux } = useSwr(`${server}/api/events?page=${index}`, fetcher)
        data = dataAux
        error = errorAux
    }

    if (error) return <p>No data</p>
    if (!data) return <ProductsLoading/>

    return (
        <>
            {!data &&
                <ProductsLoading/>
            }

            {data &&
                <section className="products-list">
                    {data.map((item: ProductTypeList) => (
                        <ProductItem
                            id={item.id}
                            name={item.name}
                            minPrice={item.minPrice}
                            color={item.color}
                            currentPrice={item.minPrice}
                            key={item.id}
                            image={item.image}
                            t={t}
                        />
                    ))}
                </section>
            }
        </>
    );
}

const ProductsContent = ({t}) => {
    const router = useRouter();

    const [pageIndex, setPageIndex] = useState(1);
    const [orderProductsOpen, setOrderProductsOpen] = useState(false);
    // const { register, handleSubmit, control, watch, formState: { errors } } = useForm();

    const [filtersOpen, setFiltersOpen] = useState(true);

    // const { data : locations, error : errorLocation } = useSWRImmutable(`${server}/api/locations`, fetcher)
    const [location, setLocation] = useState<string[]>([]);
    // const { data : tags, error : errorTags } = useSWRImmutable(`${server}/api/tags`, fetcher)
    const [tag, setTag] = useState([]);
    // const { data : types, error : errorType } = useSWRImmutable(`${server}/api/types`, fetcher)
    // const [type, setType] = useState([]);

    const [firstLoad, setFirstLoad] = useState(true)

    // const [typesArr, setTypesArr] = useState<string[]>([]);
    // const [typesArr, setTypesArr] = useState<string[]>(router.query.types ? router.query.types.split(",") : []);
    // const [locationsArr, setLocationsArr] = useState<string[]>(router.query.locations ? router.query.locations.split(",") : []);
    // const [tagsArr, setTagsArr] = useState<string[]>(router.query.tags ? router.query.tags.split(",") : []);
    const [typesArr, setTypesArr] = useState<string[]>();
    const [locationsArr, setLocationsArr] = useState<string[]>();
    const [tagsArr, setTagsArr] = useState<string[]>();

    const getFilters = () => {
        let filters = ""
        console.log(tagsArr)
        if (tagsArr) {
            let aux: string = '?'
            let auxi = tagsArr
            if (auxi.constructor === Array) { 
                auxi.forEach((x) => {
                    filters = `${filters}${aux}tags=${x}`
                    aux = '&'
                })
            } else {
                filters = `${filters}${aux}tags=${auxi}`
            }
        }
        console.log(locationsArr)
        if (locationsArr) {
            let aux: string = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            console.log(locationsArr)
            let auxi = locationsArr
            if (auxi.constructor === Array) {
                auxi.forEach((x) => {
                    filters = `${filters}${aux}locations=${x}`
                    aux = '&'
                })
            } else {
                filters = `${filters}${aux}locations=${auxi}`
            }
        }
        console.log(typesArr)
        if (typesArr) {
            let aux: string = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            let auxi = typesArr
            if (auxi.constructor === Array) {
                auxi.forEach((x) => {
                    filters = `${filters}${aux}types=${x}`
                    aux = '&'
                })
            } else {
                filters = `${filters}${aux}types=${auxi}`
            }
        }
        if (router.query.search) {
            let aux: string = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            filters = `${filters}${aux}search=${router.query.search}`
        }
        if (router.query.minPrice) {
            let aux: string = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            filters = `${filters}${aux}minPrice=${router.query.minPrice}`
        }
        if (router.query.maxPrice) {
            let aux: string = '?'
            if (filters.length > 0) {
                aux = '&'
            }
            filters = `${filters}${aux}maxPrice=${router.query.maxPrice}`
        }
        return filters
    }

    const { data : filters, mutate, error : errorFilters } = useSwr(
         `${server}/api/filters${getFilters()}`
        , fetcher)

    const [initialTypes, setInitialTypes] = useState<string[]>([]);

    useEffect(() => {
        if (firstLoad && (router.query.tags || router.query.types || router.query.locations)) {
            setTagsArr(router?.query?.tags)
            setLocationsArr(router?.query?.locations)
            setTypesArr(router?.query?.types)
            console.log(router?.query?.types)
            console.log(router?.query?.locations)
            console.log(router?.query?.tags)
            setFirstLoad(false)
        }
    }, [router.query.tags, router.query.locations, router.query.types, firstLoad])

    useEffect(() => {
            // mutate()
        console.log(typesArr)
        console.log(locationsArr)
        console.log(tagsArr)
        console.log(firstLoad)
        if (!firstLoad && (typesArr || locationsArr || tagsArr)) {
            console.log("HOLA?")
            console.log(t.language)
            console.log(typesArr)
            console.log(locationsArr)
            console.log(tagsArr)
            // if (firstLoad) {
                // setFirstLoad(false)
                // return
            // }
            let query = { 
                    page: 1,
                    locale: "en"
                    // locale: t.language
                }
            if (typesArr) {
                console.log("entro")
                if (typesArr.constructor === Array) {
                    query.types = typesArr.join(",")
                } else {
                    query.types = [typesArr]
                }
            } 
            if (locationsArr) {
                console.log("entro")
                if (locationsArr.constructor === Array) {
                    query.locations = locationsArr.join(",")
                } else {
                    query.locations = [locationsArr]
                }
            } 
            if (tagsArr) {
                console.log("entro")
                if (tagsArr.constructor === Array) {
                    query.tags = tagsArr.join(",")
                } else {
                    query.tags = [tagsArr]
                }
            } 
            console.log(query)
        router.push({
            query
        },
          undefined, 
          { shallow: true }
          )
        }
    }, [typesArr, locationsArr, tagsArr, firstLoad])

    const { register, handleSubmit, control, watch, formState: { errors } } = useForm();

    // if (errorLocation || errorTags || errorType) return <p>Loading...</p>
    // if (!locations || !tags || !types) return <p>No data</p>
    if (errorFilters) return <p>No data</p>
    if (!filters) return <p>Loading...</p>

    let locationList: any[] = []
    filters.locations.entry.forEach(x => locationList.push({
        value: x.key.id,
        label: x.key.name
    }))

    let tagList: any[] = []
    filters.tags.entry.forEach(x => tagList.push({
        value: x.key.id,
        label: x.key.name
    }))

    let typeList: any[] = []
    filters.types.entry.forEach(x => typeList.push({
        value: x.key.id,
        label: x.key.name
    }))

    const addQueryParams = () => {
        // query params changes
        // console.log(location)
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

    return (
        <>
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
                                router?.query?.types?.split(",").map((x: number) => {
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
                                    let aux: string[] = []
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
                                            placeholder={t("create.type")}
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
                                router?.query?.locations?.split(",").map((x: number) => {
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
                                    let aux: string[] = []
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
                                            placeholder={t("create.location")}
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
                                router?.query?.tags?.split(",").map((x: number) => {
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
                                    let aux: string[] = []
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
                                            placeholder={t("create.tags")}
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
                                <option>{t("filter.username")}</option>
                                <option>{t("filter.rating")}</option>
                            </select>
                        </div>
                    </div>
                    <div className="products__filter__select">
                        <h4>Order: </h4>
                        <div className="select-wrapper">
                            <select>
                                <option>{t("filter.ascending")}</option>
                                <option>{t("filter.descending")}</option>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div>
                <div className="event-discovery flex w-full flex-1 flex-col items-center justify-center px-20 text-center">
                    <Page index={pageIndex} t={t}/>
                    <div style={{ display: 'none' }}><Page index={pageIndex + 1} t={t}/></div>
                </div>
                <button className="pag-button" onClick={() => setPageIndex(pageIndex <= 1 ? pageIndex : pageIndex - 1)}>&laquo;</button>
                <button className="pag-button" onClick={() => setPageIndex(pageIndex + 1)}>&raquo;</button>
            </div>
        </>

    );
};

export default ProductsContent
