import {render, screen, waitForElement, fireEvent, act, wait, waitFor} from '@testing-library/react';
import Organizers from '../components/pages/organizers';
import {BrowserRouter, useLocation} from 'react-router-dom'
import i18n from "../i18n";
import 'whatwg-fetch'
import '@testing-library/jest-dom/extend-expect'

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
                <Organizers/>
            </BrowserRouter>
        )
    })

    test('load data', async () => {

    });
})