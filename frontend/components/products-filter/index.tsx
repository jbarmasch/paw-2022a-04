import {useState} from 'react';
import Checkbox from './form-builder/checkbox';
import CheckboxColor from './form-builder/checkbox-color';
import Slider from 'rc-slider';

// data
import useSWRImmutable from "swr/immutable";
import {server} from "../../utils/server";
import Select from "react-select";
import * as React from "react";
import {Controller, useForm} from "react-hook-form";
import {useIntl} from "react-intl";
import i18n from "../../utils/i18n"


const {createSliderWithTooltip} = Slider;
const Range = createSliderWithTooltip(Slider.Range);

// @ts-ignore
const fetcher = (...args) => fetch(...args).then((res) => res.json())

const ProductsFilter = () => {
    
    const [filtersOpen, setFiltersOpen] = useState(false);

    const { data : locations, error : errorLocation } = useSWRImmutable(`${server}/api/locations`, fetcher)
    const [location, setLocation] = useState<string[]>([]);
    const { data : tags, error : errorTags } = useSWRImmutable(`${server}/api/tags`, fetcher)
    const [tag, setTag] = useState([]);
    const { data : types, error : errorType } = useSWRImmutable(`${server}/api/types`, fetcher)
    const [type, setType] = useState([]);

    const { register, handleSubmit, control, watch, formState: { errors } } = useForm();

    if (errorLocation || errorTags || errorType) return <p>Loading...</p>
    if (!locations || !tags || !types) return <p>No data</p>

    let locationList: any[] = []
    locations.forEach(x => locationList.push({
        value: x.id,
        label: x.name
    }))

    let tagList: any[] = []
    tags.forEach(x => tagList.push({
        value: x.id,
        label: x.name
    }))

    let typeList: any[] = []
    types.forEach(x => typeList.push({
        value: x.id,
        label: x.name
    }))

    const addQueryParams = () => {
        // query params changes
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
                            const currentSelection = typeList.find(
                                (c) => c.value === value
                            );

                            const handleSelectChange = (selectedOption) => {
                                onChange(selectedOption);
                                // console.log(selectedOption)
                                setLocation(selectedOption);
                            };

                            return (
                                <>
                                    <label hidden htmlFor="type-input">Select type: </label>
                                    <Select
                                        className="form__input"
                                        id="type-input"
                                        value={currentSelection}
                                        name={name}
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
                        name="type"
                        render={({ field: { onChange, value, name, ref } }) => {
                            const currentSelection = locationList.find(
                                (c) => c.value === value
                            );

                            const handleSelectChange = (selectedOption) => {
                                onChange(selectedOption);
                                // console.log(selectedOption)
                                setLocation(selectedOption);
                            };

                            return (
                                <>
                                    <label hidden htmlFor="type-input">Select location: </label>
                                    <Select
                                        className="form__input"
                                        id="location-input"
                                        value={currentSelection}
                                        name={name}
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
                        name="type"
                        render={({ field: { onChange, value, name, ref } }) => {
                            const currentSelection = tagList.find(
                                (c) => c.value === value
                            );

                            const handleSelectChange = (selectedOption) => {
                                onChange(selectedOption);
                                // console.log(selectedOption)
                                setLocation(selectedOption);
                            };

                            return (
                                <>
                                    <label hidden htmlFor="type-input">Select tags: </label>
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

                <button type="submit" className="btn btn-submit btn--rounded btn--yellow">Apply</button>
            </div>
        </form>
    )
}

export default ProductsFilter
  