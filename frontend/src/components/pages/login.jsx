import Layout from '../layout';
import {useForm, Controller} from "react-hook-form";
import {server} from '../../utils/server';
import {useAuth} from '../../utils/useAuth';
import * as React from "react";
import {useState} from "react";
import i18n from '../../i18n'
import {Link, useHistory, useLocation} from 'react-router-dom'
import queryString from 'query-string'
import TextField from '@mui/material/TextField';
import Checkbox from '@mui/material/Checkbox';
import FormControl from '@mui/material/FormControl';
import FormHelperText from '@mui/material/FormHelperText';
import Button from '@mui/material/Button';
import InputAdornment from '@mui/material/InputAdornment';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import IconButton from '@mui/material/IconButton';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import FormControlLabel from '@mui/material/FormControlLabel';

const Login = () => {
    const {login} = useAuth();

    const history = useHistory();
    const {search} = useLocation()
    const values = queryString.parse(search)
    const [error, setError] = useState(false)

    const {register, handleSubmit, control, watch, formState: {errors}} = useForm();

    const [showPassword, setShowPassword] = React.useState(false);

    const handleClickShowPassword = () => setShowPassword((show) => !show);

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    const onSubmit = async (data) => {
        let authorization = data.username + ":" + data.password

        const res = await fetch(`${server}/api/users`, {
            method: "GET",
            headers: {
                'Authorization': `Basic ${btoa(authorization)}`
            }
        })

        if (res.status !== 200) {
            setError(true)
            return;
        }

        localStorage.setItem("Access-Token", res.headers.get("Access-Token"))
        localStorage.setItem("Refresh-Token", res.headers.get("Refresh-Token"))
        localStorage.setItem("User-ID", res.headers.get("User-ID"))

        const bad = await fetch(`${server}/api/users/${res.headers.get("User-ID")}`, {
            method: "GET",
            headers: {
                'Authorization': `Basic ${btoa(authorization)}`
            }
        })

        let user = await bad.json();
        user["accessToken"] = res.headers.get("Access-Token")
        user["refreshToken"] = res.headers.get("Refresh-Token")
        login(user)

        if (values?.redirectTo) {
            history.push(values.redirectTo)
        } else {
            history.push("/")
        }
    };

    return (
        <Layout>
            <section className="form-page">
                <div className="container">
                    <div className="form-block">
                        <h2 className="form-block__title">{i18n.t("login.login")}</h2>
                        <form className="form" onSubmit={handleSubmit(onSubmit)}>
                            <div className="form__input-row">
                                <div className="form__input-row">

                                    <Controller
                                        name="username"
                                        rules={{required: i18n.t('fieldRequired')}}
                                        control={control}
                                        defaultValue={''}
                                        render={({field, fieldState}) => {
                                            return (
                                                <FormControl className="full-width-input">
                                                    <TextField id="username-input" label={i18n.t("login.username")}
                                                               variant="outlined"
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

                                </div>

                                <Controller
                                    name="password"
                                    rules={{required: i18n.t('fieldRequired')}}
                                    control={control}
                                    defaultValue={''}
                                    render={({field, fieldState}) => {
                                        return (
                                            <FormControl className="full-width-input">
                                                <InputLabel
                                                    htmlFor="password-input">{i18n.t("login.password")}</InputLabel>
                                                <OutlinedInput
                                                    id="password-input"
                                                    label={i18n.t("login.password")}
                                                    error={!!fieldState.error}
                                                    {...field}
                                                    type={showPassword ? 'text' : 'password'}
                                                    autoComplete='on'
                                                    endAdornment={
                                                        <InputAdornment position="end">
                                                            <IconButton
                                                                aria-label="toggle password visibility"
                                                                onClick={handleClickShowPassword}
                                                                onMouseDown={handleMouseDownPassword}
                                                                edge="end"
                                                            >
                                                                {showPassword ? <VisibilityOff/> : <Visibility/>}
                                                            </IconButton>
                                                        </InputAdornment>
                                                    }
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

                            </div>

                            {error && <FormHelperText error>{i18n.t("login.notFound")}</FormHelperText>}

                            <div className="form-actions">
                                <Button type="submit" variant="contained">{i18n.t("login.signIn")}</Button>
                            </div>
                            <p className="form__signup-link">{i18n.t("login.notAMember")}<Link
                                to="/register">{i18n.t("login.signUp")}</Link></p>
                        </form>
                    </div>

                </div>
            </section>
        </Layout>
    )
}

export default Login
