import React from 'react'
import i18n from "../../i18n";
import PageIntro from "../page-intro"
import Layout from "../layout"
import {Link} from "react-router-dom"

export default function Home() {
    return (
        <Layout>
            <PageIntro/>

            <section className="featured">
                <div className="container">
                    <article style={{backgroundImage: 'url(http://www.fedracongressi.com/fedra/wp-content/uploads/2016/02/revelry-event-designers-homepage-slideshow-38.jpeg)'}}
                             className="featured-item featured-item-large">
                        <div className="featured-item__content">
                            <h3>{i18n.t("home.recommended")}</h3>
                            <Link to="/recommended" className="btn btn--rounded">Show Collection</Link>
                        </div>
                    </article>

                    <article style={{backgroundImage: 'url(https://images.unsplash.com/photo-1429962714451-bb934ecdc4ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8Y29uY2VydHxlbnwwfHwwfHw%3D&w=1000&q=80)'}}
                             className="featured-item featured-item-small-first">
                        <div className="featured-item__content">
                            <h3>{i18n.t("home.trending")}</h3>
                            <Link to="/few-tickets" className="btn btn--rounded">More details</Link>
                        </div>
                    </article>

                    <article style={{backgroundImage: 'url(https://www.incimages.com/uploaded_files/image/1920x1080/getty_614867390_321301.jpg)'}}
                             className="featured-item featured-item-small">
                        <div className="featured-item__content">
                            <h3>{i18n.t("home.featured")}</h3>
                            <a href="https://www.youtube.com/watch?v=eBGIQ7ZuuiU" className="btn btn--rounded">VIEW ALL</a>
                        </div>
                    </article>
                </div>
            </section>

            <section className="section">
                <div className="container">
                    <header className="section__intro">
                        <h4>Why should you choose us?</h4>
                    </header>

                    <ul className="shop-data-items">
                        <li>
                            <i className="icon-shipping"></i>
                            <div className="data-item__content">
                                <h4>Free Shipping</h4>
                                <p>All purchases over $199 are eligible for free shipping via USPS First Class Mail.</p>
                            </div>
                        </li>

                        <li>
                            <i className="icon-payment"></i>
                            <div className="data-item__content">
                                <h4>Easy Payments</h4>
                                <p>All payments are processed instantly over a secure payment protocol.</p>
                            </div>
                        </li>

                        <li>
                            <i className="icon-cash"></i>
                            <div className="data-item__content">
                                <h4>Money-Back Guarantee</h4>
                                <p>If an item arrived damaged or you've changed your mind, you can send it
                                    back for a full refund.</p>
                            </div>
                        </li>

                        <li>
                            <i className="icon-materials"></i>
                            <div className="data-item__content">
                                <h4>Finest Quality</h4>
                                <p>Designed to last, each of our products has been crafted with the finest
                                    materials.</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </section>
        </Layout>
    )
}