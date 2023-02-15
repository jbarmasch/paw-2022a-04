import Layout from '../layout';
import BookingsContent from "../bookings-content/bookings-content";
import BookingsLoading from "../bookings-content/bookings-loading";
import {useAuth} from '../../utils/useAuth';

const Bookings = () => {
    const {user} = useAuth();

    if (!user) {
        return <BookingsLoading/>
    }

    return (
        <Layout>
            <section className="products-page">
                <div className="container">
                    <BookingsContent userId={user.id}/>
                </div>
            </section>
        </Layout>
    )
}

export default Bookings
