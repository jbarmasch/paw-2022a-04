import Layout from "../layout"
import RecommendedContent from '../products-content/recommended-content';
import i18n from '../../i18n'
import {Link, useLocation} from 'react-router-dom'
import queryString from 'query-string'

const RecommendedEvents = () => {
    return (
    <Layout>
        <section className="products-page">
            <div className="container">
                <RecommendedContent/>
            </div>
        </section>
    </Layout>
)
}

export default RecommendedEvents;
