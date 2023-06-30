import { useEffect, useRef, useState } from 'react';
import useOnClickOutside from 'use-onclickoutside';
import i18n from "../i18n";
import { Link, useHistory, useLocation } from 'react-router-dom'
import LoginOutlinedIcon from '@mui/icons-material/LoginOutlined';
import IconButton from '@mui/material/IconButton';
import Avatar from '@mui/material/Avatar';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import Divider from '@mui/material/Divider';
import Button from '@mui/material/Button';
import ListItemIcon from '@mui/material/ListItemIcon';
import SearchOutlinedIcon from '@mui/icons-material/SearchOutlined';
import Logout from '@mui/icons-material/Logout';
import MenuRoundedIcon from '@mui/icons-material/MenuRounded';
import { useAuth } from '../utils/useAuth';
import BotPassLogo from '../assets/images/logo-right.png';
import BotPassIntroLogo from '../assets/images/logo-right-intro.png';
import LocalActivityRoundedIcon from '@mui/icons-material/LocalActivityRounded';
import BookOnlineRoundedIcon from '@mui/icons-material/BookOnlineRounded';
import { LazyLoadImage } from 'react-lazy-load-image-component';

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
    const history = useHistory();

    const { user } = useAuth();

    const [onTop, setOnTop] = useState(arrayPaths.includes(path));
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
        history.push(`/events?search=${query}`);
    })

    const [anchorEl, setAnchorEl] = useState(null);
    const open = Boolean(anchorEl);
    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    const logout = () => {
        localStorage.removeItem("Access-Token")
        localStorage.removeItem("Refresh-Token")
        localStorage.removeItem("User-ID")
        localStorage.removeItem("user")
        history.go(0)
    }

    return (
        <header className={`site-header ${!onTop ? 'site-header--fixed' : ''}`}>
            <div className="container">
                <Link to="/">
                    {!onTop ?
                        <LazyLoadImage
                            className="logo"
                            component="img"
                            height="50px"
                            width="126px"
                            src={BotPassLogo}
                            alt="BotPass"
                        />

                        :
                        <LazyLoadImage
                            className="logo"
                            component="img"
                            height="50px"
                            width="126px"
                            src={BotPassIntroLogo}
                            alt="BotPass"
                        />
                    }
                </Link>
                <nav ref={navRef} className={`site-nav ${menuOpen ? 'site-nav--open' : ''}`}>
                    <Link className={"header-link " + (path?.includes("/events") ? "header-link-active" : "")}
                        to="/events">
                        {i18n.t("nav.events")}
                    </Link>
                    <Link className={"header-link " + (path?.includes("/organizers") ? "header-link-active" : "")}
                        to="/organizers">
                        {i18n.t("nav.organizers")}
                    </Link>
                    <Link className={"header-link " + ((path && path === "/create-event") ? "header-link-active" : "")}
                        to="/create-event">
                        {i18n.t("nav.createEvent")}
                    </Link>
                    <Link className="site-nav__btn" to="/profile">
                        {i18n.t("nav.profile")}
                    </Link>
                    {user?.role === "ROLE_CREATOR" && 
                    <Link className="site-nav__btn" to="/my-events">
                        {i18n.t("nav.myEvents")}
                    </Link>}
                    <Link className="site-nav__btn" to="/bookings">
                        {i18n.t("nav.bookings")}
                    </Link>
                    <Button className="site-nav__btn" onClick={logout}>
                        {i18n.t("nav.logout")}
                    </Button>
                </nav>

                <div className="site-header__actions">
                    <div ref={searchRef}
                        className={`search-form-wrapper ${searchOpen ? 'search-form--active' : ''}`}>
                        <form className={`search-form`} onSubmit={handleSubmit}>
                            <i className="icon-cancel" onClick={() => setSearchOpen(!searchOpen)}></i>
                            <input
                                type='text'
                                name='search'
                                value={query}
                                onChange={handleParam(setQuery)}
                                placeholder={i18n.t("nav.searchEvent")}
                                aria-label={i18n.t("nav.searchEvent")}
                            />
                        </form>
                        <IconButton onClick={() => setSearchOpen(!searchOpen)} aria-label={i18n.t("nav.searchEvent")}
                            className="icon-search"><SearchOutlinedIcon alt={i18n.t("nav.searchEvent")}
                                style={{ color: onTop && !searchOpen ? 'white' : 'black' }} /></IconButton>
                    </div>
                    {!user &&
                        <Link to="/login"><IconButton><LoginOutlinedIcon
                            style={{ color: onTop ? 'white' : 'black' }} /></IconButton></Link>
                    }
                    {user &&
                        <>
                            <IconButton
                                onClick={handleClick}
                                size="small"
                                sx={{ ml: 2 }}
                                aria-controls={open ? 'account-menu' : undefined}
                                aria-haspopup="true"
                                aria-expanded={open ? 'true' : undefined}
                            >
                                <Avatar sx={{
                                    width: 32,
                                    height: 32,
                                    color: 'black',
                                    bgcolor: "secondary"
                                }}>{user.username[0].toUpperCase()}</Avatar>
                            </IconButton>
                            <Menu
                                anchorEl={anchorEl}
                                id="account-menu"
                                open={open}
                                onClose={handleClose}
                                onClick={handleClose}
                                PaperProps={{
                                    elevation: 0,
                                    sx: {
                                        overflow: 'visible',
                                        filter: 'drop-shadow(0px 2px 8px rgba(0,0,0,0.32))',
                                        mt: 1.5,
                                        '& .MuiAvatar-root': {
                                            width: 32,
                                            height: 32,
                                            ml: -0.5,
                                            mr: 1,
                                        },
                                        '&:before': {
                                            content: '""',
                                            display: 'block',
                                            position: 'absolute',
                                            top: 0,
                                            right: 14,
                                            width: 10,
                                            height: 10,
                                            bgcolor: 'background.paper',
                                            transform: 'translateY(-50%) rotate(45deg)',
                                            zIndex: 0,
                                        },
                                    },
                                }}
                                transformOrigin={{ horizontal: 'right', vertical: 'top' }}
                                anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
                            >
                                <Link to="/profile">
                                    <MenuItem className={"nav-link"} onClick={handleClose}>
                                        <Avatar />{i18n.t("nav.profile")}
                                    </MenuItem>
                                </Link>
                                {user?.roles?.includes("ROLE_CREATOR") && <Link to="/my-events">
                                    <MenuItem className={"nav-link"} onClick={handleClose}>
                                        <LocalActivityRoundedIcon className="nav-menu-icon" />{i18n.t("nav.myEvents")}
                                    </MenuItem>
                                </Link>}
                                <Link to="/bookings">
                                    <MenuItem className={"nav-link"} onClick={handleClose}>
                                        <BookOnlineRoundedIcon className="nav-menu-icon" />{i18n.t("nav.bookings")}
                                    </MenuItem>
                                </Link>
                                <Divider />
                                <MenuItem className={"nav-link"} onClick={handleClose}>
                                    <ListItemIcon>
                                        <Button className="icon-button" onClick={logout}
                                            startIcon={<Logout fontSize="small" />}>
                                            {i18n.t("nav.logout")}
                                        </Button>
                                    </ListItemIcon>
                                </MenuItem>
                            </Menu>
                        </>
                    }
                    <IconButton
                        onClick={() => setMenuOpen(true)}
                        className="site-header__btn-menu"
                        style={{ color: onTop ? 'white' : 'black' }}>
                        <MenuRoundedIcon />
                    </IconButton>
                </div>
            </div>
        </header>
    )
};


export default Header;
