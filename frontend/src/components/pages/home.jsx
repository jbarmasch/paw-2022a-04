import React from 'react'
import i18n from "../../i18n";
import PageIntro from "../page-intro"
import Layout from "../layout"
import {Link} from "react-router-dom"
import UpcomingEvents from "../events-content/upcoming-events";
import FewTicketsEvents from "../events-content/few-tickets-events";

export default function Home() {
    return (
        <Layout>
            <PageIntro/>
      
            <div style={{marginTop: "40px"}}>
                <UpcomingEvents/>
                <FewTicketsEvents/>
            </div>
        </Layout>
    )
}