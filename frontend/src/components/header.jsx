import { useEffect, useRef, useState } from 'react';
import useOnClickOutside from 'use-onclickoutside';
import i18n from "../i18n";
import { Link, useHistory, useLocation } from 'react-router-dom'
import { checkLogin } from '../utils/auth'
import LoginOutlinedIcon from '@mui/icons-material/LoginOutlined';
import IconButton from '@mui/material/IconButton';
import Avatar from '@mui/material/Avatar';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import Divider from '@mui/material/Divider';
import ListItemIcon from '@mui/material/ListItemIcon';
import SearchOutlinedIcon from '@mui/icons-material/SearchOutlined';
import Logout from '@mui/icons-material/Logout';
import {amber} from '@mui/material/colors';

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

    let accessToken;
    let refreshToken;

    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
        refreshToken = localStorage.getItem("Refresh-Token");
    }

    const [logged, setLogged] = useState(false)

    useEffect(() => {
        checkLogin(accessToken, refreshToken).then(x => setLogged(x))
    }, [accessToken, refreshToken])

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

    return (
        <header className={`site-header ${!onTop ? 'site-header--fixed' : ''}`}>
            <div className="container">
                <Link to="/">
                    <h1 className={"site-logo " + ((path && path == "/") ? "header-link-active" : "")}>{/*<Logo/>*/}BotPass</h1>
                </Link>
                <nav ref={navRef} className={`site-nav ${menuOpen ? 'site-nav--open' : ''}`}>
                    <Link className={"header-link " + (path?.includes("/events") ? "header-link-active" : "")} to="/events">
                        {i18n.t("nav.events")}
                    </Link>
                    <Link className={"header-link " + (path?.includes("/organizers") ? "header-link-active" : "")} to="/organizers">
                        {i18n.t("nav.organizers")}
                    </Link>
                    <Link className={"header-link " + ((path && path == "/create-event") ? "header-link-active" : "")} to="/create-event">
                        {i18n.t("nav.createEvent")}
                    </Link>
                    <button className="site-nav__btn">{i18n.t("nav.account")}</button>
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
                                placeholder='Search event'
                                aria-label='Search event'
                            />
                        </form>
                        <IconButton onClick={() => setSearchOpen(!searchOpen)} className="icon-search"><SearchOutlinedIcon style={{ color: onTop && !searchOpen ? 'white' : 'black' }}/></IconButton>
                    </div>
                    {!logged &&
                        <Link to="/login"><IconButton><LoginOutlinedIcon style={{ color: onTop ? 'white' : 'black' }}/></IconButton></Link>
                    }
                    {logged &&
                        <>
                            <IconButton
                                onClick={handleClick}
                                size="small"
                                sx={{ ml: 2 }}
                                aria-controls={open ? 'account-menu' : undefined}
                                aria-haspopup="true"
                                aria-expanded={open ? 'true' : undefined}
                            >
                                <Avatar  sx={{ width: 32, height: 32, color: 'black'}}>M</Avatar>
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
                                        <Avatar /> Profile
                                    </MenuItem>
                                </Link>
                                <Link to="/my-events">
                                    <MenuItem className={"nav-link"} onClick={handleClose}>
                                        <Avatar /> My events
                                    </MenuItem>
                                </Link>
                                <Link to="/bookings">
                                    <MenuItem className={"nav-link"} onClick={handleClose}>
                                        <Avatar /> Bookings
                                    </MenuItem>
                                </Link>
                                <Divider />
{/*                                <MenuItem onClick={handleClose}>
                                    <ListItemIcon>
                                        <PersonAdd fontSize="small" />
                                    </ListItemIcon>
                                    Add another account
                                </MenuItem>
                                <MenuItem onClick={handleClose}>
                                    <ListItemIcon>
                                        <Settings fontSize="small" />
                                    </ListItemIcon>
                                    Settings
                            </MenuItem> */}
                                <MenuItem onClick={handleClose}>
                                    <ListItemIcon>
                                        <Logout fontSize="small" />
                                    </ListItemIcon>
                                    Logout
                                </MenuItem>
                            </Menu>
                        </>
                    }
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
