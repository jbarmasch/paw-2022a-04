import Layout from '../layout';
import MyEventsContent from "../my-events-content/my-events-content";
import {useAuth} from '../../utils/useAuth';

const MyEvents = () => {
    const {user} = useAuth();

    return (
        <Layout>
            <section className="products-page">
                <div className="container">
                    <MyEventsContent userId={user.id}/>
                </div>
            </section>
        </Layout>
    )
}

export default MyEvents;
