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

            {/* <section className="featured">
                <div className="container">
                    <article
                        style={{backgroundImage: 'url(http://www.fedracongressi.com/fedra/wp-content/uploads/2016/02/revelry-event-designers-homepage-slideshow-38.jpeg)'}}
                        className="featured-item featured-item-large">
                        <div className="featured-item__content">
                            <h3>{i18n.t("home.recommended")}</h3>
                            <Link to="/recommended" className="btn btn--rounded">Show Collection</Link>
                        </div>
                    </article>

                    <article
                        style={{backgroundImage: 'url(https://images.unsplash.com/photo-1429962714451-bb934ecdc4ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8Y29uY2VydHxlbnwwfHwwfHw%3D&w=1000&q=80)'}}
                        className="featured-item featured-item-small-first">
                        <div className="featured-item__content">
                            <h3>{i18n.t("home.trending")}</h3>
                            <Link to="/few-tickets" className="btn btn--rounded">More details</Link>
                        </div>
                    </article>

                    <article
                        style={{backgroundImage: 'url(https://www.incimages.com/uploaded_files/image/1920x1080/getty_614867390_321301.jpg)'}}
                        className="featured-item featured-item-small">
                        <div className="featured-item__content">
                            <h3>{i18n.t("home.featured")}</h3>
                            <a href="https://www.youtube.com/watch?v=eBGIQ7ZuuiU" className="btn btn--rounded">VIEW
                                ALL</a>
                        </div>
                    </article>
                </div>

            </section> */}
            <div style={{marginTop: "40px"}}>
                <UpcomingEvents/>
                <FewTicketsEvents/>
            </div>
        </Layout>
    )
}