import {createContext} from 'react';
import {server} from './server';
import {useEffect, useState} from "react";
import {Link, useHistory, useLocation} from 'react-router-dom'

import {User} from './useUser';

export const AuthContext = createContext({
    user: null,
    setUser: () => {
    },
});