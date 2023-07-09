import React from 'react';
import {render, screen, waitFor} from '@testing-library/react';
import Profile from '../components/pages/profile';
import '@testing-library/jest-dom/extend-expect';
import {server} from "../mocks/server";
import {BrowserRouter} from "react-router-dom";

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

global.window = { location: { pathname: null } };

jest.mock('../utils/useAuth', () => ({
    useAuth: jest.fn(() => ({
        user: {
            id: 1,
            username: 'Test User',
            mail: 'test@example.com',
            rating: 4.5,
            votes: 10,
        },
    })),
}));

jest.mock('swr', () => jest.fn()
    .mockReturnValueOnce({
    data: {
        eventsAttended: 5,
        bookingsMade: 8,
        favLocation: {name: 'Test Location'},
        favType: {name: 'Test Type'},
    },
    isLoading: false,
    error: null,
}).mockReturnValueOnce({
    data: null,
    isLoading: true,
    error: null,
}).mockReturnValueOnce({
        data: null,
        isLoading: true,
        error: null,
    }))


describe('Profile', () => {
    beforeEach(() => {
        jest.clearAllMocks();
        jest.resetModules();
    });

    test('renders the profile details', async () => {
        render(
            <BrowserRouter>
                <Profile/>
            </BrowserRouter>
        );

        const username = await waitFor(() => screen.getByText('Test User'));
        expect(username).toBeInTheDocument();

        expect(screen.getByText('5')).toBeInTheDocument();
        expect(screen.getByText('8')).toBeInTheDocument();
        expect(screen.getByText('Test Location')).toBeInTheDocument();
        expect(screen.getByText('Test Type')).toBeInTheDocument();
    });

    test('renders loading page when user is not available', () => {
        jest.mock('../utils/useAuth', () => ({
            useAuth: jest.fn(() => ({
                user: null,
            })),
        }));

        render(<BrowserRouter>
            <Profile/>
        </BrowserRouter>);

        expect(screen.getByTestId('loading')).toBeInTheDocument();
    });

    test('renders loading page while user stats are being fetched', () => {
        render(
            <BrowserRouter>
                <Profile/>
            </BrowserRouter>
        );

        expect(screen.getByTestId('loading')).toBeInTheDocument();
    });
});
