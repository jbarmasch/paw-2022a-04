import React from 'react';
import {render, screen, fireEvent, act, waitFor} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import {server} from "../mocks/server";
import { BrowserRouter } from 'react-router-dom';
import i18n from "../i18n";
import 'whatwg-fetch';
import '@testing-library/jest-dom/extend-expect';
import Login from "../components/pages/login";

beforeAll(() => {
    server.listen()
})

afterEach(() => {
    server.resetHandlers()
    jest.clearAllMocks();
})

afterAll(() => {
    server.close()
})

jest.mock("../utils/auth", () => ({
    checkLogin: jest.fn(() => {
        return true
    }),
}));

const mockHistoryPush = jest.fn();

jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useLocation: () => ({
        pathname: "localhost:2557/paw-2022a-04/event/1"
    }),
    useHistory: () => ({
        push: mockHistoryPush,
    }),
}));

describe("Login", () => {
    beforeEach(() => {
        jest.clearAllMocks()
    });

    test('displays required error messages when form is submitted with empty fields', async () => {
        render(
            <BrowserRouter>
                <Login />
            </BrowserRouter>
        );

        const submitButton = screen.getByRole('button', { name: i18n.t("login.signIn") });
        await act(async () => {
            fireEvent.click(submitButton);
        })

        await waitFor(() => expect(screen.getAllByText(i18n.t('fieldRequired'))).toHaveLength(2));
    });

    test('saves access token, refresh token, and user ID to local storage upon successful login', async () => {
        render(
            <BrowserRouter>
                <Login />
            </BrowserRouter>
        );

        fireEvent.change(screen.getByLabelText("Username"), { target: { value: "testuser" } });
        fireEvent.change(screen.getByLabelText("Password"), { target: { value: "testpassword" } });

        const submitButton = screen.getByRole('button', { name: i18n.t("login.signIn") });
        await act(async () => {
            fireEvent.click(submitButton);
        })
         
        await waitFor(() => expect(localStorage.getItem("Access-Token")).toBe("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3ZDZkYTkzYi01YWY0LTRjMDMtYTMxYy0yMWU2NzNjYzFlYzQiLCJpc3MiOiJodHRwOi8vc3NoLnNsb2NvY28uY29tLmFyOjI1NTcvcGF3LTIwMjJhLTA0IiwiYXVkIjoiaHR0cDovL3NzaC5zbG9jb2NvLmNvbS5hcjoyNTU3L3Bhdy0yMDIyYS0wNCIsInN1YiI6InNhbnRpbG9jb2NvIiwiaWF0IjoxNjg4ODM2NzYzLCJleHAiOjE2ODg5MjMxNjMsImF1dGhvcml0aWVzIjpbIlJPTEVfQ1JFQVRPUiIsIlJPTEVfVVNFUiJdLCJpc1JlZnJlc2giOmZhbHNlfQ.86eFX6MWIKbDM8amSjPdQ0Z4xp3t10a6q_IgaXjMd4E"));
        await waitFor(() => expect(localStorage.getItem("Refresh-Token")).toBe("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwODI5NWU4MS0xYTdlLTQ3MTUtYmE5YS01MjRiYjMwNGFmODMiLCJpc3MiOiJodHRwOi8vc3NoLnNsb2NvY28uY29tLmFyOjI1NTcvcGF3LTIwMjJhLTA0IiwiYXVkIjoiaHR0cDovL3NzaC5zbG9jb2NvLmNvbS5hcjoyNTU3L3Bhdy0yMDIyYS0wNCIsInN1YiI6InNhbnRpbG9jb2NvIiwiaWF0IjoxNjg4ODM2NzYzLCJleHAiOjE2ODk0NDE1NjMsImF1dGhvcml0aWVzIjpbIlJPTEVfQ1JFQVRPUiIsIlJPTEVfVVNFUiJdLCJpc1JlZnJlc2giOnRydWV9.n69p-H3qkAT_ZZrw8xSEyUSNGbP3ZfaDnMRGJZ-NVNU"));
        await waitFor(() => expect(localStorage.getItem("User-ID")).toBe("1"));
    });

    test('navigates to home page', async () => {
        render(
            <BrowserRouter>
                <Login />
            </BrowserRouter>
        );

        fireEvent.change(screen.getByLabelText("Username"), { target: { value: "testuser" } });
        fireEvent.change(screen.getByLabelText("Password"), { target: { value: "testpassword" } });

        const submitButton = screen.getByRole('button', { name: i18n.t("login.signIn") });
        await act(async () => {
            fireEvent.click(submitButton);
        })

        await waitFor(() => {
            expect(mockHistoryPush).toHaveBeenCalledWith('/');
        })
    });

    test('navigates to registration page when "Not a member yet?" link is clicked', () => {
        render(
            <BrowserRouter>
                <Login />
            </BrowserRouter>
        );

        fireEvent.click(screen.getByText(i18n.t("login.notAMember")));
        expect(screen.getByText(i18n.t("login.signUp"))).toBeInTheDocument();
    });

    test('displays custom error message when username is not found', async () => {
        render(
            <BrowserRouter>
                <Login />
            </BrowserRouter>
        );

        global.fetch = jest.fn(() =>
            Promise.resolve({
                status: 401,
                headers: {
                    get: () => null
                },
                json: () => Promise.resolve({}),
            })
        );


        fireEvent.change(screen.getByLabelText("Username"), { target: { value: "testuser" } });
        fireEvent.change(screen.getByLabelText("Password"), { target: { value: "testpassword" } });

        const submitButton = screen.getByRole('button', { name: i18n.t("login.signIn") });
        await act(async () => {
            fireEvent.click(submitButton);
        })

        await waitFor(() => expect(screen.getByText(i18n.t("login.notFound"))).toBeInTheDocument());
    });
});
