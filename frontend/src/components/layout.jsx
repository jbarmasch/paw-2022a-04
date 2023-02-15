import Footer from "./footer"
import Header from "./header"
import {useFindPath} from './header'

const Layout = ({children}) => {
    let path = useFindPath();

    return (
        <>
            <Header/>
            <main className={(path !== '/' ? 'main-page' : '')}>
                {children}
            </main>
            {path !== '/login' && (<Footer/>)}
        </>
    )
}

export default Layout
