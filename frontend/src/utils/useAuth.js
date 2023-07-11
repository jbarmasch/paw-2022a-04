import {useEffect} from 'react';
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
            let res = fetch(`${server}/api/organizers`, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
            })

            let aux = await res;
            if (aux.status === 200) {
                return;
            }

            res = fetch(`${server}/api/organizers`, {
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

    const updateRoles = () => {
        const refreshToken = getItem('Refresh-Token')

        fetch(`${server}/api/organizers`, {
            headers: {
                'Authorization': `Bearer ${refreshToken}`
            },
        })
            .then(y => {
                if (y.status === 200) {
                    let accessToken = y.headers.get("Access-Token")
                    localStorage.setItem('roles', jwtDecode(accessToken)["authorities"]);
                }
            })
    }

    const login = (user) => {
        const accessToken = getItem('Access-Token')
        addUser(user);
        localStorage.setItem('roles', jwtDecode(accessToken)["authorities"]);
    };

    const logout = () => {
        removeUser();
    };

    const refresh = () => {
        const refreshToken = getItem('Refresh-Token')

        fetch(`${server}/api/organizers`, {
            headers: {
                'Authorization': `Bearer ${refreshToken}`
            },
        })
            .then(y => {
                if (y.status === 200) {
                    let accessToken = y.headers.get("Access-Token")
                    localStorage.setItem("Access-Token", accessToken)

                    const user = getItem('user')
                    let aux = JSON.parse(user)
                    localStorage.setItem('roles', jwtDecode(accessToken)["authorities"]);
                    addUser(aux);
                }
            })
    }

    return {user, login, logout, refresh, getRoles: updateRoles};
};