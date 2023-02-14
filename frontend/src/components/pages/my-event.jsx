import { useEffect, useMemo, useState, useRef } from 'react';
import Layout from '../layout';
import { server } from '../../utils/server';
import useSwr from "swr";
import useSWRImmutable from "swr/immutable";
import MyEventLoading from "../../components/my-events-content/my-event-loading";
import { useForm, useFieldArray, Controller } from "react-hook-form";
import { Link, useHistory, useLocation } from 'react-router-dom'
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
import Select, { SelectChangeEvent } from '@mui/material/Select';
import Button from '@mui/material/Button';
import Checkbox from '@mui/material/Checkbox';
import Table from '@mui/material/Table';
import TableRow from '@mui/material/TableRow';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableHead from '@mui/material/TableHead';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import FormControlLabel from '@mui/material/FormControlLabel';
import Autocomplete from '@mui/material/Autocomplete';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from 'dayjs'
import TextField from '@mui/material/TextField';
import InputAdornment from '@mui/material/InputAdornment';
import { styled } from '@mui/material/styles';
import ClearRoundedIcon from '@mui/icons-material/ClearRounded';
import AddRoundedIcon from '@mui/icons-material/AddRounded';
import 'dayjs/locale/es';
import 'dayjs/locale/en';
import { getPrice } from "../../utils/price";
import { ParseDateTime } from "../products-content/event-item"

const isEqualsJson = (oldTicket, newTicket) => {
    let oldKeys = Object.keys(oldTicket);
    let newKeys = Object.keys(newTicket);

    return oldKeys.length === newKeys.length && Object.keys(oldTicket).every(key => {
        // console.log(oldTicket[key])
        // console.log(newTicket[key])
        // console.log(oldTicket[key] == newTicket[key])
        return oldTicket[key] == newTicket[key]
    });
}

const getMinValue = (ticket) => {
    return ticket.hasOwnProperty("booked") && ticket.booked > 0 ? ticket.booked : 1
}

const MyEvent = (props) => {
    const prevLocation = useLocation();

    const [showBlock, setShowBlock] = useState('description');

    const fetcher = (url) => fetch(url).then((res) => res.json());

    const history = useHistory();
    let path = useFindPath();

    const [rowsData, setRowsData] = useState([]);
    const [index, setIndex] = useState(0);
    const [edit, setEdit] = useState(false);
    const [activeMin, setActiveMin] = useState();


    const {
        data: locations,
        isLoading: locationsLoading,
        isValidating: locationsValidating
    } = useSWRImmutable(`${server}/api/locations`, fetcher)
    const [location, setLocation] = useState();
    const {
        data: tags,
        isLoading: tagsLoading,
        isValidating: tagsValidating
    } = useSWRImmutable(`${server}/api/tags`, fetcher)
    const [tag, setTag] = useState();
    const {
        data: types,
        isLoading: typesLoading,
        isValidating: typesValidating
    } = useSWRImmutable(`${server}/api/types`, fetcher)
    const [type, setType] = useState();
    const [minAge, setMinAge] = useState();
    const [date, setDate] = useState(null);

    // const { register, handleSubmit, control, watch, setValue, formState: { errors } } = useForm();
    const [active, setActive] = useState(false)
    const [imageName, setImageName] = useState()
    const [image, setImage] = useState()
    const inputRef = useRef(null);

    let start = 14;
    let ages = [];
    while (start <= 27) {
        ages.push({
            value: start,
            label: start
        })
        start++;
    }

    const { data: event, mutate, error: errorData } = useSwr(props.match.params.id ? `${server}/api/events/${props.match.params.id}` : null, fetcher)

    const [tickets, setTickets] = useState([]);

    useEffect(() => {
        if (event) {
            fetch(event.tickets)
                .then(r => {
                    if (r.status == 200) {
                        return r.json()
                    } else {
                        return
                    }
                })
                .then(d => {
                    console.log(d)
                    setTickets(d)
                    setValue("tickets", tickets);
                })
        }
    }, [event])

    const { data: aux, error: error } = useSwr(event ? `${event.image}` : null, fetcher)

    const { register, control, handleSubmit, reset, watch, setValue, getValues, formState: { errors } } = useForm({
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

    const { fields, append, prepend, remove, swap, move, insert } = useFieldArray(
        {
            control,
            name: "tickets"
        }
    );

    let accessToken;
    let refreshToken;
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
        refreshToken = localStorage.getItem("Refresh-Token");
    }

    useEffect(() => {
        setValue("tickets", tickets);
    }, [tickets]);

    useEffect(() => {
        const fetchData = async (accessToken, refreshToken, pathname) => {
            let res = fetch(`${server}/api/users/test`, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
            })

            let aux = await res;
            if (aux.status == 200) {
                return;
            }

            res = fetch(`${server}/api/users/test`, {
                headers: {
                    'Authorization': `Bearer ${refreshToken}`
                },
            })

            aux = await res;
            if (aux.status == 200) {
                localStorage.setItem("Access-Token", aux.headers.get("Access-Token"))
                return;
            }

            if (path !== "/login") {
                history.push(`/login?redirectTo=${prevLocation.pathname}`);
            }
        }

        if (accessToken && refreshToken) {
            fetchData(accessToken, refreshToken, path)
        } else {
            const pathname = path
            if (path !== "/login") {
                history.push(`/login?redirectTo=${prevLocation.pathname}`);
            }
        }
    }, [accessToken, refreshToken]);

    if (error || errorData) return <p>No data</p>
    if (!aux || !event) return <MyEventLoading />

    const onSubmit = async (data) => {
        console.log(data)

        console.log("LISTO")
        console.log(event)

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

        // if (!isEqualsJson(obj, event)) {
        //     console.log("not equals")
        // }
        let changed = false
        if (obj.name != data.name || 
            obj.location != data.location || 
            obj.type != data.type ||
            obj.date != date.date ||
            (data.description && obj.description != date.description) ||
            (data.tags && obj.tags != date.tags) ||
            (data.minAge && obj.minAge != date.minAge)) {
            changed = true
        }

        console.log(obj)

        let resi

        if (changed) {
            resi = await fetch(`${server}/api/events/${props.match.params.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify(obj)
            })
        }

        let resAux = await resi
        // return;

        console.log(rowsData)

        for (const x of rowsData) {
            await fetch(`${server}/api/tickets/${x}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
            })
        }

        let res
        let auxi = {
            tickets: []
        }

        if (data.tickets) {

        for (const d of data.tickets) {
            if (d.ticketId) {
                const ticket = tickets.find(x => {
                    return x.ticketId == d.ticketId
                })

                // console.log(ticket)
                console.log(d)

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

                const newTicket = { ...defaultValues };

                console.log(newTicket)

                let aux = JSON.parse(JSON.stringify(d));

                if (!isEqualsJson(d, newTicket)) {
                    console.log("not equals")
                    res = await fetch(`${server}/api/tickets/${d.ticketId}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${accessToken}`
                        },
                        body: JSON.stringify(aux)
                    })
                }
            }
            else {
                auxi.tickets.push(d)
            }
        }

        let aux = JSON.parse(JSON.stringify(auxi));
        console.log(aux)

        res = await fetch(`${server}/api/events/${props.match.params.id}/tickets`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(aux)
        })

        let json = await res;
        }

        // history
        mutate()
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

    if (locationsLoading || tagsLoading || typesLoading) return <p>{i18n.t("loading")}</p>

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

    console.log(event.minAge)

    return (
        <Layout>
            <section className="product-single">
                {/* <div className="container my-event-page"> */}
                    <form className="form container my-event-page" onSubmit={handleSubmit(onSubmit)}>
                        <div className="my-event-content">
                        <div className="contain">
                            <img className="event-image" src={`data:image/png;base64,${aux.image}`} alt="Event" />
                            {!!event.soldOut && <span className="event-image-sold-out">{i18n.t("event.soldOut")}</span>}
                        </div>
                            <Paper className="event-info" elevation={2}>
                                {/* <div className="product-single__content">
                        <img className={"product-gallery__image"} src={`data:image/png;base64,${aux.image}`} alt="My event image"/>
                    </div> */}
                                <div className="event-actions">
                                    {!edit ?
                                        <>
                                            <IconButton onClick={() => setEdit(true)} type="submit"><EditRoundedIcon /></IconButton>
                                            <IconButton><DeleteRoundedIcon /></IconButton>
                                        </> :
                                        <>
                                            <IconButton onClick={() => { setEdit(false) }}><DoneRoundedIcon /></IconButton>
                                            <IconButton onClick={() => setEdit(false)}><ClearRoundedIcon /></IconButton>
                                        </>
                                    }
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
                                        {/* <li>
                                            <h4>{i18n.t("event.minPrice")}</h4>
                                            <span>{getPrice(event.minPrice, false)}</span>
                                        </li> */}
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
                                                // <Link className="pointer" to={`/events?tags=${t.id}`}>
                                                <Chip label={t.name} key={t.id} onClick={() => history.push(`/events?page=1&tags=${t.id}`)} />
                                                // </Link>
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
                                            {/* <Input value={event.name}></Input> */}
                                            <Controller
                                name="name"
                                rules={{ required: i18n.t('fieldRequired') }}
                                control={control}
                                defaultValue={event.name}
                                render={({ field, fieldState }) => {
                                    return (
                                        <FormControl className="my-event-input">
                                            <InputLabel className="casper" htmlFor="name-input">{i18n.t("event.name")}</InputLabel>
                                            <TextField id="name-input" variant="standard"
                                                error={!!fieldState.error}
                                                // isHiddenLabel
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
                                            {/* <span>{event.description}</span> */}
                                            {/* <Input value={event.description}></Input> */}
                                            <Controller
                                name="description"
                                control={control}
                                defaultValue={event.description}
                                render={({ field, fieldState }) => {
                                    return (
                                        <FormControl className="full-width-input">
                                            <InputLabel className="casper" htmlFor="description-input">{i18n.t("event.description")}</InputLabel>
                                            <TextField id="description-input" variant="standard"
                                                multiline
                                                maxRows={3}
                                                // isHiddenLabel
                                                {...field} />
                                        </FormControl>
                                    );
                                }}
                            />
                                        </li>
                                        {/* <li>
                                            <h4>{i18n.t("event.minPrice")}</h4>
                                            <span>{event.minPrice}</span>
                                        <li> */}
                                        <li>
                                            <h4>{i18n.t("event.location")}</h4>
                                            <Controller
                                                name="location"
                                                rules={{ required: i18n.t('fieldRequired') }}
                                                control={control}
                                                defaultValue={auxxx}
                                                render={({ field: { onChange }, fieldState }) => {
                                                    return (
                                                        <FormControl className="my-event-input" size="small">
                                                            <InputLabel sx={{display: "none"}} htmlFor="location-autocomplete">{i18n.t("create.location")}</InputLabel>
                                                            <Autocomplete
                                                                disablePortal
                                                                disableClearable
                                                                id="location-autocomplete"
                                                                value={location ? location : auxxx}
                                                                options={locationList}
                                                                noOptionsText={i18n.t("autocompleteNoOptions")}
                                                                onChange={(event, item) => {
                                                                    console.log(item)
                                                                    onChange(item);
                                                                    setLocation(item)
                                                                }}
                                                                isOptionEqualToValue={(option, value) => { return option.value === value.value }}
                                                                renderInput={(params) => <TextField variant='standard' {...params} error={!!fieldState.error} />}
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
                                            <h4>{i18n.t("event.type")}</h4>
                                            {/* <span>{event.type}</span> */}
                                            {/* <Input value={event.type.name}></Input> */}
                                            <Controller
                                                name="type"
                                                rules={{ required: i18n.t('fieldRequired') }}
                                                control={control}
                                                defaultValue={event.type.id}
                                                render={({ field, fieldState }) => {
                                                    return (
                                                        <FormControl className="my-event-input" size="small" variant="standard">
                                                            <InputLabel className="casper" id="type-select-label" error={!!fieldState.error}>{i18n.t("create.type")}</InputLabel>
                                                            <Select
                                                                id="type-select"
                                                                labelId="type-select-label"
                                                                value={event.type.id}
                                                                error={!!fieldState.error}
                                                                {...field}
//                                                                input={<OutlinedInput/>}
                                                            >
                                                                {typeList.map((x) => (
                                                                    <MenuItem
                                                                        key={x.value}
                                                                        value={x.value}
                                                                    // value={x.label}
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
                                        <Controller
                                control={control}
                                name="date"
                                rules={{
                                    required: i18n.t('fieldRequired'),
                                    validate: {
                                        min: (date) => { return (new Date(date) > Date.now()) || i18n.t("create.dateError") }
                                    }
                                }}
                                defaultValue={event.date} // <---------- HERE
                                render={({ field: { ref, onBlur, name, onChange, ...field }, fieldState }) => (
                                    <FormControl className="my-event-input">
                                        <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale={i18n.language  != 'en' && i18n.language != 'es' ? 'en' : i18n.language}>
                                            <InputLabel className="casper" htmlFor="event-date-input">{i18n.t("event.date")}</InputLabel>
                                            <DateTimePicker
                                                id="event-date-input"
                                                renderInput={(inputProps) => (
                                                    <TextField
                                                        {...inputProps}
                                                        onBlur={onBlur}
                                                        name={name}
                                                        error={!!fieldState.error}
                                                        variant="standard"
                                                    />)}
                                                onChange={(event) => { onChange(event); setDate(event); }}
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
                                            <h4>{i18n.t("event.tags")}</h4>
                                            <Controller
                                                control={control}
                                                defaultValue={theTags}
                                                name="tags"
                                                render={({ field: { onChange, value, name, ref } }) => {
                                                    const currentSelection = tagList.find(
                                                        (c) => c.value === value
                                                    );

                                                    const handleSelectChange = (selectedOption) => {
                                                        onChange(selectedOption.target.value);
                                                        console.log(selectedOption.target.value)
                                                        setTag(selectedOption.target.value);
                                                    };

                                                    return (
                                                        <>
                                                            <FormControl className="my-event-input" variant="standard" size="small">
                                                                <InputLabel className="casper" id="tags-select-label">{i18n.t("create.tags")}</InputLabel>
                                                                <Select
                                                                    variant="standard"
                                                                    labelId="tags-select-label"
                                                                    id="tags-select"
                                                                    multiple
                                                                    // value={tag}
                                                                    value={tag ? tag : theTags}
                                                                    onChange={handleSelectChange}
                                                                    // input={<OutlinedInput label={i18n.t("create.tags")} />}
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

                                            {/* <span>{event.tags}</span> */}
                                        </li>
                                        
                                        {/* {event.minAge &&
                                            <li>
                                                <h4>{i18n.t("event.minAge")}</h4>
                                                <Controller
                                                    name="minAge"
                                                    rules={{ required: i18n.t('fieldRequired') }}
                                                    control={control}
                                                    defaultValue={event.minAge}
                                                    render={({ field, fieldState }) => {
                                                        return (
                                                            <FormControl sx={{ width: 120 }}>
                                                                <InputLabel id="type-select-label" error={!!fieldState.error}>{i18n.t("create.type")}</InputLabel>
                                                                <Select
                                                                    id="type-select"
                                                                    label={i18n.t("create.type")}
                                                                    labelId="type-select-label"
                                                                    value={ages.indexOf(event.minAge)}
                                                                    // onChange={(e, i) => {console.log(i); onChange(i); setType(i)}}
                                                                    error={!!fieldState.error}
                                                                    {...field}
                                                                >
                                                                    {ages.map((x) => (
                                                                        <MenuItem
                                                                            key={x.value}
                                                                            value={x.value}
                                                                        // value={x.label}
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
                                            || */}
                                            <div className="horizontal align-center">
                                            <FormControlLabel
                                                value={activeMin !== 'undefined' ? activeMin : (event.minAge ? true : false)}
                                                checked={activeMin !== 'undefined' ? activeMin : (event.minAge ? true : false)}
                                                control={<Checkbox onClick={() => {
                                                    console.log(activeMin)
                                                    setActiveMin(activeMin !== 'undefined' ? !activeMin : (event.minAge ? false : true))
                                                }
                                                } />}
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
                                                render={({ field, fieldState }) => {
                                                    return (
                                                        <FormControl variant="standard" disabled={activeMin !== 'undefined' ? !activeMin : (event.minAge ? false : true)} className={"min-age-select"}>
                                                            <InputLabel id="stackoverflow-label" error={!!fieldState.error}>{i18n.t("event.minAge")}</InputLabel>
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
                                        {/* } */}
                                    </ul>
                                }
                                {/* <ul>
                                <li>
                                    <h4>{i18n.t("event.name")}</h4>
                                    <Input value={event.name}></Input>
                                </li>
                                <li>
                                    <h4>{i18n.t("event.description")}</h4>
                                    {/* <span>{event.description}</span> */}
                                {/* <Input value={event.description}></Input> */}
                                {/* </li>
                                <li>
                                    <h4>{i18n.t("event.minPrice")}</h4>
                                    {/* <span>{event.minPrice}</span> */}
                                {/* <Input value={event.minPrice}></Input>
                                </li>
                                <li>
                                    <h4>{i18n.t("event.type")}</h4>
                                    {/* <span>{event.type}</span> */}
                                {/* <Input value={event.type.name}></Input>
                                </li>
                                <li>
                                    <h4>{i18n.t("event.location")}</h4>
                                    {/* <span>{event.location}</span> */}
                                {/* <Input value={event.location}></Input> */}
                                {/*<Controller
                                name="location"
                                rules={{ required: i18n.t('fieldRequired') }}
                                control={control}
                                // defaultValue={location}
                                defaultValue={location}
                                render={({ field: { onChange }, fieldState }) => {
                                    return (
                                        <FormControl sx={{ width: 120 }}>
                                            <Autocomplete
                                                disablePortal
                                                id="location-autocomplete"
                                                value={event.location.name}
                                                options={locationList}
                                                noOptionsText={i18n.t("autocompleteNoOptions")}
                                                onChange={(event, item) => {
                                                    // console.log(item)
                                                    onChange(item);
                                                    setLocation(item)
                                                }}
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
                                </li>
                                <li>
                                    <h4>{i18n.t("event.tags")}</h4>
                                    {event.tags && event.tags.map((t) =>
                                        // <Link className="pointer" to={`/events?tags=${t.id}`}>
                                        <Chip label={t.name} key={t.id} onClick={() => history.push(`/events?page=1&tags=${t.id}`)} />
                                        // </Link>
                                    )}
                                    {/* <span>{event.tags}</span> */}
                                {/* </li>
                                {event.minAge &&
                                    <li>
                                        <h4>{i18n.t("event.minAge")}</h4>
                                        <span>{i18n.t("event.minAgeText")} {event.minAge}</span>
                                    </li>
                                }
                            </ul> */}
                                {/* </form> */}
                            </Paper>
                        </div>
                        {/* <Table className="ticket-table ticket-table-input"> */}
                        <TableContainer component={Paper}>
                            <Table className="edit-table" size="small">
                                <TableHead>
                                    <StyledTableRow>
                                        <StyledTableCell>Ticket name</StyledTableCell>
                                        <StyledTableCell>Price</StyledTableCell>
                                        <StyledTableCell>Quantity</StyledTableCell>
                                        <StyledTableCell>Max p/ user</StyledTableCell>
                                        <StyledTableCell>Starting</StyledTableCell>
                                        <StyledTableCell>Until</StyledTableCell>
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
                                                }} ><AddRoundedIcon /></IconButton>
                                            }
                                            {/* <button
                                                type="button"
                                                onClick={() => {
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
                                                }}
                                            >
                                                Add
                                            </button> */}
                                        </StyledTableCell>
                                    </StyledTableRow>
                                </TableHead>
                                <TableBody>
                                    {fields.length == 0 ?
                                    
                                    <TableRow><StyledTableCell>{i18n.t("event.noTickets")}</StyledTableCell></TableRow>
                                    
                                    : fields.map((item, index) => {
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
                                                            render={({ field, fieldState }) => {
                                                                return (
                                                                    <FormControl>
                                                                        <InputLabel sx={{ display: "none" }} htmlFor={`ticketname-input${item.ticketid}`}>{i18n.t("bookings.price")}</InputLabel>
                                                                        <TextField id={`ticketname-input${item.ticketid}`} variant="standard"
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


                                                    {/* <input
                                                            // className={quicksand.className}
                                                            defaultValue={`${item.ticketName}`}
                                                            {...register(`tickets[${index}].ticketName`, { required: true })}
                                                            type="text"
                                                        />
                                            {errors.tickets?.at(index)?.ticketName?.type === 'required' && <span>{i18n.t("fieldRequired")}</span>} */}
                                                </StyledTableCell>
                                                <StyledTableCell>

                                                    {edit ?
                                                        <Controller
                                                            name={`tickets[${index}].price`}
                                                            control={control}
                                                            rules={{
                                                                required: i18n.t('fieldRequired'),
                                                                validate: {
                                                                    min: (x) => { return x > 0 || i18n.t("myEvents.ticketPriceError") }
                                                                }
                                                            }}
                                                            defaultValue={`${item.price}`}
                                                            render={({ field, fieldState }) => {
                                                                return (
                                                                    <FormControl>
                                                                        <InputLabel sx={{ display: "none" }} htmlFor={`price-input${item.ticketid}`}>{i18n.t("bookings.price")}</InputLabel>
                                                                        <TextField id={`price-input${item.ticketid}`} variant="standard"
                                                                            type="number"
                                                                            InputProps={{
                                                                                startAdornment: <InputAdornment position="start">$</InputAdornment>,
                                                                                inputMode: 'numeric', pattern: '[0-9]*'
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
                                                    {/*
                                                        <input
                                                            defaultValue={`${item.price}`}
                                                            {...register(`tickets[${index}].price`, { required: true, min: 0 })}
                                                            type="number"
                                                        />
                                                        {errors.tickets?.at(index)?.price?.type === 'required' && <span>{i18n.t("fieldRequired")}</span>}
                                                        {errors.tickets?.at(index)?.price?.type === 'min' && <span>{i18n.t("myEvents.ticketPriceError")}</span>} */}
                                                </StyledTableCell>
                                                <StyledTableCell>

                                                    {edit ?
                                                        <Controller
                                                            name={`tickets[${index}].qty`}
                                                            control={control}
                                                            rules={{
                                                                required: i18n.t('fieldRequired'),
                                                                validate: {
                                                                    min: (x) => { return x > 0 || i18n.t("myEvents.ticketQtyError") }
                                                                }
                                                            }}
                                                            defaultValue={`${item.qty}`}
                                                            render={({ field, fieldState }) => {
                                                                return (
                                                                    <FormControl>
                                                                        <InputLabel sx={{ display: "none" }} htmlFor={`qty-input${item.ticketid}`}>{i18n.t("bookings.price")}</InputLabel>
                                                                        <TextField id={`qty-input${item.ticketid}`} variant="standard"
                                                                            type="number"
                                                                            InputProps={{
                                                                                inputMode: 'numeric', pattern: '[0-9]*'
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
                                                    {/* <input
                                                            defaultValue={`${item.qty}`}
                                                            {...register(`tickets[${index}].qty`, {
                                                                required: true,
                                                                min: getMinValue(item)
                                                            }
                                                            )}
                                                            type="number"
                                                        />
                                                        {errors.tickets?.at(index)?.qty?.type === 'required' && <span>{i18n.t("fieldRequired")}</span>}
                                                        {errors.tickets?.at(index)?.qty?.type === 'min' && <span>{i18n.t("myEvents.ticketQtyError")}</span>} */}
                                                </StyledTableCell>
                                                <StyledTableCell>

                                                    {edit ?
                                                        <Controller
                                                            name={`tickets[${index}].maxPerUser`}
                                                            control={control}
                                                            rules={{
                                                                required: i18n.t('fieldRequired'),
                                                                validate: {
                                                                    min: (x) => { return x >= 1 || i18n.t("myEvents.ticketsPerUserError") },
                                                                    max: (x) => { return x <= 10 || i18n.t("myEvents.ticketsPerUserError") }
                                                                }
                                                            }}
                                                            defaultValue={`${item.maxPerUser}`}
                                                            render={({ field, fieldState }) => {
                                                                return (
                                                                    <FormControl>
                                                                        <InputLabel sx={{ display: "none" }} htmlFor={`maxPUser-input${item.ticketid}`}>{i18n.t("bookings.price")}</InputLabel>
                                                                        <TextField id={`maxPUser-input${item.ticketid}`} variant="standard"
                                                                            type="number"
                                                                            InputProps={{
                                                                                inputMode: 'numeric', pattern: '[0-9]*'
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

                                                    {/* <input
                                                            defaultValue={`${item.maxPerUser}`}
                                                            {...register(`tickets[${index}].maxPerUser`, { required: true, min: 1, max: 10 })}
                                                            type="number"
                                                        />
                                                        {errors.tickets?.at(index)?.maxPerUser?.type === 'required' && <span>{i18n.t("fieldRequired")}</span>}
                                                        {errors.tickets?.at(index)?.maxPerUser?.type === 'min' && <span>{i18n.t("myEvents.ticketsPerUserError")}</span>}
                                                    {errors.tickets?.at(index)?.maxPerUser?.type === 'max' && <span>{i18n.t("myEvents.ticketsPerUserError")}</span>} */}
                                                </StyledTableCell>
                                                <StyledTableCell className="date-input">

                                                    {edit ?
                                                        <Controller
                                                            control={control}
                                                            name={`tickets[${index}].starting`}
                                                            rules={{
                                                                //                                                                required: i18n.t('fieldRequired'),
                                                            }}
                                                            defaultValue={date} // <---------- HERE
                                                            render={({ field: { ref, onBlur, name, onChange, ...field }, fieldState }) => (
                                                                <FormControl>
                                                                    <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale={i18n.language != 'en' && i18n.language != 'es' ? 'en' : i18n.language}>
                                                                        <InputLabel sx={{ display: "none" }} htmlFor={`tickets[${index}].starting`}>{i18n.t("bookings.starting")}</InputLabel>
                                                                        <DateTimePicker
                                                                            id={`tickets[${index}].starting`}
                                                                            renderInput={(inputProps) => (
                                                                                <TextField
                                                                                    {...inputProps}
                                                                                    onBlur={onBlur}
                                                                                    name={name}
                                                                                    error={!!fieldState.error}
                                                                                    variant="standard"
                                                                                />)}
                                                                            isHiddenLabel
                                                                            onChange={(event) => { console.log(event.toISOString()); onChange(event.toISOString()); setDate(event.toISOString()); }}
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

                                                        : <span>{item.starting ? ParseDateTime(item.starting) : ""}</span>
                                                    }

                                                    {/* <input
                                                            defaultValue={`${item.starting}`}
                                                            {...register(`tickets[${index}].starting`)}
                                                            type="datetime-local"
                                                /> */}
                                                    {/* {errors.tickets?.at(index)?.starting?.type && <span>{i18n.t("fieldRequired")}</span>} */}
                                                </StyledTableCell>
                                                <StyledTableCell className="date-input">

                                                    {edit ?
                                                        <Controller
                                                            control={control}
                                                            name={`tickets[${index}].until`}
                                                            rules={{
                                                                // required: i18n.t('fieldRequired'),
                                                                validate: () => {
                                                                    if (getValues(`tickets[${index}].starting`) && getValues(`tickets[${index}].until`)) {
                                                                        return new Date(getValues(`tickets[${index}].starting`)).getTime() < new Date(getValues(`tickets[${index}].until`)).getTime()
                                                                    }
                                                                    return true;
                                                                }
                                                            }}
                                                            defaultValue={date}
                                                            render={({ field: { ref, onBlur, name, onChange, ...field }, fieldState }) => (
                                                                <FormControl>
                                                                    <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale={i18n.language != 'en' && i18n.language != 'es' ? 'en' : i18n.language}>
                                                                        <InputLabel sx={{ display: "none" }} htmlFor={`tickets[${index}].until`}>{i18n.t("bookings.until")}</InputLabel>
                                                                        <DateTimePicker

                                                                            id={`tickets[${index}].until`}
                                                                            renderInput={(inputProps) => (
                                                                                <TextField
                                                                                    {...inputProps}
                                                                                    onBlur={onBlur}
                                                                                    name={name}
                                                                                    error={!!fieldState.error}
                                                                                    variant="standard"
                                                                                />)}
                                                                            isHiddenLabel
                                                                            onChange={(event) => { console.log(event.toISOString()); onChange(event.toISOString()); setDate(event.toISOString()); }}
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

                                                    {/* <input
                                                            defaultValue={`${item.until}`}
                                                            {...register(`tickets[${index}].until`, {
                                                                validate: () => {
                                                                    if (getValues(`tickets[${index}].starting`) && getValues(`tickets[${index}].until`)) {
                                                                        return new Date(getValues(`tickets[${index}].starting`)).getTime() < new Date(getValues(`tickets[${index}].until`)).getTime()
                                                                    }
                                                                    return true;
                                                                }
                                                            })}
                                                            type="datetime-local"
                                                        />
                                                        {errors.tickets?.at(index)?.until?.type === 'required' && <span>{i18n.t("fieldRequired")}</span>}
                                                        {errors.tickets?.at(index)?.until?.type === 'validate' && <span>{i18n.t("myEvents.tickeTableCellateError")}</span>} */}
                                                </StyledTableCell>

                                                <StyledTableCell>

                                                    <IconButton onClick={() => {
                                                        if (item.ticketId) {
                                                            console.log(item.ticketId)
                                                            rowsData.push(item.ticketId)
                                                            setRowsData(rowsData)
                                                        }
                                                        remove(index)
                                                    }}><DeleteRoundedIcon /></IconButton>
                                                    {/*<IconButton><EditRoundedIcon/></IconButton>

                                                <IconButton><DoneRoundedIcon/></IconButton>
                                                <IconButton><ClearRoundedIcon/></IconButton>*/}
                                                    {/* <button type="button" onClick={() => {
                                                    if (item.ticketId) {
                                                        console.log(item.ticketId)
                                                        rowsData.push(item.ticketId)
                                                        setRowsData(rowsData)
                                                    }
                                                    remove(index)
                                                }}>
                                                    Delete
                                            </button> */}
                                                </StyledTableCell>
                                            </StyledTableRow>
                                        );
                                    })}
                                </TableBody>
                            </Table>
                        </TableContainer>
                        {/* <input type="submit" /> */}
                    </form>
                    {/*}
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
                            </div>

*/}

                    {/* <img className="event-image" src={`data:image/png;base64,${aux.image}`} alt="Event" /> */}

                    {/*<Image src={`data:image/png;base64,${aux.image}`} className="product-gallery__image" layout="raw" width={"400px"} height={"400px"}/>*/}
                    {/* <Content product={event}/> */}

                {/* </div> */}
            </section>
        </Layout>
    );
}

export default MyEvent
