import Layout from '../layout';
import * as React from 'react';
import {useForm, Controller} from "react-hook-form";
import i18n from '../../i18n'
import {server, fetcher} from '../../utils/server'
import {Link, useHistory, useLocation} from 'react-router-dom'
import useSWRImmutable from 'swr/immutable'
import {useEffect, useState, useRef} from "react";
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormHelperText from '@mui/material/FormHelperText';
import FormControl from '@mui/material/FormControl';
import Select, {SelectChangeEvent} from '@mui/material/Select';
import Button from '@mui/material/Button';
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';
import Autocomplete from '@mui/material/Autocomplete';
import {DateTimePicker} from '@mui/x-date-pickers/DateTimePicker';
import {LocalizationProvider} from '@mui/x-date-pickers/LocalizationProvider';
import TextField from '@mui/material/TextField';
import IconButton from '@mui/material/IconButton';
import ClearRoundedIcon from '@mui/icons-material/ClearRounded';
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs';
import 'dayjs/locale/es';
import 'dayjs/locale/en';
import {useAuth} from '../../utils/useAuth';
import {LoadingPage} from '../../utils/loadingPage'
import {Alert, Snackbar} from "@mui/material";

const CreateEvent = () => {
    const history = useHistory();

    let accessToken;
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
    }

    const {user} = useAuth();
    const {refresh} = useAuth();

    const {
        data: locations,
        isLoading: locationsLoading,
        error: locationsError
    } = useSWRImmutable(`${server}/api/locations`, fetcher)
    const [location, setLocation] = useState([]);
    const {
        data: tags,
        isLoading: tagsLoading,
        error: tagsError
    } = useSWRImmutable(`${server}/api/tags`, fetcher)
    const [tag, setTag] = useState([]);
    const {
        data: types,
        isLoading: typesLoading,
        error: typesError
    } = useSWRImmutable(`${server}/api/types`, fetcher)
    const [date, setDate] = useState(null);

    const {handleSubmit, control, formState: {errors}, setError} = useForm();
    const [active, setActive] = useState(false)
    const [imageName, setImageName] = useState()
    const [image, setImage] = useState()
    const inputRef = useRef(null);
    const [openSnackbar, setOpenSnackbar] = useState(false);

    let start = 14;
    let ages = [];
    while (start <= 27) {
        ages.push({
            value: start,
            label: start
        })
        start++;
    }

    async function onImageChange(e) {
        e.preventDefault()
        if (e.target.files[0].size > (1024 * 1024)) {
            setError('image', {type: 'custom', message: i18n.t("create.imageError")});
        } else {
            setImageName(e.target.files[0].name)
            await setImage(e.target.files[0])
        }
    }

    const fixDate = (date) => {
        let dateAux = new Date(date)
        let timestamp = dateAux.getTime() - dateAux.getTimezoneOffset() * 60000;
        return  new Date(timestamp).toISOString().slice(0, -8)
    }

    const onSubmit = async (data) => {
        let correctDate = fixDate(data.date)

        let obj = {
            name: data.name,
            location: data.location.value,
            type: data.type,
            date: correctDate,
        }

        if (data.description) {
            obj.description = data.description
        }

        if (data.tags) {
            obj.tags = data.tags
        }

        if (data.hasMinAge) {
            obj.hasMinAge = data.hasMinAge
            obj.minAge = data.minAge
        }

        const formData = new FormData();
        formData.append('form', new Blob([JSON.stringify(obj)], {
            type: "application/json"
        }));

        const res = await fetch(`${server}/api/events`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${accessToken}`
            },
            body: formData
        })

        let json = await res;

        if (json.ok) {
            let eventId = json.headers.get("Location")?.split("/").slice(-1)[0]

            if (image) {
                const formData = new FormData();
                formData.append('image', image, image.name)
                formData.append('form', new Blob([JSON.stringify(obj)], {
                    type: "application/json"
                }));

                const res = await fetch(`${server}/api/events/${eventId}/image`, {
                    method: 'PUT',
                    headers: {
                        'Authorization': `Bearer ${accessToken}`
                    },
                    body: formData
                })

                let json = await res;

                if (!json.ok) {
                    let errors = await json.json()
                    setError('image', {type: 'custom', message: errors['message']});
                    return;
                }
            }

            refresh()
            history.push("/my-events/" + eventId)
        } else {
            let errors = await json.json()
            if (errors.constructor !== Array) {
                setOpenSnackbar(true)
            } else {
                errors.forEach(x => {
                    if (!x["path"]) {
                        setOpenSnackbar(true)
                    } else {
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
                    }
                })
            }
        }
    }

    if (!user || locationsLoading || tagsLoading || typesLoading) {
        return <LoadingPage/>
    }

    if (locationsError || tagsError || typesError) {
        history.push("/404")
        return
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
                <Alert severity="error" onClose={handleCloseSnackbar}>{i18n.t("error.api")}</Alert>
            </Snackbar>

            <section className="form-page">
                <div className="container">
                    <div className="form-block">
                        <h2 className="form-block__title">{i18n.t("nav.createEvent")}</h2>
                        <form className="form create-form" onSubmit={handleSubmit(onSubmit)}>

                            <Controller
                                name="name"
                                rules={{required: i18n.t('fieldRequired')}}
                                control={control}
                                defaultValue={''}
                                render={({field, fieldState}) => {
                                    return (
                                        <FormControl sx={{width: 120}}>
                                            <TextField id="name-input" label={i18n.t("create.name")} variant="outlined"
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

                            <Controller
                                name="description"
                                control={control}
                                defaultValue={''}
                                render={({field, fieldState}) => {
                                    return (
                                        <FormControl sx={{width: 120}}>
                                            <TextField id="description-input" label={i18n.t("create.description")}
                                                       variant="outlined"
                                                       {...field} />
                                        </FormControl>
                                    );
                                }}
                            />

                            <Controller
                                name="location"
                                rules={{required: i18n.t('fieldRequired')}}
                                control={control}
                                defaultValue={location}
                                render={({field: {onChange}, fieldState}) => {
                                    return (
                                        <FormControl sx={{width: 120}}>
                                            <Autocomplete
                                                disablePortal
                                                id="location-autocomplete"
                                                options={locationList}
                                                noOptionsText={i18n.t("autocompleteNoOptions")}
                                                onChange={(event, item) => {
                                                    onChange(item);
                                                    setLocation(item)
                                                }}
                                                isOptionEqualToValue={(option, value) => {
                                                    return option.value === value.value
                                                }}
                                                renderInput={(params) => <TextField {...params}
                                                                                    label={i18n.t("create.location")}
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

                            <Controller
                                name="type"
                                rules={{required: i18n.t('fieldRequired')}}
                                control={control}
                                defaultValue={''}
                                render={({field, fieldState}) => {
                                    return (
                                        <FormControl sx={{width: 120}}>
                                            <InputLabel id="type-select-label"
                                                        error={!!fieldState.error}>{i18n.t("create.type")}</InputLabel>
                                            <Select
                                                id="type-select"
                                                label={i18n.t("create.type")}
                                                labelId="type-select-label"
                                                error={!!fieldState.error}
                                                inputProps={{ "data-testid": "select-type-input" }}
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

                            <Controller
                                control={control}
                                defaultValue={''}
                                name="tags"
                                render={({field: {onChange, value, name, ref}}) => {
                                    const handleSelectChange = (selectedOption) => {
                                        onChange(selectedOption.target.value);
                                        setTag(selectedOption.target.value);
                                    };

                                    return (
                                        <>
                                            <FormControl>
                                                <InputLabel id="tags-select-label">{i18n.t("create.tags")}</InputLabel>
                                                <Select
                                                    labelId="tags-select-label"
                                                    id="tags-select"
                                                    multiple
                                                    value={tag}
                                                    onChange={handleSelectChange}
                                                    input={<OutlinedInput label={i18n.t("create.tags")}/>}
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
                                defaultValue={date}
                                render={({field: {ref, onBlur, name, onChange, ...field}, fieldState}) => (
                                    <FormControl sx={{width: 120}}>
                                        <LocalizationProvider dateAdapter={AdapterDayjs}
                                                              adapterLocale={i18n.language !== 'en' && i18n.language !== 'es' ? 'en' : i18n.language}>
                                            <DateTimePicker
                                                renderInput={(inputProps) => (
                                                    <TextField
                                                        {...inputProps}
                                                        onBlur={onBlur}
                                                        name={name}
                                                        error={!!fieldState.error}
                                                        data-testid="dateTimePicker"
                                                    />)}
                                                label={i18n.t("create.date")}
                                                onChange={(event) => {
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


                            <div className="horizontal align-center">
                                <FormControlLabel
                                    value={false}
                                    control={<Checkbox onClick={() => setActive(!active)}/>}
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
                                    defaultValue={''}
                                    render={({field, fieldState}) => {
                                        return (
                                            <FormControl disabled={!active}
                                                         className={"min-age-select " + (!active ? "select-disabled" : "")}>
                                                <InputLabel id="stackoverflow-label"
                                                            error={!!fieldState.error}>{i18n.t("create.minAge")}</InputLabel>
                                                <Select
                                                    id="age-select"
                                                    label={i18n.t("create.minAge")}
                                                    labelId="minAge-id"
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
                            <div className="horizontal align-center">
                                <Controller
                                    name="image"
                                    control={control}
                                    defaultValue={''}
                                    render={({field, fieldState}) => (
                                        <>
                                            <input
                                                accept="image/*"
                                                hidden
                                                id="file-upload-button"
                                                type="file"
                                                ref={inputRef}
                                                onChange={e => {
                                                        field.onChange(e.target.files);
                                                        onImageChange(e)
                                                    }
                                                }
                                            />
                                            <label htmlFor="file-upload-button">
                                                <Button variant="raised" component="span">
                                                    {i18n.t("create.uploadFile")}
                                                </Button>
                                            </label>
                                            {imageName && (<>
                                                <span>{imageName}</span>
                                                <IconButton onClick={() => {
                                                    setImageName(null)
                                                    inputRef.current.value = null
                                                }}><ClearRoundedIcon/></IconButton>
                                            </>)}
                                            {fieldState.error ? (
                                                <FormHelperText error>
                                                    {fieldState.error?.message}
                                                </FormHelperText>
                                            ) : null}
                                        </>
                                    )}
                                />
                            </div>
                            <div className="form-actions">
                                <Button variant="contained" type="submit" data-testid="button-submit">{i18n.t("submit")}</Button>
                            </div>
                        </form>
                    </div>

                </div>
            </section>
        </Layout>
    )
}

export default CreateEvent
