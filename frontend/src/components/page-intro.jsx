import i18n from '../i18n'
import landingImage from '../assets/images/intro.jpg'
import Button from "@mui/material/Button";
import MoneyOffCsredRoundedIcon from '@mui/icons-material/MoneyOffCsredRounded';
import CelebrationRoundedIcon from '@mui/icons-material/CelebrationRounded';
import ChairRoundedIcon from '@mui/icons-material/ChairRounded';

export default function PageIntro() {
    return (
        <section className="page-intro">

            <div className="page-intro__container">
                <img className="page-intro__slide" alt="Landing" src={landingImage}/>
                <h2 className="page-intro__content vertical">
                    {i18n.t("home.phrase")}
                    <div><Button color={"secondary"} variant={"contained"}>{i18n.t("seeEvents")}</Button></div>
                </h2>
            </div>

            <div className="shop-data">
                <div className="container">
                    <ul className="shop-data__items">
                        <li className="home-item">
                            <MoneyOffCsredRoundedIcon className="home-icon"/>
                            <div className="data-item__content">
                                <h4>{i18n.t("home.createTitle")}</h4>
                                <p>{i18n.t("home.createDesc")}</p>
                            </div>
                        </li>

                        <li className="home-item">
                            <CelebrationRoundedIcon className="home-icon"/>
                            <div className="data-item__content">
                                <h4>{i18n.t("home.rangeTitle")}</h4>
                                <p>{i18n.t("home.rangeDesc")}</p>
                            </div>
                        </li>

                        <li className="home-item">
                            <ChairRoundedIcon className="home-icon"/>
                            <div className="data-item__content">
                                <h4>{i18n.t("home.bookTitle")}</h4>
                                <p>{i18n.t("home.bookDesc")}</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </section>
    )
};

// export default PageIntro;