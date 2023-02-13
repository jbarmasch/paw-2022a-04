import landingImage from '../../assets/images/intro.jpg'
import { Link, useHistory, useLocation } from 'react-router-dom'
import Layout from "../layout"
import SimilarEvents from '../products-content/similar-events';
import RecommendedEvents from '../products-content/recommended-events';
import i18n from '../../i18n'
import queryString from 'query-string'
import useSwr from 'swr';
const fetcher = (...args) => fetch(...args).then((res) => res.json())

const ThankYou = () => {
    const { state } = useLocation()
    const history = useHistory()

    if (!state) {
        history.push("/")
    }

    let booking = state.booking
    let eventId = state.eventId

    const { data, error } = useSwr(`${booking}`, fetcher)

    if (error) return <p>No data</p>
    if (!data) return <p>sss</p>

    console.log(data)

    return (
        <Layout>
            <section className="thank-you-page">
            <img className="event-image" src={`data:image/png;base64,${data.image}`} alt="Event" />
                <div style={{position: "relative"}}>
                    <img className="thank-you-image" alt="Landing" src={landingImage} />
                    <h2 className="thank-you-content">{i18n.t("thankYou.phrase")}</h2>
                </div>

                <SimilarEvents id={eventId}/>
                <RecommendedEvents id={eventId}/>
            </section>
        </Layout>
    )
}

export default ThankYou;
