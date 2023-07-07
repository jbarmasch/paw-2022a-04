import Layout from "../layout"
import { useForm, Controller } from "react-hook-form";
import {useEffect, useState, setValue} from "react";
import {server} from '../../utils/server';
import i18n from "../../i18n";
import FormControl from "@mui/material/FormControl";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormHelperText from "@mui/material/FormHelperText";
import * as React from "react";
import {useAuth} from '../../utils/useAuth';
import {Link, useHistory, useLocation} from 'react-router-dom'
import queryString from 'query-string'
import {Alert, Snackbar} from "@mui/material";

const RegisterPage = () => {
    const { register, handleSubmit, control, getValues, watch, formState: { errors }, setError } = useForm();
    const {login} = useAuth();
    const [active, setActive] = useState(false)
    const history = useHistory();
    const {search} = useLocation()
    const values = queryString.parse(search)
    // const [error, setError] = useState(false)
    const [usernameError, setUsernameError] = useState(false)
    const [mailError, setMailError] = useState(false)
    const [openSnackbar, setOpenSnackbar] = useState(false);

    const onSubmit = async (data) => {
        const res = await fetch(`${server}/api/users`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        })
        
        let json = await res;

        if (json.status === 201) {
            let authorization = data.username + ":" + data.password
    
            const aux = await fetch(json.headers.get("Location"), {
                method: "GET",
                headers: {
                    'Authorization': `Basic ${btoa(authorization)}`
                }
            })

            if (!aux.ok) {
                setOpenSnackbar(true)
                return
            }
    
            let user = await aux.json();

            localStorage.setItem("Access-Token", aux.headers.get("Access-Token"))
            localStorage.setItem("Refresh-Token", aux.headers.get("Refresh-Token"))
            localStorage.setItem("User-ID", aux.headers.get("User-ID"))

            user["accessToken"] = aux.headers.get("Access-Token")
            user["refreshToken"] = aux.headers.get("Refresh-Token")
            login(user)
    
            if (values?.redirectTo) {
                history.push(values.redirectTo)
            } else {
                history.push("/")
            }
        } else if (json.status === 400) {
            let errors = await json.json()
            errors.forEach(x => {
                let variable = String(x["path"]).split(".").slice(-1)[0]
                switch (variable) {
                    case "username":
                        console.log(x)
                        setError('username', { type: 'custom', message: x['message'] });
                        break
                    case "mail":
                        setError('mail', { type: 'custom', message: x['message'] });
                        break
                    case "password":
                        setError('password', { type: 'custom', message: x['message'] });
                        break
                    case "repeatPassword":
                        setError('repeatPassword', { type: 'custom', message: x['message'] });
                        break
                    default:
                        break;
                }
            })
        } else {
            setOpenSnackbar(true)
        }
    }

    let vertical = "top"
    let horizontal = "right"

    const handleClose = () => {
        setOpenSnackbar(false)
    }

    return (
        <Layout>
            <Snackbar
                anchorOrigin={{ vertical, horizontal }}
                open={openSnackbar}
                onClose={handleClose}
                key={vertical + horizontal}
                autoHideDuration={15000}
            >
                <Alert severity="error" onClose={handleClose}>{i18n.t("error.api")}</Alert>
            </Snackbar>

            <section className="form-page">
                <div className="container">
                    <div className="form-block">
                        <h2 className="form-block__title">{i18n.t("register.register")}</h2>

                        <form className="form" onSubmit={handleSubmit(onSubmit)}>
                            <div className="form__input-row">
                                <Controller
                                    name="username"
                                    rules={{
                                        required: i18n.t('fieldRequired'),
                                        validate: {
                                            minLength: (x) => {return x.length >= 8 || i18n.t("register.usernameLenError")},
                                            maxLength: (x) => {return x.length <= 100 || i18n.t("register.usernameLenError")},
                                        }
                                    }}
                                    control={control}
                                    defaultValue={''}
                                    render={({field, fieldState}) => {
                                        return (
                                            <FormControl className="full-width-input">
                                                <TextField id="username-input" label={i18n.t("register.username")}
                                                            autoComplete='off'
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

                            {usernameError && <FormHelperText error>{usernameError}</FormHelperText>}
                            </div>

                            <div className="form__input-row">
                                <Controller
                                    name="mail"
                                    rules={{
                                        required: i18n.t('fieldRequired'),
                                        validate: {
                                            pattern: (x) => {
                                                const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
                                                return reg.test(x) || i18n.t("register.mailPatternError")
                                        }
                                    }}}
                                    control={control}
                                    defaultValue={''}
                                    render={({field, fieldState}) => {
                                        return (
                                            <FormControl className="full-width-input">
                                                <TextField id="email-input" label={i18n.t("register.mail")}
                                                            autoComplete='off'
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

                            <div className="form__input-row">
                                <Controller
                                    name="password"
                                    rules={{required: i18n.t('fieldRequired'),
                                        validate: {
                                            minLength: (x) => {return x.length >= 8 || i18n.t("register.passwordLenError")},
                                            maxLength: (x) => {return x.length >= 8 || i18n.t("register.passwordLenError")},
                                            pattern: (x) => {
                                                const reg = /[a-zA-Z0-9]+/
                                                return reg.test(x) || i18n.t("register.passwordPatternError")
                                            }
                                    }}}
                                    control={control}
                                    defaultValue={''}
                                    render={({field, fieldState}) => {
                                        return (
                                            <FormControl className="full-width-input">
                                                <TextField id="password-input" label={i18n.t("register.pass")}
                                                            autoComplete="new-password"
                                                            type="password"
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

                            <div className="form__input-row">
                                <Controller
                                    name="repeatPassword"
                                    rules={{required: i18n.t('fieldRequired'),
                                        validate: {
                                            match: (x) => {
                                                const { password } = getValues();
                                                return password === x || i18n.t("register.passwordMatchError");
                                            },
                                        }}
                                    }
                                    control={control}
                                    defaultValue={''}
                                    render={({field, fieldState}) => {
                                        return (
                                            <FormControl className="full-width-input">
                                                <TextField id="repeat-password-input" label={i18n.t("register.repeat")}
                                                            autoComplete="new-password"
                                                            type="password"
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

                            <div className="center full-width"><Button variant="contained" type="submit">{i18n.t("login.signUp")}</Button></div>

                        </form>
                    </div>

                </div>
            </section>
        </Layout>
    )
}

export default RegisterPage
