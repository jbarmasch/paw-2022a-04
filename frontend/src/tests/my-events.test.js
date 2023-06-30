import {render, screen, waitForElement, fireEvent, act, wait, waitFor} from '@testing-library/react';
import MyEvents from '../components/pages/my-events';
import {BrowserRouter, useLocation} from 'react-router-dom'
import i18n from "../i18n";
import 'whatwg-fetch'
import '@testing-library/jest-dom/extend-expect'

jest.mock("../utils/useAuth", () => ({
    useAuth: jest.fn(() => {
        let user = {id: 1, role: "ROLE_USER", username: "HOLA", roles: ["ROLE_USER"]};
        return {user}
    }),
}));

jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useLocation: () => ({
        pathname: "localhost:2557/paw-2022a-04/my-events"
    })
}));

global.window = { location: { pathname: null } };

describe("register", () => {
    beforeEach(() => {
        render(<BrowserRouter>
                <MyEvents/>
            </BrowserRouter>
        )
    })

    test('load data', async () => {

    });
})