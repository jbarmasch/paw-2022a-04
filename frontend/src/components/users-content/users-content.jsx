import useSwr from 'swr';
import UsersLoading from './content-loading';
import {server} from '../../utils/server';
import {parseLink} from '../../utils/pages';
import UserItem from "./user-item";
import {useEffect, useState} from "react";
import Pagination from '@mui/material/Pagination';
import ContentLoading from './content-loading';
import { useHistory } from 'react-router-dom'
import { Controller, useForm } from "react-hook-form";
import i18n from '../../i18n'
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

const fetcherHeaders = (...args) => fetch(...args).then((res) => {
    return {
        headers: res.headers,
        data: res.json()
    }
})

function Page({ data, aux, setAux }) {
    if (!data) return <ContentLoading />
    data = data.data

    data.then((x) => {
        setAux(x)
    })

    return (
        <>
            {aux &&
                <section className="user-list">
                    {aux.map((item) => (
                        <UserItem
                            key={item.id}
                            id={item.id}
                            username={item.username}
                            rating={item.rating}
                            votes={item.votes}
                        />
                    ))}
                </section>
            }
        </>
    );
}

const UsersContent = () => {
    const history = useHistory()
    const [pageIndex, setPageIndex] = useState(1)
    const [orderProductsOpen, setOrderProductsOpen] = useState(false);
    const [order, setOrder] = useState("USERNAME_ASC")
    const [child, setChild] = useState();

    let links

    const { register, handleSubmit, control, watch, formState: { errors } } = useForm();

    let {data, error} = useSwr(`${server}/api/organizers?page=${pageIndex}&order=${order}`, fetcherHeaders);

    if (error) return <p>No data</p>
    // TODO: hacer loading bien
    if (!data) return <ContentLoading />

    links = parseLink(data.headers.get("Link"))

    const handlePageChange = (e, page) => {
        setPageIndex(page)
        history.replace(`/organizers?page=${page}`)
    }

    const style = {
        control: base => ({
            ...base,
            caretColor: "transparent",
            border: "revert",
            background: "transparent",
            boxShadow: "1px 1px transparent",
            height: "42px"
        })
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

    return (
        <section className="users-content products-content">
            <div className="products-content__intro">
                <h2>{i18n.t("filter.title")}</h2>
                <button type="button" onClick={() => setOrderProductsOpen(!orderProductsOpen)}
                        className="products-filter-btn"><i className="icon-filters"></i></button>
                    <form className={`products-content__filter ${orderProductsOpen ? 'products-order-open' : ''}`}>
                            <div className="products__filter__select">
                                <Controller
                                    control={control}
                                    defaultValue={'USERNAME_ASC'}
                                    name="order"
                                    render={({ field: { onChange, value, name, ref } }) => {
                                        const currentSelection = orderList.find(
                                            (c) => c.value === value
                                        );

                                        const handleSelectChange = (selectedOption) => {
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
                <div>
                    {data && <Page data={data} aux={child} setAux={setChild} />}
                </div>
                <div className="pagination">
                    <Pagination count={Number(links.last.page)} showFirstButton showLastButton page={pageIndex} onChange={handlePageChange} />
                </div>
            </div>
        </section>
    );
};

export default UsersContent
  