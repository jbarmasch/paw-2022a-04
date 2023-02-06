import Footer from "./footer"
import Header from "./header"
import {useFindPath} from './header'

const Layout = ({ children }) => {
    let path = useFindPath();

    return (
        <>
            <Header/>
            {/* <main className="main">{children}</main> */}
            <main className={(path !== '/' ? 'main-page' : '')}>
                {children}
            </main>
            <Footer />
        </>
  )
}
  
 export default Layout

//  <div className="app-main">
//  <Head>
//  </Head>

//  <Header t={t}/>

//  <main className={(pathname !== '/' ? 'main-page' : '')}>
//      {children}
//  </main>
// </div>