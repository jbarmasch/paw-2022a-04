import {render, screen, waitForElement, fireEvent, act, wait, waitFor} from '@testing-library/react';
import Events from '../components/pages/events';
import {BrowserRouter, useLocation} from 'react-router-dom'
import i18n from "../i18n";
import 'whatwg-fetch'
import '@testing-library/jest-dom/extend-expect'

jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useLocation: () => ({
        pathname: "localhost:2557/paw-2022a-04/events"
    })
}));

global.window = { location: { pathname: null } };

describe("events", () => {
    beforeEach(() => {
        render(<BrowserRouter>
                <Events/>
            </BrowserRouter>
        )
    })

    test('get events', async () => {
        expect(screen.queryByText(i18n.t("event.noEvents"))).not.toBeInTheDocument()
    });

    test('load events', async () => {
        // expect(await screen.findByRole("button", { label: "Go to next page" })).toBeInTheDocument()
        // await act(async () => {
        //     expect(screen.getByText("Evento de prueba!")).toBeInTheDocument()
        // })
        // expect(screen.getByText("Evento de prueba!")).toBeInTheDocument()
        // await act(async () => {
        //     expect(await screen.findByText("Evento de prueba!")).toBeInTheDocument()
        // })

        // await act(async () => {
        //     await waitFor(() => {
        //         screen.getByText("Evento de prueba!").toBeInTheDocument()
        //     })
        // })
    });

    test('create user', async () => {
        // await act(async () => {
        //     fireEvent.click(screen.getByText(i18n.t("login.signUp")))
        //     await waitFor(() => {
        //         // const location = useLocation();
        //         // expect(location.pathname).toBe("/localhost:2557/paw-2022a-04/")
        //         expect(global.window.location.pathname).not.toContain('/register');
        //     })
        // })
    });
})
