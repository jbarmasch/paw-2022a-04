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

const CreateEvent = () => {
    const history = useHistory();

    // TODO: mover a context
    let accessToken;
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
    }

    const {user} = useAuth();

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
    } = useSWRImmutable(`${server}/api/tags?locale=${i18n.language}`, fetcher)
    const [tag, setTag] = useState([]);
    const {
        data: types,
        isLoading: typesLoading,
        error: typesError
    } = useSWRImmutable(`${server}/api/types?locale=${i18n.language}`, fetcher)
    const [type, setType] = useState([]);
    const [minAge, setMinAge] = useState();
    const [date, setDate] = useState(null);

    const {handleSubmit, control, formState: {errors}} = useForm();
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

    const onSubmit = async (data) => {
        console.log(data)

        let auxi = new Date(data.date).toISOString().slice(0, -8)
        console.log(auxi)

        let obj = {
            name: data.name,
            location: data.location.value,
            type: data.type,
            date: auxi,
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

        console.log(obj)

        const res = await fetch(`${server}/api/events`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(obj)
        })

        let json = await res;

        if (json.status !== 201) {
            return;
        }

        let eventId = json.headers.get("Location")?.split("/").slice(-1)[0]

        console.log(data.image)
        console.log(data.image[0])

        const formData = new FormData();

        // const file = data.image[0];
        // let blob = file.slice(0, file.size, file.type);
        // let newFile = new File([blob], file.name, {
        //     type: file.type,
        // });

        formData.append("image", data.image[0])

        const resImage = await fetch(`${server}/api/events/${eventId}/image`, {
            method: 'POST',
            headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${accessToken}`
            },
            body: formData
        })

        let jsonImage = await resImage;

        if (jsonImage.status === 201) {
            history.push("/my-events/" + eventId)
        }
    }

    if (!user) {
        return <></>
    }

    // TODO: hacer loading
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

    return (
        <Layout>
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
                                                    console.log(item)
                                                    onChange(item);
                                                    setLocation(item)
                                                }}
                                                isOptionEqualToValue={(option, value) => {
                                                    console.log(value.value + " " + option.value);
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
                                defaultValue={''} // <---------- HERE
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
                                                    />)}
                                                label={i18n.t("create.date")}
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
                                    // defaultValue={image}
                                    rules={{
                                        required: i18n.t('fieldRequired'),
                                    }}
                                    render={({field}) => (
                                        <>
                                            <input
                                                accept="image/*"
                                                hidden
                                                id="file-upload-button"
                                                type="file"
                                                ref={inputRef}
                                                onChange={e => {
                                                    field.onChange(e.target.files);
                                                    console.log(e.target.files[0])
                                                    setImageName(e.target.files[0].name)
                                                }}
                                            />
                                            <label htmlFor="file-upload-button">
                                                <Button variant="raised" component="span">
                                                    {i18n.t("create.uploadFile")}
                                                </Button>
                                            </label>
                                            {imageName && (<>
                                                <span>{imageName}</span>
                                                <IconButton onClick={() => {
                                                    console.log(image)
                                                    setImageName(null)
                                                    inputRef.current.value = null
                                                }}><ClearRoundedIcon/></IconButton>
                                            </>)}
                                        </>
                                    )}
                                />
                            </div>
                            <div className="form-actions">
                                <Button variant="contained" type="submit">{i18n.t("submit")}</Button>
                            </div>
                        </form>
                    </div>

                </div>
            </section>
        </Layout>
    )
}

export default CreateEvent
