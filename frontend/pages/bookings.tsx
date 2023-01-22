import Layout from '../layouts/Main';
import Footer from '../components/footer';
import Breadcrumb from '../components/breadcrumb';
import BookingsContent from "../components/bookings-content";

const Bookings = () => (
    <Layout>
        <Breadcrumb text={"All Bookings"}/>
        <section className="products-page">
            <div className="container">
                <BookingsContent userId={"4"}/>
            </div>
        </section>
        <Footer/>
    </Layout>
)

export default Bookings