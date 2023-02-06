import Layout from '../../layouts/Main';
import Footer from '../../components/footer';
import Breadcrumb from '../../components/breadcrumb';
import ProductsFilter from '../../components/products-filter';
import ProductsContent from '../../components/products-content';
import { useTranslation } from 'next-i18next'
import { getStaticPaths, makeStaticProps } from '../../utils/get-static'
import Link from '../../components/Link'

const Events = () => {
    const { t } = useTranslation(['common'])

    return (

    <Layout t={t}>
        <Breadcrumb text={"All Events"}/>
        <section className="products-page">
            <div className="container">
                {/* <ProductsFilter t={t}/> */}
                <ProductsContent t={t}/>
            </div>
        </section>
        <Footer/>
    </Layout>
)
}

export default Events;

const getStaticProps = makeStaticProps(['common'])
export { getStaticPaths, getStaticProps }
  