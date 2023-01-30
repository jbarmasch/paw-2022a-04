import Layout from '../../layouts/Main';
import Footer from '../../components/footer';
import Breadcrumb from '../../components/breadcrumb';
import ProductsFilter from '../../components/products-filter';
import UsersContent from '../../components/users-content';
import { useTranslation } from 'next-i18next'
import { getStaticPaths, makeStaticProps } from '../../utils/get-static'
import Link from '../../components/Link'

const Events = () => {
    const { t } = useTranslation(['common']);
    
    return (

        <Layout t={t}>
            <section className="products-page">
                <div className="container">
                    {/*<UserFilter/>*/}
                    <UsersContent/>
                </div>
            </section>
            <Footer/>
        </Layout>
    )
}

export default Events

const getStaticProps = makeStaticProps(['common'])
export { getStaticPaths, getStaticProps }
