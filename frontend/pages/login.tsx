import Layout from '../layouts/Main';
import Link from 'next/link';
import {useForm} from "react-hook-form";
import {server} from '../utils/server';
import * as React from "react";
import {useRouter} from "next/router";
import {useEffect} from "react";

type LoginMail = {
    username: string;
    password: string;
}

const LoginPage = () => {
    const router = useRouter();

    const {register, handleSubmit, control, watch, formState: {errors}} = useForm();

    // useEffect(() => {
    //     if (!router.isReady) return;
    //     console.log(router.query)
    // }, [router.isReady]);

    const onSubmit = async (data: LoginMail) => {
        let authorization = data.username + ":" + data.password
        console.log(authorization)

        const res = await fetch(`${server}/api/users/test`, {
            method: "GET",
            headers: {
                // 'Content-Type': 'application/json',
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

        if (router.query && router.query.pathname) {
            router.push(router.query.pathname);
        } else {
            router.push("/")
        }
    };

    return (
        <Layout>
            <section className="form-page">
                <div className="container">
                    <div className="back-button-section">
                        <Link href="/events">
                            <i className="icon-left"></i> Back to store
                        </Link>
                    </div>

                    <div className="form-block">
                        <h2 className="form-block__title">Log in</h2>
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
                                            // pattern: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
                                        }
                                    )}
                                />


                                {errors.username?.type === 'required' &&
                                    <p className="message message--error">This field is required</p>
                                }

                                {/*{errors.email?.type === 'pattern' &&*/}
                                {/*<p className="message message--error">Please write a valid email</p>*/}
                                {/*}*/}
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

                            <div className="form__btns">
                                <button type="button" className="btn-social fb-btn"><i className="icon-facebook"></i>Facebook
                                </button>
                                <button type="button" className="btn-social google-btn"><img
                                    src="/images/icons/gmail.svg" alt="gmail"/> Gmail
                                </button>
                            </div>

                            <button type="submit" className="btn btn--rounded btn--yellow btn-submit">Sign in</button>

                            <p className="form__signup-link">Not a member yet? <a href="/register">Sign up</a></p>
                        </form>
                    </div>

                </div>
            </section>
        </Layout>
    )
}

export default LoginPage
