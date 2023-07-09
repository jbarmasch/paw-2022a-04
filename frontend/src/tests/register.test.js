import {render, screen, fireEvent, act, waitFor} from '@testing-library/react';
import Register from '../components/pages/register';
import {BrowserRouter} from 'react-router-dom'
import i18n from "../i18n";
import 'whatwg-fetch';
import '@testing-library/jest-dom/extend-expect';
import {server} from "../mocks/server";

beforeAll(() => {
    server.listen()
})

afterEach(() => {
    server.resetHandlers()
})

afterAll(() => {
    server.close()
})

const mockHistoryPush = jest.fn();

jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useLocation: () => ({
        pathname: "localhost:2557/paw-2022a-04/register"
    }),
    useHistory: () => ({
        push: mockHistoryPush,
    }),
}));

global.window = { location: { pathname: null } };

describe("Register", () => {
    beforeEach(() => {
        render(<BrowserRouter>
                <Register/>
            </BrowserRouter>
        )
    })

    test('should load data in the form correctly', async () => {
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

    test('should return an error', async () => {
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


    test('should create user', async () => {
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

        const submitButton = screen.getByRole('button', { name: i18n.t("login.signUp") });
        await act(async () => {
            fireEvent.click(submitButton);
        })

        await waitFor(() => {
            expect(mockHistoryPush).toHaveBeenCalledWith('/');
        })

    });
})