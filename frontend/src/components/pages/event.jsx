import { useState } from 'react';
import Footer from '../footer';
import Layout from '../layout';
// import ProductsFeatured from '../products-featured';
// import Description from '../product-single/description';
import { server } from '../../utils/server';
import { getPrice } from '../../utils/price';

import useSwr from "swr";
import ProductItemLoading from "../products-content/item-loading";
// import Content from "../product-single/content";
import Select from '@mui/material/Select';
// import {postData} from "../../utils/services";
import { useForm, Controller } from "react-hook-form";
import * as React from "react";
import i18n from '../../i18n'
import { Link, useHistory, useLocation } from 'react-router-dom'
import useFindPath from '../header'
import { useEffect } from "react";
import { checkLogin } from '../../utils/auth'
import Paper from '@mui/material/Paper';
import Chip from '@mui/material/Chip';
import Button from '@mui/material/Button';
import SimilarEvents from '../products-content/similar-events'
import RecommendedEvents from '../products-content/recommended-events'
import { styled } from '@mui/material/styles';
import Table from '@mui/material/Table';
import TableRow from '@mui/material/TableRow';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableHead from '@mui/material/TableHead';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormHelperText from '@mui/material/FormHelperText';

const Event = (props) => {
    const prevLocation = useLocation();

    const [showBlock, setShowBlock] = useState('description');

    const fetcher = (url) => fetch(url).then((res) => res.json());

    const { register, handleSubmit, control, watch, formState: { errors } } = useForm();

    const history = useHistory();
    let path = useFindPath();


    let accessToken;
    let refreshToken;
    let userId;

    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
        refreshToken = localStorage.getItem("Refresh-Token");
        userId = localStorage.getItem("User-ID");
        // pendingBook = localStorage.getItem("PendingBook");
    }

    // useEffect(() => {
    //     if (!pendingBook || !accessToken) {
    //         return
    //     }

    //     const fetchData = async (accessToken, pendingBook) => {
    //         const response = await fetch(`${server}/api/events/${props.match.params.id}/bookings`, {
    //             method: 'POST',
    //             headers: {
    //                 'Content-Type': 'application/json',
    //                 'Authorization': `Bearer ${accessToken}`
    //             },
    //             body: pendingBook
    //         });

    //         let json = await response; 
    //     }

    //     fetchData(accessToken, pendingBook)
    // }, [pendingBook, accessToken])

    const { data: event, error: errorData } = useSwr(props.match.params.id ? `${server}/api/events/${props.match.params.id}` : null, fetcher)
    const { data: tickets, error: errorTickets } = useSwr(event ? `${event.tickets}` : null, fetcher)
    const { data: aux, error: error } = useSwr(event ? `${event.image}` : null, fetcher)
    const { data: organizer, error: errorOrganizer } = useSwr(event ? `${event.organizer}` : null, fetcher)
    const [location, setLocation] = useState(tickets ? new Array(tickets.length) : []);

    if (error || errorData || errorOrganizer) return <p>{i18n.t("noData")}</p>
    if (!aux || !event || !organizer) return <ProductItemLoading />

    const style = {
        control: base => ({
            ...base,
            border: "revert",
            background: "transparent",
            boxShadow: "1px 1px transparent",
            height: "42px",
        })
    }

    const getArray = (max, left) => {
        max = max <= left ? max : left
        let start = 1;
        let ages = [];
        while (start <= max) {
            ages.push({
                value: start,
                label: start
            })
            start++;
        }
        return ages;
    }

    const StyledTableRow = styled(TableRow)(({ theme }) => ({
        '&:nth-of-type(even)': {
            backgroundColor: theme.palette.action.hover,
        },
        '&:last-child td, &:last-child th': {
            border: 0,
        },
    }));

    const StyledTableCell = styled(TableCell)(({ theme }) => ({
        [`&.${tableCellClasses.head}`]: {
            // backgroundColor: "#343434",
            // backgroundColor: theme.palette.secondary.main,
            backgroundColor: "#cacaca",
            color: theme.palette.common.black,
            fontSize: "14px",
            fontWeight: "500"
        },
        [`&.${tableCellClasses.body}`]: {
            fontSize: 14,
        },
    }));

    const onSubmit = async (data) => {
        let isLogged = await checkLogin(accessToken, refreshToken)
        console.log(isLogged)

        console.log(data)
        delete data.location
        let i = 0
        let auxi = {
            bookings: []
        }

        for (let x in data) {
            console.log(x)
            console.log(location)
            console.log(data[x])
            let qtyAux = location[i++]
            if (qtyAux) {
                auxi.bookings.push({
                    ticketId: data[x],
                    qty: qtyAux
                })
            }
        }

        if (!isLogged) {
            // localStorage.setItem('PendingBook', JSON.stringify(auxi))
            history.push(`/login?redirectTo=${prevLocation.pathname}`);
            return
        }

        const response = await fetch(`${server}/api/events/${props.match.params.id}/bookings`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(auxi)
        });

        let json = await response;

        if (json.status == 201) {
            history.push({
                pathname: `/thank-you`,
                state: {
                    booking: json.headers.get("Location"),
                    eventId: props.match.params.id
                }
            });
        }
    }


    console.log(userId)
    console.log(event)
    // console.log("MAYORES DE" + event.minAge)
    // console.log("MAYORES DE" + event)

    return (
        <Layout>

            <section className="product-single">
                <div className="container my-event-page">
                    <div className="my-event-content">
                        <div className="contain">
                            <img className="event-image" src={`data:image/png;base64,${aux.image}`} alt="Event" />
                            {!!event.soldOut && <span className="event-image-sold-out">{i18n.t("event.soldOut")}</span>}
                        </div>
                        <Paper className="event-info" elevation={2}>
                            <ul className="event-info-content">
                                <li>
                                    <h4>{i18n.t("event.name")}</h4>
                                    <span>{event.name}</span>
                                </li>
                                <li>
                                    <h4>{i18n.t("event.description")}</h4>
                                    <span>{event.description}</span>
                                </li>
                                {/* <li>
                                    <h4>{i18n.t("event.minPrice")}</h4>
                                    <span>{event.minPrice}</span>
    </li> */}
                                <li>
                                    <h4>{i18n.t("event.type")}</h4>
                                    <span>{event.type.name}</span>
                                </li>
                                <li>
                                    <h4>{i18n.t("event.location")}</h4>
                                    <span>{event.location.name}</span>
                                </li>
                                <li>
                                    <h4>{i18n.t("event.tags")}</h4>
                                    {event.tags && event.tags.map((t) => 
                                        // <Link className="pointer" to={`/events?tags=${t.id}`}>
                                            <Chip label={t.name} key={t.id} onClick={() => history.push(`/events?page=1&tags=${t.id}`)}/>
                                        // </Link>
                                    )}
                                    {/* <span>{event.tags}</span> */}
                                </li>
                                <li>
                                    <h4>{i18n.t("event.organizer")}</h4>
                                    <Link className="underline" to={`/events?page=1&userId=${organizer.id}`}>{organizer.username}</Link>
                                </li>
                                {event.minAge &&
                                    <li>
                                        <h4>{i18n.t("event.minAge")}</h4>
                                        <span>{i18n.t("event.minAgeText")} {event.minAge}</span>
                                    </li>
                                }
                                </ul>
                        </Paper>
                        {/* <div>&nbsp;</div> */}
                        {/*<Image src={`data:image/png;base64,${aux.image}`} className="product-gallery__image" layout="raw" width={"400px"} height={"400px"}/>*/}
                        {/* <Content product={event}/> */}
                        </div>
                        <form className="form" onSubmit={handleSubmit(onSubmit)}>
                            {tickets && (
                                <div>
                                    <TableContainer component={Paper}>
                                    <Table className="edit-table" size="small">
                                        <TableHead>
                                            <StyledTableRow>
                                                <StyledTableCell>{i18n.t("event.ticket")}</StyledTableCell>
                                                <StyledTableCell>{i18n.t("event.price")}</StyledTableCell>
                                                <StyledTableCell>{i18n.t("event.quantity")}</StyledTableCell>
                                            </StyledTableRow>
                                        </TableHead>
                                        <TableBody>
                                        {tickets.map((item) => (
                                            <StyledTableRow key={item.ticketId}>
                                                    <StyledTableCell>{item.ticketName}</StyledTableCell>
                                                    <StyledTableCell>{getPrice(item.price, false)}</StyledTableCell>
                                                    <StyledTableCell>
                                                        <input {...register("ticketId" + item.ticketId, { required: true })} className={"casper"} value={item.ticketId} />
                                                        <Controller
                                    name={`ticket-${item.ticketId}`}
                                    rules={{
                                        required: i18n.t('fieldRequired'),
                                        validate: {
                                            min: (x) => { x > 0 || i18n.t("event.bookingError") }
                                        }
                                    }}
                                    control={control}
                                    defaultValue={''} // <---------- HERE
                                    render={({ field: { onChange, value, name, ref }, fieldState }) => {
                                        const list = getArray(item.maxPerUser, item.qty - item.booked)
                                        const currentSelection = value
                                            // list.find(
                                            //     (c) => c.value === value
                                            // );

                                        const handleSelectChange = (selectedOption) => {
                                            console.log(selectedOption.target.value)
                                            onChange(selectedOption.target.value);
                                            let value = -1
                                            for (let i = 0; i < tickets.length; i++) {
                                                if (tickets[i].ticketId === item.ticketId) {
                                                    value = i;
                                                    break;
                                                }
                                            }
                                            if (value == -1) {
                                                alert('que cambias')
                                            }
                                            location[value] = selectedOption.target.value
                                            setLocation(location);
                                        };

                                        return (
                                            <FormControl className={"min-age-select"}
                                                    disabled={userId == organizer.id || event.soldOut}>
                                                <InputLabel  id="stackoverflow-label" error={!!fieldState.error}>{i18n.t("event.selectQty")}</InputLabel>
                                                <Select
                                                    id="age-select"
                                                    label={i18n.t("event.selectQty")}
                                                    labelId="minAge-id"
                                                    value={currentSelection}
                                                    error={!!fieldState.error}
                                                    onChange={handleSelectChange}
                                                    // {...field}
                                                >
                                                    {list.map((x) => (
                                                        <MenuItem
                                                            key={x.value}
                                                            value={x.label}
                                                        >
                                                            {x.value}
                                                        </MenuItem>
                                                    ))}
                                                </Select>
                                                {fieldState.error ? (
                                                    <FormHelperText error>
                                                        {fieldState.error?.message}
                                                    </FormHelperText>
                                                ) : null}
                                            </FormControl>
                                        );
                                    }}
                                />
                                                        {/* <Controller
                                                            control={control}
                                                            name="location"
                                                            render={({ field: { onChange, value, name, ref } }) => {
                                                                const list = getArray(item.maxPerUser, item.qty - item.booked)
                                                                const currentSelection = list.find(
                                                                    (c) => c.value === value
                                                                );

                                                                const handleSelectChange = (selectedOption) => {
                                                                    onChange(selectedOption);
                                                                    let value = -1
                                                                    for (let i = 0; i < tickets.length; i++) {
                                                                        if (tickets[i].ticketId === item.ticketId) {
                                                                            value = i;
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (value == -1) {
                                                                        alert('que cambias')
                                                                    }
                                                                    location[value] = selectedOption.value
                                                                    setLocation(location);
                                                                };

                                                                return (
                                                                    <>
                                                                        <label hidden htmlFor="location-input">{i18n.t("event.selectQty")}</label>
                                                                        <Select
                                                                            id={"qty-input-" + item.ticketIdid}
                                                                            value={currentSelection}
                                                                            name={name}
                                                                            options={list}
                                                                            onChange={handleSelectChange}
                                                                            placeholder={i18n.t("event.selectQty")}
                                                                            styles={style}
                                                                        />
                                                                    </>
                                                                );
                                                            }}
                                                            rules={{
                                                                required: true
                                                            }}
                                                        />
                                                        {errors.location?.type && <span>{i18n.t("fieldRequired")}</span>} */}
                                                    </StyledTableCell>
                                                </StyledTableRow>
                                        ))}
                                        </TableBody>
                                    </Table>
                                    </TableContainer>
                                </div>
                            )}

                            {/* <button type="submit" className="btn btn--rounded btn--yellow btn-submit">{i18n.t("login.signIn")}</button> */}

                            <div className="center">
                                <Button disabled={userId == organizer.id || event.soldOut} color="secondary" variant="contained" type="submit">{i18n.t("book")}</Button>
                            </div>
                        </form>
                    </div>



                    {/*
                    <div className="product-single__info">
                        <div className="product-single__info-btns">
                            <button type="button" onClick={() => setShowBlock('description')}
                                    className={`btn btn--rounded ${showBlock === 'description' ? 'btn--active' : ''}`}>Description
                            </button>
                            <button type="button" onClick={() => setShowBlock('reviews')}
                                    className={`btn btn--rounded ${showBlock === 'reviews' ? 'btn--active' : ''}`}>Reviews
                                (2)
                            </button>
                        </div>

                    </div> */}
            </section>
            <RecommendedEvents id={event.id} />
            <SimilarEvents id={event.id} /> 
        </Layout>
    );
}

export default Event
