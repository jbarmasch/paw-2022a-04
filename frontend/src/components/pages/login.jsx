import Layout from '../layout';
import {useForm} from "react-hook-form";
import {server} from '../../utils/server';
import * as React from "react";
import {useEffect} from "react";
import i18n from '../../i18n'
import {Link, useHistory, useLocation} from 'react-router-dom'
import useFindPath from '../header'
import queryString from 'query-string'

const Login = () => {
    const history = useHistory();
    let path = useFindPath();
    const { search } = useLocation()
    const values = queryString.parse(search)

    const {register, handleSubmit, control, watch, formState: {errors}} = useForm();

    const onSubmit = async (data) => {
        let authorization = data.username + ":" + data.password

        const res = await fetch(`${server}/api/users/test`, {
            method: "GET",
            headers: {
                'Authorization': `Basic ${btoa(authorization)}`
            }
        })

        if (res.status != 200) {
            alert("NO EXISTIS PIBE")
            return
        }

        localStorage.setItem("Access-Token", res.headers.get("Access-Token"))
        localStorage.setItem("Refresh-Token", res.headers.get("Refresh-Token"))
        localStorage.setItem("User-ID", res.headers.get("User-ID"))

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
                                <input
                                    className="form__input input__text"
                                    placeholder="Username"
                                    id="email-input"
                                    type="text"
                                    {...register(
                                        "username",
                                        {
                                            required: true,
                                        }
                                    )}
                                />

                                {errors.username?.type === 'required' &&
                                    <p className="message message--error">This field is required</p>
                                }
                            </div>

                            <div className="form__input-row">
                                <input
                                    className="form__input input__text"
                                    type="password"
                                    placeholder="Password"
                                    {...register(
                                        'password',
                                        {
                                            required: true
                                        })}
                                />
                                {errors.password?.type === 'required' &&
                                    <p className="message message--error">This field is required</p>
                                }
                            </div>

                            <div className="form__info">
                                <div className="checkbox-wrapper">
                                    <label htmlFor="check-signed-in" className={`checkbox checkbox--sm`}>
                                        <input
                                            type="checkbox"
                                            name="keepSigned"
                                            id="check-signed-in"
                                        />
                                        <span className="checkbox__check"></span>
                                        <p>Keep me signed in</p>
                                    </label>
                                </div>
                                <a href="/forgot-password" className="form__info__forgot-password">Forgot password?</a>
                            </div>

                            <button type="submit" className="btn btn--rounded btn--yellow btn-submit">{i18n.t("login.signIn")}</button>

                            <p className="form__signup-link">{i18n.t("login.notAMember")}<Link to="/register">{i18n.t("login.signUp")}</Link></p>
                        </form>
                    </div>

                </div>
            </section>
        </Layout>
    )
}

export default Login
