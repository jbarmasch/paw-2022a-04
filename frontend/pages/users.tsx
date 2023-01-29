import Layout from '../layouts/Main';
import Footer from '../components/footer';
import Breadcrumb from '../components/breadcrumb';
import ProductsFilter from '../components/products-filter';
import UsersContent from '../components/users-content';

const Events = () => (
    <Layout>
        <Breadcrumb text={"All Events"}/>
        <section className="products-page">
            <div className="container">
                {/*<UserFilter/>*/}
                <UsersContent/>
            </div>
        </section>
        <Footer/>
    </Layout>
)

export default Events
  