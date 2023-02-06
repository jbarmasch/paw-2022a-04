import Layout from "../layout"
// import ProductsFilter from '../products-filter';
import ProductsContent from '../products-content/products-content';
import i18n from '../../i18n'
import {Link, useLocation} from 'react-router-dom'
import queryString from 'query-string'


const Events = () => {
    return (

    <Layout>
        <section className="products-page">
            <div className="container">
                {/* <ProductsFilter t={t}/> */}
                <ProductsContent/>
            </div>
        </section>
    </Layout>
)
}

export default Events;
