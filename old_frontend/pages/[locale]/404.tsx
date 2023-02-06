import LayoutError from '../../layouts/404';
import { useTranslation } from 'next-i18next'
import { getStaticPaths, makeStaticProps } from '../../utils/get-static'
import Link from '../../components/Link'

const ErrorPage = () => {
    const { t } = useTranslation(['common'])

    return (
    <LayoutError>
        <section className="error-page">
            <div className="container">
                <h1>Error 404</h1>
                <p>Whoops. Looks like this page doesn't exist</p>
                <a href="/" className="btn btn--rounded btn--yellow">Go to home</a>
            </div>
        </section>
    </LayoutError>
)}

export default ErrorPage

const getStaticProps = makeStaticProps(['common'])
export { getStaticPaths, getStaticProps }
