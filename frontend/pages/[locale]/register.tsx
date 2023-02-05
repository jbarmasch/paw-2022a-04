import Layout from '../../layouts/Main';
import { useTranslation } from 'next-i18next'
import { getStaticPaths, makeStaticProps } from '../../utils/get-static'
import Link from '../../components/Link'
import { useForm, Controller } from "react-hook-form";
import {useRouter} from "next/router";
import {useEffect, useState} from "react";
import {server} from '../../utils/server';

const RegisterPage = () => {
    const { t } = useTranslation(['common'])

    const router = useRouter()

    const { register, handleSubmit, control, getValues, watch, formState: { errors } } = useForm();
    const [active, setActive] = useState(false)

    const onSubmit = async (data) => {
        // let obj = {
        //     username: data.username,
        //     description: data.description,
        //     location: data.location.value,
        //     type: data.type.value,
        //     tags: aux,
        //     date: auxi,
        //     hasMinAge: data.hasMinAge,
        //     minAge: data.minAge
        // }

        console.log(data)

        const res = await fetch(`${server}/api/users`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        })

        let json = await res;

        if (json.status == 201) {
            // router.push("/")
            let authorization = data.username + ":" + data.password
            console.log(authorization)
    
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
    
            if (router.query && router.query.pathname) {
                router.push(router.query.pathname);
            } else {
                router.push("/")
            }
        }
    }

    return (
        <Layout t={t}>
            <section className="form-page">
                <div className="container">
                    <div className="form-block">
                        <h2 className="form-block__title">{t("register.create")}</h2>

                        <form className="form" onSubmit={handleSubmit(onSubmit)}>
                            <div className="form__input-row">
                                <input className="form__input input__text" id="name-input" {...register("username", { required: true, maxLength: 10})} type="text" placeholder={t("register.name")}/>
                                {errors.username?.type === 'required' && <span className="message message--error">{t("fieldRequired")}</span>}
                                {errors.username?.type === 'maxLength' && <span className="message message--error">{t("usernameInvalid")}</span>}
                            </div>

                            <div className="form__input-row">
                                <input
                                    className="form__input input__text"
                                    placeholder="Mail"
                                    id="email-input"
                                    type="text"
                                    {...register(
                                        "mail",
                                        {
                                            required: true,
                                            pattern: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
                                        }
                                    )}
                                />

                                {errors.mail?.type === 'required' && <span className="message message--error">{t("fieldRequired")}</span>}
                                {errors.mail?.type === 'pattern' && <span className="message message--error">{t("mailInvalid")}</span>}
                            </div>

                            <div className="form__input-row">
                                <input className="form__input input__text" id="name-input" {...register("password", { required: true, maxLength: 10})} type="password" placeholder={t("register.pass")}/>
                                {errors.password?.type === 'required' && <span className="message message--error">{t("fieldRequired")}</span>}
                                {errors.password?.type === 'maxLength' && <span className="message message--error">{t("passwordInvalid")}</span>}
                            </div>

                            <div className="form__input-row">
                                <input className="form__input input__text" id="name-input" {...register("repeatPassword", { 
                                    required: true, 
                                    maxLength: 10,
                                    validate: (value) => {
                                        const { password } = getValues();
                                        return password === value || "Passwords should match!";
                                    }
                                      })} type="password" placeholder={t("register.repeat")}/>
                                {errors.repeatPassword?.type === 'required' && <span className="message message--error">{t("fieldRequired")}</span>}
                                {errors.repeatPassword?.type === 'maxLength' && <span className="message message--error">{t("passwordInvalid")}</span>}
                                {errors.repeatPassword?.type === 'validate' && <span className="message message--error">{t("passwordMatch")}</span>}
                            </div>

                            <button type="submit" className="btn btn--rounded btn--yellow btn-submit">Sign up</button>

                        </form>
                    </div>

                </div>
            </section>
        </Layout>
    )
}

export default RegisterPage

const getStaticProps = makeStaticProps(['common'])
export { getStaticPaths, getStaticProps }
