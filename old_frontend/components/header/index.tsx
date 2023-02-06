import {useEffect, useRef, useState} from 'react';
import useOnClickOutside from 'use-onclickoutside';
import Logo from '../../assets/icons/logo';
import {useRouter} from 'next/router';
import {postData} from "../../utils/services";
import {server} from "../../utils/server";
// import {FormattedMessage} from "react-intl";
import Link from '../../components/Link'

type HeaderType = {
    isErrorPage?: Boolean;
    t
}

const Header = ({isErrorPage, t}: HeaderType) => {
    const router = useRouter();
    const arrayPaths = ['/'];

    const [onTop, setOnTop] = useState((!arrayPaths.includes(router.pathname) || isErrorPage) ? false : true);
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
        if (!arrayPaths.includes(router.pathname) || isErrorPage) {
            return;
        }

        headerClass();
        window.onscroll = function () {
            headerClass();
        };
    }, []);

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
        router.push({
            pathname: "/events",
            query: {search: query},
        })
    })

    return (
        <header className={`site-header ${!onTop ? 'site-header--fixed' : ''}`}>
            <div className="container">
                <Link href="/">
                    <h1 className="site-logo"><Logo/>BotPass</h1>
                </Link>
                <nav ref={navRef} className={`site-nav ${menuOpen ? 'site-nav--open' : ''}`}>
                    <Link className="header-link" href="/events">
                        {t("nav.events")}
                    </Link>
                    <Link className="header-link" href="/create-event">
                        {t("nav.createEvent")}
                    </Link>
                    <Link className="header-link" href="/bookings">
                        {t("nav.bookings")}
                    </Link>
                    <Link className="header-link" href="/my-events">
                        {t("nav.myEvents")}
                    </Link>
                    <Link className="header-link" href="/users">
                        {t("nav.organizers")}
                    </Link>
                    <button className="site-nav__btn">{t("nav.account")}</button>
                </nav>

                <div className="site-header__actions">
                    <button ref={searchRef}
                            className={`search-form-wrapper ${searchOpen ? 'search-form--active' : ''}`}>
                        <form className={`search-form`} onSubmit={handleSubmit}>
                            <i className="icon-cancel" onClick={() => setSearchOpen(!searchOpen)}></i>
                            {/*<input type="text" name="search" placeholder="Enter the product you are looking for"/>*/}
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
                    <Link href="/login">
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
