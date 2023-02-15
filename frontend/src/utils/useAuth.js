import {useEffect, useState} from 'react';
import {useUser} from './useUser';
import {useLocalStorage} from './useLocalStorage';
import {server} from './server';
import {useHistory, useLocation} from 'react-router-dom'

// const useFindPath = () => {
//     const location = useLocation();
//     const [currentPath, setCurrentPath] = useState();
//     useEffect(() => {
//         setCurrentPath(location.pathname);
//     }, [location]);
//     return currentPath;
// };

let protectedPaths = ["/bookings", "/profile", "/create-event", "/my-events"]

export const useAuth = () => {
    const {user, addUser, removeUser} = useUser();
    const {getItem} = useLocalStorage();
    const history = useHistory()
    // const path = useFindPath();
    // const prevLocation = useLocation();
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

    // console.log(pathname)
    // protectedPaths.forEach(x => {
    //     if (pathname && pathname.includes(x)) {
    //         checkLogin();
    //     }
    // })

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

    const login = (user) => {
        addUser(user);
    };

    const logout = () => {
        removeUser();
    };

    return {user, login, logout};
};