import {useEffect, useState} from 'react';
import {useUser} from './useUser';
import {useLocalStorage} from './useLocalStorage';
import {server} from './server';
import {useHistory, useLocation} from 'react-router-dom'
import jwtDecode from "jwt-decode";

let protectedPaths = ["/bookings", "/profile", "/create-event", "/my-events"]

export const useAuth = () => {
    const {user, addUser, removeUser} = useUser();
    const {getItem} = useLocalStorage();
    const history = useHistory()
    const { pathname } = useLocation();
    let redirect = false;

    const checkLogin = () => {
        const accessToken = getItem('Access-Token')
        const refreshToken = getItem('Refresh-Token')

        const fetchData = async (accessToken, refreshToken) => {
            let res = fetch(`${server}/api/users`, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
            })

            let aux = await res;
            if (aux.status === 200) {
                return;
            }

            res = fetch(`${server}/api/users`, {
                headers: {
                    'Authorization': `Bearer ${refreshToken}`
                },
            })

            aux = await res;
            if (aux.status === 200) {
                localStorage.setItem("Access-Token", aux.headers.get("Access-Token"))
                return;
            }

            if (pathname !== "/login") {
                redirect = true;
                history.push(`/login?redirectTo=${pathname}`);
            }
        }

        if (accessToken && refreshToken) {
            fetchData(accessToken, refreshToken, pathname)
        } else {
            if (pathname !== "/login") {
                redirect = true;
                history.push(`/login?redirectTo=${pathname}`);
            }
        }
    }

    useEffect(() => {
        if (redirect) {
            return
        }

        const user = getItem('user');
        if (user) {
            console.log(user)
            addUser(JSON.parse(user));
        }
        protectedPaths.forEach(x => {
            if (pathname && pathname.includes(x)) {
                if (!user && pathname !== "/login") {
                    history.push(`/login?redirectTo=${pathname}`);
                }
                checkLogin()
            }
        })
    }, []);

    const login = (user) => {
        const accessToken = getItem('Access-Token')

        console.log(user)
        user["roles"] = jwtDecode(accessToken)["authorities"]
        console.log(user)

        addUser(user);
    };

    const logout = () => {
        removeUser();
    };

    const refresh = () => {
        // let user = getItem('user');
        // console.log(user)
        // if (user) {
        //     user = JSON.parse(user)
        //     fetch(`${server}/api/users/${user["id"]}`, {
        //         method: "GET",
        //         headers: {
        //             'Authorization': `Bearer ${user["accessToken"]}`
        //         }
        //     }).then(x => x.json().then(y => {
        //             console.log(y)
        //             user["role"] = y["role"]
        //             console.log(user)
        //             addUser(user);
        //         }
        //     ))
        // }
        const refreshToken = getItem('Refresh-Token')

        fetch(`${server}/api/users`, {
            headers: {
                'Authorization': `Bearer ${refreshToken}`
            },
        })
            .then(y => {
                console.log(y)
                if (y.status === 200) {
                    let accessToken = y.headers.get("Access-Token")
                    localStorage.setItem("Access-Token", accessToken)

                    const user = getItem('user')
                    let aux = JSON.parse(user)
                    aux["roles"] = jwtDecode(accessToken)["authorities"]
                    console.log(aux)
                    addUser(aux);
                }
            })
    }

    return {user, login, logout, refresh};
};