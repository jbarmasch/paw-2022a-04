import {render, screen, waitForElement, fireEvent, act, wait, waitFor} from '@testing-library/react';
import Event from '../components/pages/event';
import {BrowserRouter, useLocation} from 'react-router-dom'
import i18n from "../i18n";
import 'whatwg-fetch'
import '@testing-library/jest-dom/extend-expect'

jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useLocation: () => ({
        pathname: "localhost:2557/paw-2022a-04/event/1"
    })
}));

global.window = { location: { pathname: null } };

describe("register", () => {
    beforeEach(() => {
        render(<BrowserRouter>
                <Event match={{params: {id: 1}, isExact: true, path: "", url: ""}}/>
            </BrowserRouter>
        )
    })

    test('load data', async () => {

    });
})