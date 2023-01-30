import Layout from '../../layouts/Main';
import {useForm} from "react-hook-form";
import {server} from '../../utils/server';
import {postData} from '../../utils/services';
import { useTranslation } from 'next-i18next'
import { getStaticPaths, makeStaticProps } from '../../utils/get-static'
import Link from '../../components/Link'

type ForgotMail = {
    email: string;
}

const ForgotPassword = () => {
    const { t } = useTranslation(['common']);

    const {register, handleSubmit, errors} = useForm();

    const onSubmit = async (data: ForgotMail) => {
        const res = await postData(`${server}/api/login`, {
            email: data.email,
        });

        console.log(res);
    };

    return (
        <Layout t={t}>
            <section className="form-page">
                <div className="container">
                    <div className="back-button-section">
                        <Link href="/events">
                            <i className="icon-left"></i> Back to shop
                        </Link>
                    </div>

                    <div className="form-block">
                        <h2 className="form-block__title">Forgot your password?</h2>
                        <p className="form-block__description">Enter your email or phone number and recover your
                            account</p>

                        {/*<form className="form" onSubmit={handleSubmit(onSubmit)}>*/}
                        {/*    <div className="form__input-row">*/}
                        {/*        <input*/}
                        {/*            className="form__input"*/}
                        {/*            placeholder="email"*/}
                        {/*            type="text"*/}
                        {/*            {...register(*/}
                        {/*                'email',*/}
                        {/*                {*/}
                        {/*                    required: true,*/}
                        {/*                    pattern: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,*/}
                        {/*                }*/}
                        {/*            )}*/}
                        {/*        />*/}

                        {/*        {errors.email && errors.email.type === 'required' &&*/}
                        {/*        <p className="message message--error">This field is required</p>*/}
                        {/*        }*/}

                        {/*        {errors.email && errors.email.type === 'pattern' &&*/}
                        {/*        <p className="message message--error">Please write a valid email</p>*/}
                        {/*        }*/}
                        {/*    </div>*/}

                        {/*    <div className="form__input-row">*/}
                        {/*        <input*/}
                        {/*            className="form__input"*/}
                        {/*            type="password"*/}
                        {/*            placeholder="Password"*/}
                        {/*            {...register(*/}
                        {/*                'password',*/}
                        {/*                {*/}
                        {/*                    required: true*/}
                        {/*                })}*/}
                        {/*        />*/}
                        {/*        {errors.password && errors.password.type === 'required' &&*/}
                        {/*        <p className="message message--error">This field is required</p>*/}
                        {/*        }*/}
                        {/*    </div>*/}

                        {/*    <button type="submit" className="btn btn--rounded btn--yellow btn-submit">Reset password*/}
                        {/*    </button>*/}
                        {/*</form>*/}
                    </div>

                </div>
            </section>
        </Layout>
    )
}

export default ForgotPassword

const getStaticProps = makeStaticProps(['common'])
export { getStaticPaths, getStaticProps }
