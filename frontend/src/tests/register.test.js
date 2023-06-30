import {render, screen, waitForElement, fireEvent, act, wait, waitFor} from '@testing-library/react';
import Register from '../components/pages/register';
import {BrowserRouter, useLocation} from 'react-router-dom'
import i18n from "../i18n";
import 'whatwg-fetch'
import '@testing-library/jest-dom/extend-expect'

jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useLocation: () => ({
        pathname: "localhost:2557/paw-2022a-04/register"
    })
}));

global.window = { location: { pathname: null } };

describe("register", () => {
    beforeEach(() => {
        render(<BrowserRouter>
                <Register/>
            </BrowserRouter>
        )
    })

    test('load data', async () => {
        const info = {
            username: "mairimashita",
            email: "mairimashita@gmail.com",
            password: "irumakun",
            repeatPassword: "irumakun",
        };

        const registerUsername = i18n.t("register.username")
        const registerMail = i18n.t("register.mail")
        const registerPassword = i18n.t("register.pass")
        const registerRepeatPassword = i18n.t("register.repeat")

        fireEvent.change(screen.getByLabelText(registerUsername), {
            target: { value: info.username },
        });
        fireEvent.change(screen.getByLabelText(registerMail), {
            target: { value: info.email },
        });
        fireEvent.change(screen.getByLabelText(registerPassword), {
            target: { value: info.password },
        });
        fireEvent.change(screen.getByLabelText(registerRepeatPassword), {
            target: { value: info.repeatPassword },
        });

        expect(screen.getByLabelText(registerUsername).value).toBe(info.username);
        expect(screen.getByLabelText(registerUsername).value).not.toBe(info.email);
        expect(screen.getByLabelText(registerMail).value).toBe(info.email);
        expect(screen.getByLabelText(registerPassword).value).toBe(info.password);
        expect(screen.getByLabelText(registerRepeatPassword).value).toBe(info.repeatPassword);
    });

    test('create x user', async () => {
        const info = {
            username: "mairimashita",
            email: "mairimashitagmail.com",
            password: "irumakun",
            repeatPassword: "irumakun",
        };

        const registerUsername = i18n.t("register.username")
        const registerMail = i18n.t("register.mail")
        const registerPassword = i18n.t("register.pass")
        const registerRepeatPassword = i18n.t("register.repeat")

        fireEvent.change(screen.getByLabelText(registerUsername), {
            target: { value: info.username },
        });
        fireEvent.change(screen.getByLabelText(registerMail), {
            target: { value: info.email },
        });
        fireEvent.change(screen.getByLabelText(registerPassword), {
            target: { value: info.password },
        });
        fireEvent.change(screen.getByLabelText(registerRepeatPassword), {
            target: { value: info.repeatPassword },
        });

        fireEvent.click(screen.getByText(i18n.t("login.signUp")))
        expect(await screen.findByText(i18n.t("register.mailPatternError"))).toBeInTheDocument()
    });


    test('create user', async () => {
        const info = {
            username: "mairimashita",
            email: "mairimashita@gmail.com",
            password: "irumakun",
            repeatPassword: "irumakun",
        };

        const registerUsername = i18n.t("register.username")
        const registerMail = i18n.t("register.mail")
        const registerPassword = i18n.t("register.pass")
        const registerRepeatPassword = i18n.t("register.repeat")

        fireEvent.change(screen.getByLabelText(registerUsername), {
            target: { value: info.username },
        });
        fireEvent.change(screen.getByLabelText(registerMail), {
            target: { value: info.email },
        });
        fireEvent.change(screen.getByLabelText(registerPassword), {
            target: { value: info.password },
        });
        fireEvent.change(screen.getByLabelText(registerRepeatPassword), {
            target: { value: info.repeatPassword },
        });

        await act(async () => {
            fireEvent.click(screen.getByText(i18n.t("login.signUp")))
            await waitFor(() => {
                // const location = useLocation();
                // expect(location.pathname).toBe("/localhost:2557/paw-2022a-04/")
                expect(global.window.location.pathname).not.toContain('/register');
            })
        })
    });
})