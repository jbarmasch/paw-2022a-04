import {server} from './server';
import {useEffect, useState} from "react";

let user = null;

export default async function getUser({router}) {
    let accessToken = null
    let refreshToken = null
    let userId = null
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
        refreshToken = localStorage.getItem("Refresh-Token");
        userId = localStorage.getItem("User-ID")
    }

    const fetchData = async (accessToken, refreshToken, pathname) => {
        let res = fetch(`${server}/api/users/test`, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            },
        })

        let aux = await res;
        if (aux.status == 200) {
            if (!(!userId || userId === "undefined")) {
                let presi = fetch(`${server}/api/users/${userId}`, {
                    headers: {
                        'Authorization': `Bearer ${accessToken}`
                    },
                })
    
                let auxi = await presi
                let au = auxi.json()
                user = au
            }
            return;
        }

        res = fetch(`${server}/api/users/test`, {
            headers: {
                'Authorization': `Bearer ${refreshToken}`
            },
        })

        aux = await res;
        if (aux.status == 200) {
            localStorage.setItem("Access-Token", aux.headers.get("Access-Token"))
            let userId = localStorage.getItem("User-ID")
            if (!(!userId || userId === "undefined")) {
                let resi = fetch(`${server}/api/users/${userId}`, {
                    headers: {
                        'Authorization': `Bearer ${accessToken}`
                    },
                })
    
                let auxi = await resi.json()
                user = auxi
            }
            return;
        }

        if (router.pathname !== "/login") {
            router.push({
                pathname: '/login',
                query: {pathname: pathname},
            }, '/login');
        }
    }

    if (accessToken && refreshToken) {
        await fetchData(accessToken, refreshToken, router.pathname)
    } else {
        const pathname = router.pathname
        if (router.pathname !== "/login") {
            router.push({
                pathname: '/login',
                query: {pathname: pathname},
            }, '/login');
        }
    }

    return user;
}
