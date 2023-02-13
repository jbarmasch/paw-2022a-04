import Layout from "../layout"
import ProductsContent from '../products-content/products-content';
import i18n from '../../i18n'
import { Link, useLocation } from 'react-router-dom'
import queryString from 'query-string'
import errorImage from '../../assets/images/500.png'

const ServerError = () => {
    return (
        <Layout>
            <section className="errors-page">
                <div className="container error-container">
                    <img className="error" alt="Error" src={errorImage} />
                </div>
            </section>
        </Layout>
    )
}

export default ServerError;
