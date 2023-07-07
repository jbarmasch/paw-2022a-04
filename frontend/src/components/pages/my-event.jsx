import {useEffect, useMemo, useState, useRef} from 'react';
import Layout from '../layout';
import {server, fetcher} from '../../utils/server';
import useSWR from "swr";
import useSWRImmutable from "swr/immutable";
import MyEventLoading from "../../components/my-events-content/my-event-loading";
import {useForm, useFieldArray, Controller} from "react-hook-form";
import {Link, useHistory, useLocation} from 'react-router-dom'
import Paper from '@mui/material/Paper';
import Chip from '@mui/material/Chip';
import i18n from '../../i18n'
import useFindPath from '../header'
import Input from '@mui/material/Input';
import EditRoundedIcon from '@mui/icons-material/EditRounded';
import DoneRoundedIcon from '@mui/icons-material/DoneRounded';
import DeleteRoundedIcon from '@mui/icons-material/DeleteRounded';
import IconButton from '@mui/material/IconButton';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormHelperText from '@mui/material/FormHelperText';
import FormControl from '@mui/material/FormControl';
import Select, {SelectChangeEvent} from '@mui/material/Select';
import Button from '@mui/material/Button';
import Checkbox from '@mui/material/Checkbox';
import Table from '@mui/material/Table';
import TableRow from '@mui/material/TableRow';
import TableCell, {tableCellClasses} from '@mui/material/TableCell';
import TableHead from '@mui/material/TableHead';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import FormControlLabel from '@mui/material/FormControlLabel';
import Autocomplete from '@mui/material/Autocomplete';
import {DateTimePicker} from '@mui/x-date-pickers/DateTimePicker';
import {LocalizationProvider} from '@mui/x-date-pickers/LocalizationProvider';
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from 'dayjs'
import TextField from '@mui/material/TextField';
import InputAdornment from '@mui/material/InputAdornment';
import {styled} from '@mui/material/styles';
import ClearRoundedIcon from '@mui/icons-material/ClearRounded';
import AddRoundedIcon from '@mui/icons-material/AddRounded';
import 'dayjs/locale/es';
import 'dayjs/locale/en';
import {getPrice} from "../../utils/price";
import {ParseDateTime} from "../events-content/event-item"
import {LoadingPage} from "../../utils/loadingPage";
import * as React from "react";
import {useAuth} from "../../utils/useAuth";
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogTitle from '@mui/material/DialogTitle';
import utc from "dayjs/plugin/utc";
import {Alert, Snackbar} from "@mui/material";

const isEqualsJson = (oldTicket, newTicket) => {
    let oldKeys = Object.keys(oldTicket);
    let newKeys = Object.keys(newTicket);

    return oldKeys.length === newKeys.length && Object.keys(oldTicket).every(key => {
        return oldTicket[key] == newTicket[key]
    });
}
const MyEvent = (props) => {
    let {user} = useAuth()

    const history = useHistory();

    const [rowsData, setRowsData] = useState([]);
    const [edit, setEdit] = useState(false);
    const [activeMin, setActiveMin] = useState();
    const [open, setOpen] = useState(false);
    const [openSnackbar, setOpenSnackbar] = useState(false);

    const {
        data: locations,
        isLoading: locationsLoading,
    } = useSWRImmutable(`${server}/api/locations`, fetcher)
    const [location, setLocation] = useState();
    const {
        data: tags,
        isLoading: tagsLoading,
    } = useSWRImmutable(`${server}/api/tags`, fetcher)
    const [tag, setTag] = useState();
    const {
        data: types,
        isLoading: typesLoading,
    } = useSWRImmutable(`${server}/api/types`, fetcher)
    const [date, setDate] = useState(null);

    const [active, setActive] = useState(false)

    let start = 14;
    let ages = [];
    while (start <= 27) {
        ages.push({
            value: start,
            label: start
        })
        start++;
    }

    const {
        data: event,
        mutate,
        error: errorData,
        isLoading: eventLoading
    } = useSWR(props.match.params.id ? `${server}/api/events/${props.match.params.id}` : null, fetcher)
    const {
        data: eventStats,
        isLoading: statsLoading,
        error: statsError
    } = useSWR(props.match.params.id ? `${server}/api/events/${props.match.params.id}/stats` : null, fetcher)
    const {
        data: ticketStats,
        isLoading: tStatsLoading,
        error: tStatsError
    } = useSWR(props.match.params.id ? `${server}/api/events/${props.match.params.id}/ticket-stats` : null, fetcher)

    const [tickets, setTickets] = useState([]);

    useEffect(() => {
        if (event) {
            fetch(event.tickets)
                .then(r => {
                    if (r.status === 200) {
                        return r.json()
                    }
                })
                .then(d => {
                    setTickets(d)
                    setValue("tickets", tickets);
                    reset()
                })
        }
    }, [event])

    // useEffect(() => {
    //     if (edit) {
    //         console.log(edit);
    //         handleSubmit(onSubmit)
    //     }
    // }, [edit])

    const imgFetcher = (...args) => fetch(...args).then(res => res.blob())
    const {data: aux, error: error, isLoading: auxLoading} = useSWRImmutable(event ? `${event.image}` : null, imgFetcher)

    const {register, control, handleSubmit, reset, watch, setValue, getValues, formState: {errors}, setError} = useForm({
        defaultValues: {
            tickets: [{
                ticketName: "",
                price: "",
                qty: "",
                maxPerUser: "",
                starting: "",
                until: ""
            }]
        }
    });

    const {fields, append, prepend, remove, swap, move, insert} = useFieldArray(
        {
            control,
            name: "tickets"
        }
    );

    let accessToken;
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
    }

    useEffect(() => {
        setValue("tickets", tickets);
    }, [tickets]);

    if (error || errorData) {
        history.push("/404")
        return;
    }
    if (auxLoading || eventLoading || !user) {
        return <MyEventLoading/>
    }

    if (user.id != event.organizer.split("/").splice(-1)[0]) {
        history.push("/404");
        return; 
    }

    const onSubmit = async (data) => {
        if (!edit) {
            return;
        }
        // setEdit(false)
        let dateAux = new Date(data.date).toISOString().slice(0, -8)

        let obj = {
            name: data.name,
            location: data.location.value,
            type: data.type,
            date: dateAux,
        }

        if (data.description) {
            obj.description = data.description
        }

        if (data.tags) {
            obj.tags = data.tags
        }

        if (data.minAge) {
            obj.hasMinAge = true
            obj.minAge = data.minAge
        }
        let changed = false

        var utc = require('dayjs/plugin/utc')
        dayjs.extend(utc)

        console.log(event.name)
        console.log(data.name)
        console.log(event.location)
        console.log(data.location)
        console.log(event.type)
        console.log(data.type)
        console.log(event.date)
        console.log(dayjs(data.date).utc().format().slice(0, -4))
        console.log(event.description)
        console.log(data.description)
        console.log(event.tags)
        console.log(data.tags)
        console.log(event.minAge)
        console.log(data.minAge)

        if (event.name != data.name ||
            event.location != data.location.value ||
            event.type != data.type ||
            event.date != dayjs(data.date).utc().format().slice(0, -4) ||
            (data.description && event.description != data.description) ||
            (data.tags && event.tags != data.tags) ||
            (data.minAge && event.minAge != data.minAge)) {
            console.log("DALE???")
            changed = true
        }

        let errors
        let resi

        if (changed) {
            const formData = new FormData();
            formData.append('image', aux, "")
            formData.append('form', new Blob([JSON.stringify(obj)], {
                type: "application/json"
            }));

            resi = await fetch(`${server}/api/events/${props.match.params.id}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
                body: formData
            })


            // SI no hay errores, mutar
            console.log("mutate")
            // await mutate(`${server}/api/events/${props.match.params.id}`, obj)
            // await mutate({ ...event, obj })
            // mutate()

            await resi

            // TODO: podria NO SER UNA LISTA!!!!
            if (resi.status === 400) {
                errors = await resi.json()
                errors.forEach(x => {
                    let variable = String(x["path"]).split(".").slice(-1)[0]
                    switch (variable) {
                        case "name":
                            setError('name', {type: 'custom', message: x['message']});
                            break
                        case "description":
                            setError('description', {type: 'custom', message: x['message']});
                            break
                        case "location":
                            setError('location', {type: 'custom', message: x['message']});
                            break
                        case "type":
                            setError('type', {type: 'custom', message: x['message']});
                            break
                        case "tags":
                            setError('tags', {type: 'custom', message: x['message']});
                            break
                        case "date":
                            setError('date', {type: 'custom', message: x['message']});
                            break
                        case "minAge":
                            setError('minAge', {type: 'custom', message: x['message']});
                            break
                        default:
                            break;
                    }
                })
            } else if (resi.status !== 201) {
                setOpenSnackbar(true)
            } else {
                // No iria pues rompería los siguientes cambios
                // mutate()
            }
        }

        // await resi

        // if (resi.status !== 200)

        let del
        for (const x of rowsData) {
            del = await fetch(`${server}/api/tickets/${x.ticketId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
            })

            await del
            if (del.status === 204) {
                remove(x.index)
                reset()
            } else {
                setOpenSnackbar(false)
            }
        }

        let res
        let auxi = {
            tickets: []
        }

        if (data.tickets) {
            for (let d of data.tickets) {
                console.log(d)
                console.log("im")
                console.log(fields)

                let q = 0
                for (const f of fields) {
                    console.log(f)
                    if (f.name === d.name) {
                        break
                    }
                    q++
                }

                if (d.starting) {
                    d.starting = new Date(d.starting).toISOString().slice(0, -8)
                } else {
                    d.starting = ""
                }

                if (d.until) {
                    d.until = new Date(d.until).toISOString().slice(0, -8)
                } else {
                    d.until = ""
                }

                if (d.ticketId) {
                    const ticket = tickets.find(x => {
                        return x.ticketId == d.ticketId
                    })

                    const defaultValues = {
                        ticketId: ticket.ticketId,
                        self: ticket.self,
                        bookings: ticket.bookings,
                        event: ticket.event,
                        ticketName: ticket.ticketName,
                        price: ticket.price,
                        qty: ticket.qty,
                        booked: ticket.booked,
                        maxPerUser: ticket.maxPerUser,
                        starting: ticket.starting ? ticket.starting : "",
                        until: ticket.until ? ticket.until : ""
                    }

                    delete d.bookings
                    delete d.event
                    delete d.self

                    const newTicket = {...defaultValues};

                    let aux = JSON.parse(JSON.stringify(d));

                    if (!isEqualsJson(d, newTicket)) {
                        res = await fetch(`${server}/api/tickets/${d.ticketId}`, {
                            method: 'PUT',
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization': `Bearer ${accessToken}`
                            },
                            body: JSON.stringify(aux)
                        })

                        await res

                        if (res.status === 400) {
                            errors = await res.json()
                            errors.forEach(x => {
                                let variable = String(x["path"]).split(".").slice(-1)[0]
                                switch (variable) {
                                    case "ticketName":
                                        console.log(x)
                                        setError('tickets[' + q + '].ticketName', {
                                            type: 'custom',
                                            message: x['message']
                                        });
                                        break
                                    case "price":
                                        setError('tickets[' + q + '].price', {type: 'custom', message: x['message']});
                                        break
                                    case "qty":
                                        setError('tickets[' + q + '].qty', {type: 'custom', message: x['message']});
                                        break
                                    case "starting":
                                        setError('tickets[' + q + '].starting', {
                                            type: 'custom',
                                            message: x['message']
                                        });
                                        break
                                    case "until":
                                        setError('tickets[' + q + '].until', {type: 'custom', message: x['message']});
                                        break
                                    case "maxPerUser":
                                        // console.log('tickets[' + q + '].maxPerUser')
                                        setError('tickets[' + q + '].maxPerUser', {
                                            type: 'custom',
                                            message: x['message']
                                        });
                                        break
                                    default:
                                        break;
                                }
                            })
                        } else if (res.status !== 204) {
                            setOpenSnackbar(true)
                        }
                    }
                } else {
                    if (d.ticketName !== '') {
                        // auxi.tickets.push(d)

                        res = await fetch(`${server}/api/events/${props.match.params.id}/tickets`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization': `Bearer ${accessToken}`
                            },
                            body: JSON.stringify(d)
                        })

                        await res

                        if (res.status === 400) {
                            errors = await res.json()
                            // TODO: podría no ser una lista: ticketUnderflow, dateRange, eventFinished
                            //  hacer if con typeof
                            errors.forEach(x => {
                                let variable = String(x["path"]).split(".").slice(-1)[0]
                                switch (variable) {
                                    case "ticketName":
                                        console.log(x)
                                        setError('tickets[' + q + '].ticketName', {
                                            type: 'custom',
                                            message: x['message']
                                        });
                                        break
                                    case "price":
                                        setError('tickets[' + q + '].price', {type: 'custom', message: x['message']});
                                        break
                                    case "qty":
                                        setError('tickets[' + q + '].qty', {type: 'custom', message: x['message']});
                                        break
                                    case "starting":
                                        setError('tickets[' + q + '].starting', {
                                            type: 'custom',
                                            message: x['message']
                                        });
                                        break
                                    case "until":
                                        setError('tickets[' + q + '].until', {type: 'custom', message: x['message']});
                                        break
                                    case "maxPerUser":
                                        setError('tickets[' + q + '].maxPerUser', {
                                            type: 'custom',
                                            message: x['message']
                                        });
                                        break
                                    default:
                                        break;
                                }
                            })
                        } else if (res.status !== 201) {
                            setOpenSnackbar(true)
                        }
                    }
                }
            }
        }

        //     if (auxi.tickets.length !== 0) {
        //         let aux = JSON.parse(JSON.stringify(auxi));
        //
        //         res = await fetch(`${server}/api/events/${props.match.params.id}/tickets`, {
        //             method: 'POST',
        //             headers: {
        //                 'Content-Type': 'application/json',
        //                 'Authorization': `Bearer ${accessToken}`
        //             },
        //             body: JSON.stringify(aux)
        //         })
        //
        //         await res;
        //
        //         if (res.status === 400) {
        //             errors = await res.json()
        //             errors.forEach(x => {
        //                 let variable = String(x["path"]).split(".").slice(-1)[0]
        //                 console.log(variable)
        //                 switch (variable) {
        //                     case "ticketName":
        //                         console.log(x)
        //                         setError('tickets[].ticketName', {type: 'custom', message: x['message']});
        //                         break
        //                     case "price":
        //                         setError('ticketPrice', {type: 'custom', message: x['message']});
        //                         break
        //                     case "password":
        //                         setError('password', {type: 'custom', message: x['message']});
        //                         break
        //                     case "repeatPassword":
        //                         setError('repeatPassword', {type: 'custom', message: x['message']});
        //                         break
        //                     default:
        //                         break;
        //                 }
        //             })
        //         }
        //     }
        // }

        // mutate()
        if (!errors) {
            await mutate({...event, obj})
            setEdit(false)
        }
    }

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleClickActive = async (e) => {
        // console.log(e.target.value)

        let resi = await fetch(`${server}/api/events/${props.match.params.id}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify({"active": e.target.value})
        })

        await resi;
        mutate()
    }

    const deleteEvent = async () => {
        let res = await fetch(`${server}/api/events/${event.id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${accessToken}`
            },
        })
        await res;
        mutate()
        history.push("/")
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

    if (locationsLoading || tagsLoading || typesLoading) return <LoadingPage/>

    let over = new Date(event.date) < Date.now()

    if (over) {
        if (statsError || tStatsError) {
            history.push("/404")
            return;
        }
        if (statsLoading || tStatsLoading) return <LoadingPage/>
    }
    
    let locationList = []
    locations.forEach(x => locationList.push({
        value: x.id,
        label: x.name
    }))

    let tagList = []
    tags.forEach(x => tagList.push({
        value: x.id,
        label: x.name
    }))

    let typeList = []
    types.forEach(x => typeList.push({
        value: x.id,
        label: x.name
    }))

    let auxxx = {
        value: event.location.id,
        label: event.location.name,
    }

    let theTags = []
    event.tags.forEach(x => theTags.push(
        x.id
    ))

    let vertical = "top"
    let horizontal = "right"

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false)
    }

    return (
        <Layout>
            <Snackbar
                anchorOrigin={{ vertical, horizontal }}
                open={openSnackbar}
                onClose={handleCloseSnackbar}
                key={vertical + horizontal}
                autoHideDuration={15000}
            >
                <Alert severity="error" onClose={handleClose}>{i18n.t("error.api")}</Alert>
            </Snackbar>

            <section className="product-single">
                <form className="form container my-event-page" onSubmit={handleSubmit(onSubmit)}>
                    <div className="my-event-content">
                        <div className="contain">
                            <img className="event-image" src={event.image} alt="Event"/>
                            {!!event.soldOut && <span className="event-image-sold-out">{i18n.t("event.soldOut")}</span>}
                        </div>
                        <Paper className="event-info" elevation={2}>
                            <div className="event-actions">
                                {!over && 
                                (!edit ?
                                    <>
                                        <IconButton onClick={() => {setEdit(true)}}><EditRoundedIcon/></IconButton>
                                        <IconButton onClick={handleClickOpen}><DeleteRoundedIcon/></IconButton>
                                        {event.soldOut ?
                                            <Button onClick={(e) => handleClickActive(e)} value={true}>{i18n.t("event.active")}</Button>
                                        :
                                            <Button onClick={(e) => handleClickActive(e)} value={false}>{i18n.t("event.notActive")}</Button>
                                        }
                                        <Dialog
                open={open}
                onClose={handleClose}
                aria-labelledby="responsive-dialog-title"
            >
                <DialogTitle id="alert-dialog-title">
                    {i18n.t("event.sureDelete")}
                </DialogTitle>
                <DialogActions>
                    <Button onClick={handleClose}>{i18n.t("bookings.cancel")}</Button>
                    <Button onClick={deleteEvent} autoFocus>
                        {i18n.t("bookings.accept")}
                    </Button>
                </DialogActions>
            </Dialog>
                                    </> :
                                    <>
                                        {/*<IconButton onClick={() => {handleSubmit(onSubmit)}} type="submit"><DoneRoundedIcon/></IconButton>*/}
                                        {/*<IconButton onClick={() => setEdit(false)}><ClearRoundedIcon/></IconButton>*/}
                                    </>
                                )}
                            </div>
                            {!edit ?
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
                                        <h4>{i18n.t("event.date")}</h4>
                                        <span>{ParseDateTime(event.date)}</span>
                                    </li>
                                    <li>
                                        <h4>{i18n.t("event.type")}</h4>
                                        <span>{event.type.name}</span>
                                    </li>
                                    <li>
                                        <h4>{i18n.t("event.tags")}</h4>
                                        {event.tags && event.tags.map((t) =>
                                                <Chip label={t.name} key={t.id}
                                                      onClick={() => history.push(`/events?page=1&tags=${t.id}`)}/>
                                        )}
                                    </li>
                                    {event.minAge &&
                                        <li>
                                            <h4>{i18n.t("event.minAge")}</h4>
                                            <span>{i18n.t("event.minAgeText")} {event.minAge}</span>
                                        </li>
                                    }
                                </ul>
                                :
                                <ul className="event-info-content">
                                    <li>
                                        <h4>{i18n.t("event.name")}</h4>
                                        <Controller
                                            name="name"
                                            rules={{required: i18n.t('fieldRequired')}}
                                            control={control}
                                            defaultValue={event.name}
                                            render={({field, fieldState}) => {
                                                return (
                                                    <FormControl className="my-event-input">
                                                        <InputLabel className="casper"
                                                                    htmlFor="name-input">{i18n.t("event.name")}</InputLabel>
                                                        <TextField id="name-input" variant="standard"
                                                                   error={!!fieldState.error}
                                                                   {...field} />
                                                        {fieldState.error ? (
                                                            <FormHelperText error>
                                                                {fieldState.error?.message}
                                                            </FormHelperText>
                                                        ) : null}
                                                    </FormControl>
                                                );
                                            }}
                                        />
                                    </li>
                                    <li>
                                        <h4>{i18n.t("event.description")}</h4>
                                        <Controller
                                            name="description"
                                            control={control}
                                            defaultValue={event.description}
                                            render={({field, fieldState}) => {
                                                return (
                                                    <FormControl className="full-width-input">
                                                        <InputLabel className="casper"
                                                                    htmlFor="description-input">{i18n.t("event.description")}</InputLabel>
                                                        <TextField id="description-input" variant="standard"
                                                                   multiline
                                                                   maxRows={3}
                                                                   {...field} />
                                                    </FormControl>
                                                );
                                            }}
                                        />
                                    </li>
                                    <li>
                                        <h4>{i18n.t("event.location")}</h4>
                                        <Controller
                                            name="location"
                                            rules={{required: i18n.t('fieldRequired')}}
                                            control={control}
                                            defaultValue={auxxx}
                                            render={({field: {onChange}, fieldState}) => {
                                                return (
                                                    <FormControl className="my-event-input" size="small">
                                                        <InputLabel sx={{display: "none"}}
                                                                    htmlFor="location-autocomplete">{i18n.t("create.location")}</InputLabel>
                                                        <Autocomplete
                                                            disablePortal
                                                            disableClearable
                                                            id="location-autocomplete"
                                                            value={location ? location : auxxx}
                                                            options={locationList}
                                                            noOptionsText={i18n.t("autocompleteNoOptions")}
                                                            onChange={(event, item) => {
                                                                onChange(item);
                                                                setLocation(item)
                                                            }}
                                                            isOptionEqualToValue={(option, value) => {
                                                                return option.value === value.value
                                                            }}
                                                            renderInput={(params) => <TextField
                                                                variant='standard' {...params}
                                                                error={!!fieldState.error}/>}
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
                                    </li>
                                    <li>
                                        <Controller
                                            control={control}
                                            name="date"
                                            rules={{
                                                required: i18n.t('fieldRequired'),
                                                validate: {
                                                    min: (date) => {
                                                        return (new Date(date) > Date.now()) || i18n.t("create.dateError")
                                                    }
                                                }
                                            }}
                                            defaultValue={event.date ? event.date : ""}
                                            render={({field: {ref, onBlur, name, onChange, ...field}, fieldState}) => (
                                                <FormControl className="my-event-input">
                                                    <LocalizationProvider dateAdapter={AdapterDayjs}
                                                                          adapterLocale={i18n.language != 'en' && i18n.language != 'es' ? 'en' : i18n.language}>
                                                        <InputLabel className="casper"
                                                                    htmlFor="event-date-input">{i18n.t("event.date")}</InputLabel>
                                                        <DateTimePicker
                                                            id="event-date-input"
                                                            minDate={Date.now()}
                                                            renderInput={(inputProps) => (
                                                                <TextField
                                                                    {...inputProps}
                                                                    onBlur={onBlur}
                                                                    name={name}
                                                                    error={!!fieldState.error}
                                                                    variant="standard"
                                                                />)}
                                                            onChange={(event) => {
                                                                onChange(event);
                                                                setDate(event);
                                                            }}
                                                            {...field}
                                                            inputRef={ref}
                                                        />
                                                    </LocalizationProvider>
                                                    {fieldState.error ? (
                                                        <FormHelperText error>
                                                            {fieldState.error?.message}
                                                        </FormHelperText>
                                                    ) : null}
                                                </FormControl>
                                            )}
                                        />


                                    </li>
                                    <li>
                                        <h4>{i18n.t("event.type")}</h4>
                                        <Controller
                                            name="type"
                                            rules={{required: i18n.t('fieldRequired')}}
                                            control={control}
                                            defaultValue={event.type.id}
                                            render={({field, fieldState}) => {
                                                return (
                                                    <FormControl className="my-event-input" size="small"
                                                                 variant="standard">
                                                        <InputLabel className="casper" id="type-select-label"
                                                                    error={!!fieldState.error}>{i18n.t("create.type")}</InputLabel>
                                                        <Select
                                                            id="type-select"
                                                            labelId="type-select-label"
                                                            value={event.type.id}
                                                            error={!!fieldState.error}
                                                            {...field}
                                                        >
                                                            {typeList.map((x) => (
                                                                <MenuItem
                                                                    key={x.value}
                                                                    value={x.value}
                                                                >
                                                                    {x.label}
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
                                    </li>

                                    <li>
                                        <h4>{i18n.t("event.tags")}</h4>
                                        <Controller
                                            control={control}
                                            defaultValue={theTags}
                                            name="tags"
                                            render={({field: {onChange, value, name, ref}}) => {
                                                const handleSelectChange = (selectedOption) => {
                                                    onChange(selectedOption.target.value);
                                                    setTag(selectedOption.target.value);
                                                };

                                                return (
                                                    <>
                                                        <FormControl className="my-event-input" variant="standard"
                                                                     size="small">
                                                            <InputLabel className="casper"
                                                                        id="tags-select-label">{i18n.t("create.tags")}</InputLabel>
                                                            <Select
                                                                variant="standard"
                                                                labelId="tags-select-label"
                                                                id="tags-select"
                                                                multiple
                                                                value={tag ? tag : theTags}
                                                                onChange={handleSelectChange}
                                                            >
                                                                {tagList.map((x) => (
                                                                    <MenuItem
                                                                        key={x.value}
                                                                        value={x.value}
                                                                    >
                                                                        {x.label}
                                                                    </MenuItem>
                                                                ))}
                                                            </Select>
                                                        </FormControl>
                                                    </>
                                                );
                                            }}
                                        />
                                    </li>

                                    <div className="horizontal align-center">
                                        <FormControlLabel
                                            value={activeMin !== 'undefined' ? activeMin : (!!event.minAge)}
                                            checked={activeMin !== 'undefined' ? activeMin : (!!event.minAge)}
                                            control={<Checkbox onClick={() => {
                                                setActiveMin(activeMin !== 'undefined' ? !activeMin : (!event.minAge))
                                            }
                                            }/>}
                                            label={i18n.t("create.hasMin")}
                                            labelPlacement="end"
                                        />

                                        <Controller
                                            name="minAge"
                                            rules={{
                                                validate: value => {
                                                    if (!active) return true;
                                                    if (value) return true;
                                                    return i18n.t('fieldRequired')
                                                }
                                            }}
                                            control={control}
                                            defaultValue={event.minAge}
                                            render={({field, fieldState}) => {
                                                return (
                                                    <FormControl variant="standard"
                                                                 disabled={activeMin !== 'undefined' ? !activeMin : (!event.minAge)}
                                                                 className={"min-age-select"}>
                                                        <InputLabel id="stackoverflow-label"
                                                                    error={!!fieldState.error}>{i18n.t("event.minAge")}</InputLabel>
                                                        <Select
                                                            id="age-select"
                                                            label={i18n.t("event.minAge")}
                                                            labelId="minAge-id"
                                                            value={event.minAge ? ages.indexOf(event.minAge) : ''}
                                                            error={!!fieldState.error}
                                                            {...field}
                                                        >
                                                            {ages.map((x) => (
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
                                    </div>
                                </ul>
                            }
                        </Paper>
                    </div>
                    <TableContainer component={Paper}>
                        <Table className="edit-table" size="small">
                            
                            {!over ? 
                            <>
                            <TableHead>
                                <StyledTableRow>
                                    <StyledTableCell>{i18n.t("event.ticket")}</StyledTableCell>
                                    <StyledTableCell>{i18n.t("event.price")}</StyledTableCell>
                                    <StyledTableCell>{i18n.t("event.quantity")}</StyledTableCell>
                                    <StyledTableCell>{i18n.t("event.maxPUser")}</StyledTableCell>
                                    <StyledTableCell>{i18n.t("event.starting")}</StyledTableCell>
                                    <StyledTableCell>{i18n.t("event.until")}</StyledTableCell>
                                    <StyledTableCell>
                                        {edit &&
                                            <IconButton onClick={() => {
                                                if (fields.lengTableCell >= 5) {
                                                    alert(i18n.t("myEvents.ticketsLeftError"))
                                                    return;
                                                }
                                                append({
                                                    ticketName: "",
                                                    price: "",
                                                    qty: "",
                                                    maxPerUser: "",
                                                    starting: "",
                                                    until: ""
                                                });
                                            }}><AddRoundedIcon/></IconButton>
                                        }

                                    </StyledTableCell>
                                </StyledTableRow>
                            </TableHead>
                            <TableBody>
                                {fields.length === 0 ?

                                    <TableRow><StyledTableCell>{i18n.t("event.noTickets")}</StyledTableCell></TableRow>

                                    : fields.map((item, index) => {
                                        console.log('EL INDICE ES' + index)
                                        return (
                                            <StyledTableRow key={item.id}>
                                                <StyledTableCell>

                                                    {edit ?
                                                        <Controller
                                                            name={`tickets[${index}].ticketName`}
                                                            control={control}
                                                            rules={{
                                                                required: i18n.t('fieldRequired'),
                                                            }}
                                                            defaultValue={`${item.ticketName}`}
                                                            render={({field, fieldState}) => {
                                                                return (
                                                                    <FormControl>
                                                                        <InputLabel sx={{display: "none"}}
                                                                                    htmlFor={`ticketname-input${item.ticketid}`}>{i18n.t("bookings.price")}</InputLabel>
                                                                        <TextField
                                                                            id={`ticketname-input${item.ticketid}`}
                                                                            variant="standard"
                                                                            error={!!fieldState.error}
                                                                            {...field} />
                                                                        {fieldState.error ? (
                                                                            <FormHelperText error>
                                                                                {fieldState.error?.message}
                                                                            </FormHelperText>
                                                                        ) : null}
                                                                    </FormControl>
                                                                );
                                                            }}
                                                        />
                                                        : <span>{item.ticketName}</span>
                                                    }

                                                </StyledTableCell>
                                                <StyledTableCell>

                                                    {edit ?
                                                        <Controller
                                                            name={`tickets[${index}].price`}
                                                            control={control}
                                                            rules={{
                                                                required: i18n.t('fieldRequired'),
                                                                validate: {
                                                                    min: (x) => {
                                                                        return x > 0 || i18n.t("myEvents.ticketPriceError")
                                                                    }
                                                                }
                                                            }}
                                                            defaultValue={`${item.price}`}
                                                            render={({field, fieldState}) => {
                                                                return (
                                                                    <FormControl>
                                                                        <InputLabel sx={{display: "none"}}
                                                                                    htmlFor={`price-input${item.ticketid}`}>{i18n.t("bookings.price")}</InputLabel>
                                                                        <TextField id={`price-input${item.ticketid}`}
                                                                                   variant="standard"
                                                                                   type="number"
                                                                                   InputProps={{
                                                                                       startAdornment: <InputAdornment
                                                                                           position="start">$</InputAdornment>,
                                                                                       inputMode: 'numeric',
                                                                                       pattern: '[0-9]*'
                                                                                   }}
                                                                                   error={!!fieldState.error}
                                                                                   {...field} />
                                                                        {fieldState.error ? (
                                                                            <FormHelperText error>
                                                                                {fieldState.error?.message}
                                                                            </FormHelperText>
                                                                        ) : null}
                                                                    </FormControl>
                                                                );
                                                            }}
                                                        />
                                                        : <span>${item.price}</span>
                                                    }

                                                </StyledTableCell>
                                                <StyledTableCell>

                                                    {edit ?
                                                        <Controller
                                                            name={`tickets[${index}].qty`}
                                                            control={control}
                                                            rules={{
                                                                required: i18n.t('fieldRequired'),
                                                                validate: {
                                                                    min: (x) => {
                                                                        return x > 0 || i18n.t("myEvents.ticketQtyError")
                                                                    }
                                                                }
                                                            }}
                                                            defaultValue={`${item.qty}`}
                                                            render={({field, fieldState}) => {
                                                                return (
                                                                    <FormControl>
                                                                        <InputLabel sx={{display: "none"}}
                                                                                    htmlFor={`qty-input${item.ticketid}`}>{i18n.t("bookings.price")}</InputLabel>
                                                                        <TextField id={`qty-input${item.ticketid}`}
                                                                                   variant="standard"
                                                                                   type="number"
                                                                                   InputProps={{
                                                                                       inputMode: 'numeric',
                                                                                       pattern: '[0-9]*'
                                                                                   }}
                                                                                   error={!!fieldState.error}
                                                                                   {...field} />
                                                                        {fieldState.error ? (
                                                                            <FormHelperText error>
                                                                                {fieldState.error?.message}
                                                                            </FormHelperText>
                                                                        ) : null}
                                                                    </FormControl>
                                                                );
                                                            }}
                                                        />

                                                        : <span>{item.qty}</span>
                                                    }
                                                </StyledTableCell>
                                                <StyledTableCell>

                                                    {edit ?
                                                        <Controller
                                                            name={`tickets[${index}].maxPerUser`}
                                                            control={control}
                                                            rules={{
                                                                required: i18n.t('fieldRequired'),
                                                                // validate: {
                                                                //     min: (x) => {
                                                                //         return x >= 1 || i18n.t("myEvents.ticketsPerUserError")
                                                                //     },
                                                                //     max: (x) => {
                                                                //         return x <= 10 || i18n.t("myEvents.ticketsPerUserError")
                                                                //     }
                                                                // }
                                                            }}
                                                            defaultValue={`${item.maxPerUser}`}
                                                            render={({field, fieldState}) => {
                                                                return (
                                                                    <FormControl>
                                                                        <InputLabel sx={{display: "none"}}
                                                                                    htmlFor={`maxPUser-input${item.ticketid}`}>{i18n.t("bookings.price")}</InputLabel>
                                                                        <TextField id={`maxPUser-input${item.ticketid}`}
                                                                                   variant="standard"
                                                                                   type="number"
                                                                                   InputProps={{
                                                                                       inputMode: 'numeric',
                                                                                       pattern: '[0-9]*'
                                                                                   }}
                                                                                   error={!!fieldState.error}
                                                                                   {...field} />
                                                                        {fieldState.error ? (
                                                                            <FormHelperText error>
                                                                                {fieldState.error?.message}
                                                                            </FormHelperText>
                                                                        ) : null}
                                                                    </FormControl>
                                                                );
                                                            }}
                                                        />
                                                        : <span>{item.maxPerUser}</span>
                                                    }

                                                </StyledTableCell>
                                                <StyledTableCell className="date-input">

                                                    {edit ?
                                                        // <Controller
                                                        //     control={control}
                                                        //     name={`tickets[${index}].starting`}
                                                        //     rules={{
                                                        //     }}
                                                        //     defaultValue={item?.starting}
                                                        //     render={({
                                                        //                  field: {ref, onBlur, name, onChange, ...field},
                                                        //                  fieldState
                                                        //              }) => (
                                                        //         <FormControl>
                                                        //             <LocalizationProvider dateAdapter={AdapterDayjs}
                                                        //                                   adapterLocale={i18n.language != 'en' && i18n.language != 'es' ? 'en' : i18n.language}>
                                                        //                 <InputLabel sx={{display: "none"}}
                                                        //                             htmlFor={`tickets[${index}].starting`}>{i18n.t("bookings.starting")}</InputLabel>
                                                        //                 <DateTimePicker
                                                        //                     id={`tickets[${index}].starting`}
                                                        //                     renderInput={(inputProps) => (
                                                        //                         <TextField
                                                        //                             {...inputProps}
                                                        //                             onBlur={onBlur}
                                                        //                             name={name}
                                                        //                             error={!!fieldState.error}
                                                        //                             variant="standard"
                                                        //                         />)}
                                                        //                     isHiddenLabel
                                                        //                     onChange={(event) => {
                                                        //                         onChange(event)
                                                        //                         setDate(event)
                                                        //                     }}
                                                        //                     {...field}
                                                        //                     inputRef={ref}
                                                        //                 />
                                                        //             </LocalizationProvider>
                                                        //             {fieldState.error ? (
                                                        //                 <FormHelperText error>
                                                        //                     {fieldState.error?.message}
                                                        //                 </FormHelperText>
                                                        //             ) : null}
                                                        //         </FormControl>
                                                        //     )}
                                                        // />

                                                        <Controller
                                                            control={control}
                                                            name={`tickets[${index}].starting`}
                                                            rules={{
                                                                // required: i18n.t('fieldRequired'),
                                                                // validate: {
                                                                //     min: (date) => {
                                                                //         return (new Date(date) > Date.now()) || i18n.t("create.dateError")
                                                                //     }
                                                                // }
                                                            }}
                                                            defaultValue={item?.starting ? item.starting : ""}
                                                            render={({field: {ref, onBlur, name, onChange, ...field}, fieldState}) => (
                                                                <FormControl className="my-event-input">
                                                                    <LocalizationProvider dateAdapter={AdapterDayjs}
                                                                                          adapterLocale={i18n.language != 'en' && i18n.language != 'es' ? 'en' : i18n.language}>
                                                                        <InputLabel className="casper"
                                                                                    htmlFor={`tickets[${index}].starting-input`}>{i18n.t("bookings.starting")}</InputLabel>
                                                                                        <DateTimePicker
                                                                                            id={`tickets[${index}].starting-input`}
                                                                            renderInput={(inputProps) => (
                                                                                <TextField
                                                                                    {...inputProps}
                                                                                    onBlur={onBlur}
                                                                                    name={name}
                                                                                    error={!!fieldState.error}
                                                                                    variant="standard"
                                                                                />)}
                                                                            onChange={(event) => {
                                                                                onChange(event);
                                                                            }}
                                                                            {...field}
                                                                            inputRef={ref}
                                                                        />
                                                                    </LocalizationProvider>
                                                                    {fieldState.error ? (
                                                                        <FormHelperText error>
                                                                            {fieldState.error?.message}
                                                                        </FormHelperText>
                                                                    ) : null}
                                                                </FormControl>
                                                            )}
                                                        />


                                                        :
                                                        <span>{item.starting ? ParseDateTime(item.starting) : ""}</span>
                                                    }

                                                </StyledTableCell>
                                                <StyledTableCell className="date-input">

                                                    {edit ?

                                                        <Controller
                                                            control={control}
                                                            name={`tickets[${index}].until`}
                                                            rules={{
                                                                validate: () => {
                                                                    if (getValues(`tickets[${index}].starting`) && getValues(`tickets[${index}].until`)) {
                                                                        return new Date(getValues(`tickets[${index}].starting`)).getTime() < new Date(getValues(`tickets[${index}].until`)).getTime()
                                                                    }
                                                                    return true;
                                                                }
                                                            }}
                                                            defaultValue={item?.until ? item.until : ""}
                                                            render={({
                                                                         field: {ref, onBlur, name, onChange, ...field},
                                                                         fieldState
                                                                     }) => (
                                                                <FormControl>
                                                                    <LocalizationProvider dateAdapter={AdapterDayjs}
                                                                                          adapterLocale={i18n.language != 'en' && i18n.language != 'es' ? 'en' : i18n.language}>
                                                                        <InputLabel sx={{display: "none"}}
                                                                                    htmlFor={`tickets[${index}].until-input`}>{i18n.t("bookings.until")}</InputLabel>
                                                                        <DateTimePicker

                                                                            id={`tickets[${index}].until-input`}
                                                                            renderInput={(inputProps) => (
                                                                                <TextField
                                                                                    {...inputProps}
                                                                                    onBlur={onBlur}
                                                                                    name={name}
                                                                                    error={!!fieldState.error}
                                                                                    variant="standard"
                                                                                />)}
                                                                            isHiddenLabel
                                                                            onChange={(event) => {
                                                                                onChange(event);
                                                                            }}
                                                                            {...field}
                                                                            inputRef={ref}
                                                                        />
                                                                    </LocalizationProvider>
                                                                    {fieldState.error ? (
                                                                        <FormHelperText error>
                                                                            {fieldState.error?.message}
                                                                        </FormHelperText>
                                                                    ) : null}
                                                                </FormControl>
                                                            )}
                                                        />
                                                        : <span>{item.until ? ParseDateTime(item.until) : ""}</span>
                                                    }

                                                </StyledTableCell>

                                                <StyledTableCell>
                                                {edit && 
                                                    <IconButton onClick={() => {
                                                        if (item.ticketId) {
                                                            rowsData.push({ ticketId: item.ticketId, index: index })
                                                            setRowsData(rowsData)
                                                        }
                                                        remove(index)
                                                    }}><DeleteRoundedIcon/></IconButton>

                                                }
                                                </StyledTableCell>
                                            </StyledTableRow>
                                        );
                                    })}
                            </TableBody></> 
                            :
                                    
                                <>{eventStats && 
                                <><TableHead>
                                    <StyledTableRow>
                                        <StyledTableCell>{i18n.t("eventStats.stats")}</StyledTableCell>
                                        <StyledTableCell></StyledTableCell>
                                    </StyledTableRow>
                                </TableHead>
                                <TableBody>
                                    <StyledTableRow>
                                        <StyledTableCell>{i18n.t("eventStats.attended")}</StyledTableCell>
                                        <StyledTableCell className="right-text">{eventStats.attended}</StyledTableCell>
                                    </StyledTableRow>
                                    <StyledTableRow>
                                        <StyledTableCell>{i18n.t("eventStats.booked")}</StyledTableCell>
                                        <StyledTableCell className="right-text">{eventStats.booked}</StyledTableCell>
                                    </StyledTableRow>
                                    <StyledTableRow>
                                        <StyledTableCell>{i18n.t("eventStats.attendance")}</StyledTableCell>
                                        <StyledTableCell className="right-text">{eventStats.attendance}</StyledTableCell>
                                    </StyledTableRow>
                                    <StyledTableRow>
                                        <StyledTableCell>{i18n.t("eventStats.saleRatio")}</StyledTableCell>
                                        <StyledTableCell className="right-text">{eventStats.saleRatio}</StyledTableCell>
                                    </StyledTableRow>
                                    <StyledTableRow>
                                        <StyledTableCell>{i18n.t("eventStats.income")}</StyledTableCell>
                                        <StyledTableCell className="right-text">{eventStats.income}</StyledTableCell>
                                    </StyledTableRow>
                                    <StyledTableRow>
                                        <StyledTableCell>{i18n.t("eventStats.expected")}</StyledTableCell>
                                        <StyledTableCell className="right-text">{eventStats.expectedIncome}</StyledTableCell>
                                    </StyledTableRow>
                                </TableBody>
</>
}
                                {ticketStats && 
                                <>
                                                                <TableHead>
                                    <StyledTableRow>
                                        <StyledTableCell>{i18n.t("ticketStats.ticketName")}</StyledTableCell>
                                        <StyledTableCell>{i18n.t("ticketStats.attendance")}</StyledTableCell>
                                        <StyledTableCell>{i18n.t("ticketStats.saleRatio")}</StyledTableCell>
                                        <StyledTableCell>{i18n.t("ticketStats.price")}</StyledTableCell>
                                        <StyledTableCell>{i18n.t("ticketStats.realQty")}</StyledTableCell>
                                        <StyledTableCell>{i18n.t("ticketStats.qty")}</StyledTableCell>
                                        <StyledTableCell>{i18n.t("ticketStats.income")}</StyledTableCell>
                                        <StyledTableCell>{i18n.t("ticketStats.booked")}</StyledTableCell>
                                    </StyledTableRow>
                                </TableHead>
                                <TableBody>
                                    {fields.map((item, index) => {
                                            return (
                                                <StyledTableRow key={item.id}>
                                                    <StyledTableCell><span>{item.ticketName}</span></StyledTableCell>
                                                    <StyledTableCell><span>{item.attendance}</span></StyledTableCell>
                                                    <StyledTableCell><span>{item.saleRatio}</span></StyledTableCell>
                                                    <StyledTableCell><span>{item.price}</span></StyledTableCell>
                                                    <StyledTableCell><span>{item.realQty}</span></StyledTableCell>
                                                    <StyledTableCell><span>{item.qty}</span></StyledTableCell>
                                                    <StyledTableCell><span>{item.income}</span></StyledTableCell>
                                                    <StyledTableCell><span>{item.booked}</span></StyledTableCell>
                                                    </StyledTableRow>
                                            );
                                        })}
                                </TableBody>
                            </>
                        }
                            </>}

                        </Table>
                    </TableContainer>

                    { edit &&
                        <>
                        <IconButton onClick={() => {handleSubmit(onSubmit)}} type="submit"><DoneRoundedIcon/></IconButton>
                        <IconButton onClick={() => setEdit(false)}><ClearRoundedIcon/></IconButton>
                        </>
                    }
                </form>

            </section>
        </Layout>
    );
}

export default MyEvent
