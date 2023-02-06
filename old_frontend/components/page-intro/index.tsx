import {Swiper, SwiperSlide} from 'swiper/react';
import SwiperCore, {EffectFade, Navigation} from 'swiper';
// import {FormattedMessage} from "react-intl";
import Link from '../../components/Link'
import Image from 'next/image';
import landingImage from '../../public/images/intro.jpg'

// SwiperCore.use([EffectFade, Navigation]);

const PageIntro = ({t}) => {
    return (
        <section className="page-intro">
            
            <div className="page-intro__container">
                <Image className="page-intro__slide" alt="Landing image" src={landingImage}/>
                <h2 className="page-intro__content">{t("home.phrase")}</h2>
            </div>

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
