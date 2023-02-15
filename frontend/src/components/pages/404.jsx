import Layout from "../layout"
import errorImage from '../../assets/images/404.png'

const PageNotFound = () => {
    return (
        <Layout>
            <section className="errors-page">
                <div className="container error-container">
                    <img width="40vw" height="28vw" className="error" alt="Error" src={errorImage}/>
                </div>
            </section>
        </Layout>
    )
}

export default PageNotFound;
