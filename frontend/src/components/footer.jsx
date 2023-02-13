import i18n from '../i18n'
import { Link, useLocation, useHistory } from 'react-router-dom'

const Footer = () => {
    return (
        <footer className="site-footer">
            <div className="container">
                <div className="site-footer__top">
                    <div className="site-footer__description">
                        <h6>{/*<Logo/>*/} BotPass</h6>
                        <p>{i18n.t("footer.text")}</p>
                    </div>

                    <div className="site-footer__links">
                        <ul>
                            <li>{i18n.t("footer.events")}</li>
                            <li><Link to="/events">{i18n.t("footer.events")}</Link></li>
                            <li><Link to="/recommended">{i18n.t("footer.recommended")}</Link></li>
                            <li><Link to="/popular">{i18n.t("footer.popular")}</Link></li>
                        </ul>
                        <ul>
                            <li>{i18n.t("footer.contact")}</li>
                            <li><a href="mailto:botpass@zohomail.com">botpass@zohomail.com</a></li>
                        </ul>
                    </div>
                </div>
            </div>

            <div className="site-footer__bottom">
                <div className="container">
                    <p>BottlerTeam - Barmasch, Bellver & Lo Coco</p>
                </div>
            </div>
        </footer>
    )
};


export default Footer