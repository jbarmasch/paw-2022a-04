import Layout from "../layout"
import EventsContent from '../events-content/events-content';

const Events = () => {
    return (
        <Layout>
            <section className="products-page">
                <div className="container">
                    <EventsContent/>
                </div>
            </section>
        </Layout>
    )
}

export default Events;
