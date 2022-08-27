import * as React from 'react';
import Typography from '@mui/material/Typography';
import Link from 'next/link';
import { useRouter } from "next/router";
import Image from "next/image";
import Searchbar from "./Searchbar"
import { SvgIcon } from '@mui/material';
import userIcon from '../public/images/svg/user.svg';

const navigationRoutes = [ "eventos", "contacto pa" ]

export default function Navbar() {
    const router = useRouter();
    return (
        <div className="nav_container">
        
            <nav className="nav_bar container">
                <Link href="/" passHref>
                    <a className=""><img className="logo" src="/favicon.ico"/></a>
                </Link>
                <div className="menu_desktop">
                    {navigationRoutes.map((singleRoute) => {
                        return (
                            <NavigationLink
                                key={singleRoute}
                                href={`/${singleRoute}`}
                                text={singleRoute}
                                router={router}
                            />
                        );
                    })}
                </div>
                <Searchbar/>
                <div className="menu_desktop_right">
                    {/* <SvgIcon component={userIcon}/> */}
                    <Image src={userIcon} className="icon"/>
                    {/* <img className="icon" src="/images/svg/user.svg"/> */}
                </div>
            </nav>
        </div>
    );
}

function NavigationLink( { href, text, router } ) {
    const isActive = router.asPath === (href === "/home" ? "/" : href);
    return (
        <Link href={href === "/home" ? "/" : href} passHref>
            <a
                href={href === "/home" ? "/" : href}
                className={`${isActive && "nav_item_active"} nav_item`}>
                <span>{text}</span>
            </a>
        </Link>
    );
}

