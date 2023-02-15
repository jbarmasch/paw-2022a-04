import {useEffect, useMemo, useState, useRef} from 'react';
import Layout from '../layout';
import {server, fetcher} from '../../utils/server';
import useSwr from "swr";
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

    const {
        data: locations,
        isLoading: locationsLoading,
    } = useSWRImmutable(`${server}/api/locations`, fetcher)
    const [location, setLocation] = useState();
    const {
        data: tags,
        isLoading: tagsLoading,
    } = useSWRImmutable(`${server}/api/tags?locale=${i18n.language}`, fetcher)
    const [tag, setTag] = useState();
    const {
        data: types,
        isLoading: typesLoading,
    } = useSWRImmutable(`${server}/api/types?locale=${i18n.language}`, fetcher)
    const [date, setDate] = useState(null);

    const [active, setActive] = useState(false)
    const [imageName, setImageName] = useState()
    const [image, setImage] = useState()

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
        error: errorData
    } = useSwr(props.match.params.id ? `${server}/api/events/${props.match.params.id}` : null, fetcher)

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
                    console.log(d)
                    setTickets(d)
                    setValue("tickets", tickets);
                })
        }
    }, [event])

    const {data: aux, error: error} = useSwr(event ? `${event.image}` : null, fetcher)

    const {register, control, handleSubmit, reset, watch, setValue, getValues, formState: {errors}} = useForm({
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

    if (error || errorData) return <p>No data</p>
    if (!aux || !event || !user) return <MyEventLoading/>

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

        await resi

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

                    const newTicket = {...defaultValues};

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
                } else {
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

            await res;
        }

        mutate()
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
                <form className="form container my-event-page" onSubmit={handleSubmit(onSubmit)}>
                    <div className="my-event-content">
                        <div className="contain">
                            <img className="event-image" src={`data:image/png;base64,${aux.image}`} alt="Event"/>
                            {!!event.soldOut && <span className="event-image-sold-out">{i18n.t("event.soldOut")}</span>}
                        </div>
                        <Paper className="event-info" elevation={2}>
                            <div className="event-actions">
                                {!edit ?
                                    <>
                                        <IconButton onClick={() => setEdit(true)}
                                                    type="submit"><EditRoundedIcon/></IconButton>
                                        <IconButton><DeleteRoundedIcon/></IconButton>
                                    </> :
                                    <>
                                        <IconButton onClick={() => {
                                            setEdit(false)
                                        }}><DoneRoundedIcon/></IconButton>
                                        <IconButton onClick={() => setEdit(false)}><ClearRoundedIcon/></IconButton>
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
                                                                console.log(item)
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
                                            defaultValue={event.date}
                                            render={({field: {ref, onBlur, name, onChange, ...field}, fieldState}) => (
                                                <FormControl className="my-event-input">
                                                    <LocalizationProvider dateAdapter={AdapterDayjs}
                                                                          adapterLocale={i18n.language != 'en' && i18n.language != 'es' ? 'en' : i18n.language}>
                                                        <InputLabel className="casper"
                                                                    htmlFor="event-date-input">{i18n.t("event.date")}</InputLabel>
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
                                        <h4>{i18n.t("event.tags")}</h4>
                                        <Controller
                                            control={control}
                                            defaultValue={theTags}
                                            name="tags"
                                            render={({field: {onChange, value, name, ref}}) => {
                                                const handleSelectChange = (selectedOption) => {
                                                    onChange(selectedOption.target.value);
                                                    console.log(selectedOption.target.value)
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
                                            value={activeMin !== 'undefined' ? activeMin : (event.minAge ? true : false)}
                                            checked={activeMin !== 'undefined' ? activeMin : (event.minAge ? true : false)}
                                            control={<Checkbox onClick={() => {
                                                console.log(activeMin)
                                                setActiveMin(activeMin !== 'undefined' ? !activeMin : (event.minAge ? false : true))
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
                                                                 disabled={activeMin !== 'undefined' ? !activeMin : (event.minAge ? false : true)}
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
                                            }}><AddRoundedIcon/></IconButton>
                                        }

                                    </StyledTableCell>
                                </StyledTableRow>
                            </TableHead>
                            <TableBody>
                                {fields.length === 0 ?

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
                                                                validate: {
                                                                    min: (x) => {
                                                                        return x >= 1 || i18n.t("myEvents.ticketsPerUserError")
                                                                    },
                                                                    max: (x) => {
                                                                        return x <= 10 || i18n.t("myEvents.ticketsPerUserError")
                                                                    }
                                                                }
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
                                                        <Controller
                                                            control={control}
                                                            name={`tickets[${index}].starting`}
                                                            rules={{
                                                            }}
                                                            defaultValue={date}
                                                            render={({
                                                                         field: {ref, onBlur, name, onChange, ...field},
                                                                         fieldState
                                                                     }) => (
                                                                <FormControl>
                                                                    <LocalizationProvider dateAdapter={AdapterDayjs}
                                                                                          adapterLocale={i18n.language != 'en' && i18n.language != 'es' ? 'en' : i18n.language}>
                                                                        <InputLabel sx={{display: "none"}}
                                                                                    htmlFor={`tickets[${index}].starting`}>{i18n.t("bookings.starting")}</InputLabel>
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
                                                                            onChange={(event) => {
                                                                                console.log(event.toISOString());
                                                                                onChange(event.toISOString());
                                                                                setDate(event.toISOString());
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
                                                            defaultValue={date}
                                                            render={({
                                                                         field: {ref, onBlur, name, onChange, ...field},
                                                                         fieldState
                                                                     }) => (
                                                                <FormControl>
                                                                    <LocalizationProvider dateAdapter={AdapterDayjs}
                                                                                          adapterLocale={i18n.language != 'en' && i18n.language != 'es' ? 'en' : i18n.language}>
                                                                        <InputLabel sx={{display: "none"}}
                                                                                    htmlFor={`tickets[${index}].until`}>{i18n.t("bookings.until")}</InputLabel>
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
                                                                            onChange={(event) => {
                                                                                console.log(event.toISOString());
                                                                                onChange(event.toISOString());
                                                                                setDate(event.toISOString());
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

                                                    <IconButton onClick={() => {
                                                        if (item.ticketId) {
                                                            console.log(item.ticketId)
                                                            rowsData.push(item.ticketId)
                                                            setRowsData(rowsData)
                                                        }
                                                        remove(index)
                                                    }}><DeleteRoundedIcon/></IconButton>

                                                </StyledTableCell>
                                            </StyledTableRow>
                                        );
                                    })}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </form>

            </section>
        </Layout>
    );
}

export default MyEvent
