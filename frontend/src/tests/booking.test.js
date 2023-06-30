import {render, screen, waitForElement, fireEvent, act, wait, waitFor} from '@testing-library/react';
import Booking from '../components/pages/booking';
import {BrowserRouter, useLocation} from 'react-router-dom'
import i18n from "../i18n";
import 'whatwg-fetch'
import '@testing-library/jest-dom/extend-expect'

// useAuth = jest.fn().mockReturnValue({id: 1, role: "ROLE_USER"})
// import {useAuth} from "../utils/useAuth";
// const {useAuth} = require("../utils/useAuth");

jest.mock("../utils/useAuth", () => ({
    useAuth: jest.fn(() => {
        let user = {id: 1, role: "ROLE_USER", username: "HOLA", roles: ["ROLE_USER"]};
        return {user}
    }),
}));

jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useLocation: () => ({
        pathname: "localhost:2557/paw-2022a-04/event/1"
    })
}));

global.window = { location: { pathname: null } };

describe("register", () => {
    // useAuth.mockImplementation(() => {
    //     let user = {id: 1, role: "ROLE_USER"};
    //     return {user}
    // })

    beforeEach(() => {
        render(<BrowserRouter>
                <Booking match={{params: {code: "disco p_rr___do"}, isExact: true, path: "", url: ""}}/>
            </BrowserRouter>
        )
    })

    test('load data', async () => {

    });
})