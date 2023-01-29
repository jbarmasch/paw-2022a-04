import {Swiper, SwiperSlide} from 'swiper/react';
import SwiperCore, {EffectFade, Navigation} from 'swiper';
// import {FormattedMessage} from "react-intl";
import i18n from "../../utils/i18n"

SwiperCore.use([EffectFade, Navigation]);

const PageIntro = () => {

    return (
        <section className="page-intro">
            {/* <Swiper navigation effect="fade" className="swiper-wrapper"> */}
                <SwiperSlide>
                    <div className="page-intro__slide" style={{backgroundImage: "url('https://static.vecteezy.com/system/resources/previews/000/692/604/original/party-crowd-banner-design-vector.jpg')"}}>
                        <div className="container">
                            <div className="page-intro__slide__content">
                                <h2>{i18n.t("home.phrase")}</h2>
                                <a href="#" className="btn-shop"><i className="icon-right"></i>Shop now</a>
                            </div>
                        </div>
                    </div>
                </SwiperSlide>

                {/* <SwiperSlide>
                    <div className="page-intro__slide" style={{backgroundImage: "url('/images/slide-2.jpg')"}}>
                        <div className="container">
                            <div className="page-intro__slide__content">
                                <h2>Make your house into a home</h2>
                                <a href="#" className="btn-shop"><i className="icon-right"></i>Shop now</a>
                            </div>
                        </div>
                    </div>
                </SwiperSlide> */}
            {/* </Swiper> */}

            <div className="shop-data">
                <div className="container">
                    <ul className="shop-data__items">
                        <li>
                            <i className="icon-shipping"></i>
                            <div className="data-item__content">
                                <h4>Create your event for free</h4>
                                <p>Use BotPass to have an extended reach</p>
                            </div>
                        </li>

                        <li>
                            <i className="icon-shipping"></i>
                            <div className="data-item__content">
                                <h4>Attend events internationally</h4>
                                <p>Go BotPass or go home!</p>
                            </div>
                        </li>

                        <li>
                            <i className="icon-cash"></i>
                            <div className="data-item__content">
                                <h4>Go to your favorite events remotely or in person</h4>
                                <p>We offer a wide variety of events</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </section>
    )
};

export default PageIntro