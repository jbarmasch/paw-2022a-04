import {useState} from 'react';
import Layout from '../layout';
import {server, fetcher} from '../../utils/server';
import {getPrice} from '../../utils/price';

import useSwr from "swr";
import ProductItemLoading from "../events-content/item-loading";
import Select from '@mui/material/Select';
import {useForm, Controller} from "react-hook-form";
import * as React from "react";
import i18n from '../../i18n'
import {Link, useHistory, useLocation} from 'react-router-dom'
import useFindPath from '../header'
import {useEffect} from "react";
import {checkLogin} from '../../utils/auth'
import Paper from '@mui/material/Paper';
import Chip from '@mui/material/Chip';
import Button from '@mui/material/Button';
import SimilarEvents from '../events-content/similar-events'
import RecommendedEvents from '../events-content/recommended-events'
import {styled} from '@mui/material/styles';
import Table from '@mui/material/Table';
import TableRow from '@mui/material/TableRow';
import TableCell, {tableCellClasses} from '@mui/material/TableCell';
import TableHead from '@mui/material/TableHead';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormHelperText from '@mui/material/FormHelperText';
import {LoadingPage} from "../../utils/loadingPage";
import {ParseDateTime} from "../events-content/event-item";

const Event = (props) => {
    const prevLocation = useLocation();

    const [showBlock, setShowBlock] = useState('description');

    // const fetcher = (url) => fetch(url).then((res) => res.json());

    const {register, handleSubmit, control, watch, formState: {errors}} = useForm();

    const history = useHistory();
    let path = useFindPath();

    let accessToken;
    let refreshToken;
    let userId;

    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
        refreshToken = localStorage.getItem("Refresh-Token");
        userId = localStorage.getItem("User-ID");
    }

    const {
        data: event,
        error: errorData
    } = useSwr(props.match.params.id ? `${server}/api/events/${props.match.params.id}` : null, fetcher)
    const {data: tickets, error: errorTickets} = useSwr(event ? `${event.tickets}` : null, fetcher)
    const {data: aux, error: error} = useSwr(event ? `${event.image}` : null, fetcher)
    const {data: organizer, error: errorOrganizer} = useSwr(event ? `${event.organizer}` : null, fetcher)
    const [location, setLocation] = useState(tickets ? new Array(tickets.length) : []);

    const {
        data: prevBooking,
        isLoading,
        error: errorBooking
    } = useSwr(props.match.params.id ? `${server}/api/users/${userId}/booking?eventId=${props.match.params.id}` : null, fetcher, {shouldRetryOnError: false})

    if (error || errorData || errorOrganizer) return <p>{i18n.t("noData")}</p>
    if (!aux || !event || !organizer || isLoading) return <LoadingPage/>

    const getArray = (max, left, booked) => {
        max = max - (booked ? booked : 0);
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

    const StyledTableRow = styled(TableRow)(({theme}) => ({
        '&:nth-of-type(even)': {
            backgroundColor: theme.palette.action.hover,
        },
        '&:last-child td, &:last-child th': {
            border: 0,
        },
    }));

    const StyledTableCell = styled(TableCell)(({theme}) => ({
        [`&.${tableCellClasses.head}`]: {
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

        if (json.status !== 201) {
            history.push("/500")
            return;
        }

        if (json.status === 201) {
            history.push({
                pathname: `/thank-you`,
                state: {
                    booking: json.headers.get("Location"),
                    event: event
                }
            });
        }
    }

    return (
        <Layout>

            <section className="product-single">
                <div className="container my-event-page">
                    <div className="my-event-content">
                        <div className="contain">
                            <img className="event-image" src={`data:image/png;base64,${aux.image}`} alt="Event"/>
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
                                <li>
                                    <h4>{i18n.t("event.location")}</h4>
                                    <span>{event.location.name}</span>
                                </li>
                                <li>
                                    <h4>{i18n.t("event.type")}</h4>
                                    <span>{event.type.name}</span>
                                </li>
                                <li>
                                    <h4>{i18n.t("event.date")}</h4>
                                    <span>{ParseDateTime(event.date)}</span>
                                </li>
                                <li>
                                    <h4>{i18n.t("event.tags")}</h4>
                                    {event.tags && event.tags.map((t) =>
                                            <Chip label={t.name} key={t.id}
                                                  onClick={() => history.push(`/events?page=1&tags=${t.id}`)}/>
                                    )}
                                </li>
                                <li>
                                    <h4>{i18n.t("event.organizer")}</h4>
                                    <Link className="underline"
                                          to={`/events?page=1&userId=${organizer.id}`}>{organizer.username}</Link>
                                </li>
                                {event.minAge &&
                                    <li>
                                        <h4>{i18n.t("event.minAge")}</h4>
                                        <span>{i18n.t("event.minAgeText")} {event.minAge}</span>
                                    </li>
                                }
                            </ul>
                        </Paper>
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
                                                        <input {...register("ticketId" + item.ticketId, {required: true})}
                                                               className={"casper"} value={item.ticketId}/>
                                                        <Controller
                                                            name={`ticket-${item.ticketId}`}
                                                            rules={{
                                                                validate: {
                                                                    min: (x) => {
                                                                        x > 0 || i18n.t("event.bookingError")
                                                                    }
                                                                }
                                                            }}
                                                            control={control}
                                                            defaultValue={''}
                                                            render={({
                                                                         field: {onChange, value, name, ref},
                                                                         fieldState
                                                                     }) => {
                                                                const list = getArray(item.maxPerUser, item.qty - item.booked, prevBooking?.ticketBookings.find((x) => x.ticket.ticketId === item.ticketId)?.qty)
                                                                const currentSelection = value
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
                                                                    if (value === -1) {
                                                                        history.push("/500")
                                                                        return;
                                                                    }
                                                                    location[value] = selectedOption.target.value
                                                                    setLocation(location);
                                                                };

                                                                return (
                                                                    <FormControl className={"qty-select"}
                                                                                 disabled={Number(userId) === organizer.id || event.soldOut}>
                                                                        <InputLabel id="select-qty-label"
                                                                                    error={!!fieldState.error}>{i18n.t("event.selectQty")}</InputLabel>
                                                                        <Select
                                                                            id="age-select"
                                                                            label={i18n.t("event.selectQty")}
                                                                            labelId="select-qty-label"
                                                                            value={currentSelection}
                                                                            error={!!fieldState.error}
                                                                            onChange={handleSelectChange}
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
                                                    </StyledTableCell>
                                                </StyledTableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                            </div>
                        )}

                        <div className="center">
                            <Button disabled={Number(userId) === organizer.id || event.soldOut} color="secondary"
                                    variant="contained" type="submit">{i18n.t("book")}</Button>
                        </div>
                    </form>
                </div>
            </section>
            <RecommendedEvents id={event.id}/>
            <SimilarEvents id={event.id}/>
        </Layout>
    );
}

export default Event
