import Layout from '../layout';
import UsersContent from '../users-content/users-content';

const Users = () => {
    return (
        <Layout>
            <section className="products-page">
                <div className="container">
                    <UsersContent/>
                </div>
            </section>
        </Layout>
    )
}

export default Users