import Layout from "../layout"
import ProductsContent from '../events-content/events-content';

const Events = () => {
    return (
        <Layout>
            <section className="products-page">
                <div className="container">
                    <ProductsContent/>
                </div>
            </section>
        </Layout>
    )
}

export default Events;
