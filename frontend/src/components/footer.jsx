import i18n from '../i18n'
import Divider from "@mui/material/Divider";


const Footer = () => {
    return (
        <footer className="site-footer">
            <Divider/>
            <div className="space-around container marg-top-xl marg-bot">
                <div className={"vertical text-wrap"}>
                    <h6> BotPass</h6>
                    <p className={"marg-top"}>{i18n.t("footer.text")}</p>
                </div>
                <div>&nbsp;</div>
                <div className={"vertical"}>
                    <h6>{i18n.t("footer.contact")}</h6>
                    <a className={"marg-top"} href="mailto:bottlerpass@zohomail.com">bottlerpass@zohomail.com</a>
                </div>
            </div>
            <Divider/>
            <div className="center marg-top marg-bot full-width">
                <p>BottlerTeam - Barmasch, Bellver & Lo Coco</p>
            </div>
        </footer>
    )
};


export default Footer