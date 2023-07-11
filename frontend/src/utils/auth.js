import {server} from './server';

export const checkLogin = async (accessToken, refreshToken) => {
    let isLogged = false

    const fetchData = async (accessToken, refreshToken) => {
        let res = fetch(`${server}/api/organizers`, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            },
        })

        let aux = await res;
        if (aux.status === 200) {
            isLogged = true
            return;
        }

        res = fetch(`${server}/api/organizers`, {
            headers: {
                'Authorization': `Bearer ${refreshToken}`
            },
        })

        aux = await res;
        if (aux.status === 200) {
            isLogged = true
            localStorage.setItem("Access-Token", aux.headers.get("Access-Token"))
        }
    }

    if (accessToken && refreshToken) {
        await fetchData(accessToken, refreshToken)
    }

    return isLogged;
}