// import React from "react";
// import Card from '@mui/material/Card';
// import i18n from '../../i18n'
// import landingImage from '../../assets/images/intro.jpg'



// // import { Button, Card, Container, Row, Col } from "reactstrap";

// // import DemoNavbar from "components/Navbars/DemoNavbar.js";
// // import SimpleFooter from "components/Footers/SimpleFooter.js";

// const Profile = (props) => {    



//     return (
//       <>
//                 <img className="profile-intro-image" alt="Landing" src={landingImage}/>

//                 <div className="profile-data">
//                 <div className="container">
//                     <ul className="profile-data__items">
//                         <li>
//                             <i className="icon-shipping"></i>
//                             <div className="data-item__content">
//                                 <h4>Create your event for free</h4>
//                                 <p>Use BotPass to have an extended reach</p>
//                             </div>
//                         </li>

//                         <li>
//                             <i className="icon-shipping"></i>
//                             <div className="data-item__content">
//                                 <h4>Attend events internationally</h4>
//                                 <p>Go BotPass or go home!</p>
//                             </div>
//                         </li>

//                         <li>
//                             <i className="icon-cash"></i>
//                             <div className="data-item__content">
//                                 <h4>Go to your favorite events remotely or in person</h4>
//                                 <p>We offer a wide variety of events</p>
//                             </div>
//                         </li>
//                     </ul>
//                 </div>
//             </div>
//       </>
//     );
//   }

// export default Profile;


import { useState } from 'react';
import Layout from '../layout';
// import ProductsFeatured from '../../../components/products-featured';
// import Description from '../../../components/product-single/description';
import { server } from '../../utils/server';
import useSwr from "swr";
import UserLoading from "../users-content/user-loading";
// import Content from "../../../components/product-single/content";
import Select from "react-select";
// import {postData} from "../../utils/services";
import { useForm, Controller } from "react-hook-form";
import * as React from "react";
// import {FormattedMessage, useIntl} from "react-intl";
import i18n from '../../i18n'
import { Link } from 'react-router-dom'
import landingImage from '../../assets/images/intro.jpg'

const Profile = (props) => {
    const fetcher = (url) => fetch(url).then((res) => res.json());

    let accessToken;
    let refreshToken;
    let userId;

    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
        refreshToken = localStorage.getItem("Refresh-Token");
        userId = localStorage.getItem("User-ID");
        console.log(userId)
    }

    const { data: user, error: error } = useSwr(userId ? `${server}/api/users/${userId}` : null, fetcher)

    const { data: userStats, error: errorStats } = useSwr(userId ? `${server}/api/users/${userId}/stats` : null, fetcher)

    if (error || errorStats) return <p>No data</p>
    if (!user || !userStats) return <UserLoading />

    console.log(user)


    return (
        <Layout>
            {/*
            <section className="product-single">
                <div className="container">
                    <div className="product-single__info">
                        <div className="product-single__info-btns">
                            <span>{user.username}</span>
                            <br />
                            <span>{user.rating} ({user.votes})</span>
                        </div>

                    </div>
                </div>
            </section> */}
            <section className="profile-page">
                <div style={{ position: "relative" }}>
                    <img className="profile-intro-image" alt="Landing" src={landingImage} />

                    <div className="profile-title">
                        <div className="center column">
                            {/* <h4>{user.rating} ({user.votes})</h4> */}

                            <h3 className="profile-name">{user.username}</h3>
                            <h3 className="profile-mail">{user.mail}</h3>

                            <table className="ticket-table">
                        <thead>
                            <tr>
                                <th>{i18n.t("stats.stats")}</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                                <tr>
                                    <td>{i18n.t("stats.ticketsBooked")}</td>
                                    <td className="right-text">{userStats.eventsAttended}</td>
                                </tr>
                                <tr>
                                    <td>{i18n.t("stats.favType")}</td>
                                    <td className="right-text">{userStats.bookingsMade}</td>
                                </tr>
                                <tr>
                                    <td>{i18n.t("stats.eventsAttended")}</td>
                                    <td className="right-text">{userStats.favLocation.name}</td>
                                </tr>
                                <tr>
                                    <td>{i18n.t("stats.favLocation")}</td>
                                    <td className="right-text">{userStats.favType.name}</td>
                                </tr>
                        </tbody>
                    </table>

                        </div>
                    </div>
                </div>
            </section>
        </Layout>
    );
}

export default Profile;
