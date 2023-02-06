import {useEffect, useRef, useState} from 'react';
import useOnClickOutside from 'use-onclickoutside';
// import Logo from '../../assets/icons/logo';
// import {useRouter} from 'next/router';
// import {postData} from "../../utils/services";
// import {server} from "../../utils/server";
import i18n from "../i18n";
import {Link} from 'react-router-dom'
import { useLocation } from "react-router-dom";

export const useFindPath = () => {
    const location = useLocation();
    const [currentPath, setCurrentPath] = useState();
    useEffect(() => {
        setCurrentPath(location.pathname);
    }, [location]);
    return currentPath;
};

const Header = () => {
    const path = useFindPath();
    const arrayPaths = ['/'];

    const [onTop, setOnTop] = useState((!arrayPaths.includes(path)) ? false : true);
    const [menuOpen, setMenuOpen] = useState(false);
    const [searchOpen, setSearchOpen] = useState(false);
    const navRef = useRef(null);
    const searchRef = useRef(null);

    const headerClass = () => {
        if (window.pageYOffset === 0) {
            setOnTop(true);
        } else {
            setOnTop(false);
        }
    }

    useEffect(() => {
        if (!arrayPaths.includes(path)) {
            return;
        }

        headerClass();
        window.onscroll = function () {
            headerClass();
        };
    }, [path]);

    const closeMenu = () => {
        setMenuOpen(false);
    }

    const closeSearch = () => {
        setSearchOpen(false);
    }

    useOnClickOutside(navRef, closeMenu);
    useOnClickOutside(searchRef, closeSearch);

    const [query, setQuery] = useState('')

    const handleParam = setValue => e => setValue(e.target.value)

    const preventDefault = f => e => {
        e.preventDefault()
        f(e)
    }

    const handleSubmit = preventDefault(() => {
        // router.push({
        //     pathname: "/events",
        //     query: {search: query},
        // })
    })

    return (
        <header className={`site-header ${!onTop ? 'site-header--fixed' : ''}`}>
        {/* <header className={`site-header`}> */}
            <div className="container">
                <Link to="/">
                    <h1 className="site-logo">{/*<Logo/>*/}BotPass</h1>
                </Link>
                <nav ref={navRef} className={`site-nav ${menuOpen ? 'site-nav--open' : ''}`}>
                    <Link className="header-link" to="/events">
                    {/* <Link className="header-link" to="/events?filter=top&origin=im"> */}
                        {i18n.t("nav.events")}
                    </Link>
                    <Link className="header-link" to="/create-event">
                        {i18n.t("nav.createEvent")}
                    </Link>
                    <Link className="header-link" to="/bookings">
                        {i18n.t("nav.bookings")}
                    </Link>
                    <Link className="header-link" to="/my-events">
                        {i18n.t("nav.myEvents")}
                    </Link>
                    <Link className="header-link" to="/users">
                        {i18n.t("nav.organizers")}
                    </Link>
                    <button className="site-nav__btn">{i18n.t("nav.account")}</button>
                </nav>

                <div className="site-header__actions">
                    <button ref={searchRef}
                            className={`search-form-wrapper ${searchOpen ? 'search-form--active' : ''}`}>
                        <form className={`search-form`} onSubmit={handleSubmit}>
                            <i className="icon-cancel" onClick={() => setSearchOpen(!searchOpen)}></i>
                            <input
                                type='text'
                                name='search'
                                value={query}
                                onChange={handleParam(setQuery)}
                                placeholder='Search event'
                                aria-label='Search event'
                            />
                        </form>
                        <i onClick={() => setSearchOpen(!searchOpen)} className="icon-search"></i>
                    </button>
                    <Link to="/login">
                        <button className="site-header__btn-avatar"><i className="icon-avatar"></i></button>
                    </Link>
                    <button
                        onClick={() => setMenuOpen(true)}
                        className="site-header__btn-menu">
                        <i className="btn-hamburger"><span></span></i>
                    </button>
                </div>
            </div>
        </header>
    )
};


export default Header;
