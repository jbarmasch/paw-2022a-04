import React from 'react';
import {render, screen, waitFor} from '@testing-library/react';
import Profile from '../components/pages/profile';
import '@testing-library/jest-dom/extend-expect';
import {server} from "../mocks/server";
import {BrowserRouter} from "react-router-dom";
import preview from "jest-preview";

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
        pathname: "localhost:2557/paw-2022a-04/profile"
    }),
    useHistory: () => ({
        push: mockHistoryPush,
    }),
}));

global.window = { location: { pathname: null } };

jest.mock('../utils/useAuth', () => ({
    useAuth: jest.fn(() => ({
        user: {
            id: 1,
            username: 'Test User',
            mail: 'test@example.com',
        },
    })),
}));

describe('Profile', () => {
    beforeEach(() => {
        localStorage.setItem('roles', "ROLE_USER, ROLE_CREATOR")
        localStorage.setItem('Access-Token', "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0NTk4MDk1YS1lZTcwLTQ5ODktYjA2MS1hZGU2NDliM2NjYjEiLCJpc3MiOiJodHRwOi8vc3NoLnNsb2NvY28uY29tLmFyOjI1NTcvcGF3LTIwMjJhLTA0IiwiYXVkIjoiaHR0cDovL3NzaC5zbG9jb2NvLmNvbS5hcjoyNTU3L3Bhdy0yMDIyYS0wNCIsInN1YiI6Im1hcmljaG9jaG9jaG8iLCJpYXQiOjE2ODkxNDc3MDcsImV4cCI6MTY4OTIzNDEwNywiYXV0aG9yaXRpZXMiOlsiUk9MRV9DUkVBVE9SIiwiUk9MRV9VU0VSIl0sImlzUmVmcmVzaCI6ZmFsc2V9.zkgUmYYw8t9Y2d79uR80Uw7y98hfpLT8KKnyDR6pnXM")
        jest.clearAllMocks();
        jest.resetModules();
    });

    test('renders the profile details', async () => {
        render(
            <BrowserRouter>
                <Profile/>
            </BrowserRouter>
        );

        const username = await screen.findByText('Test User');
        expect(username).toBeInTheDocument();
    });
});
