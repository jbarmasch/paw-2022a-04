import {render, screen, waitForElement, fireEvent, act, wait, waitFor, within} from '@testing-library/react';
import { BrowserRouter, useLocation, useHistory} from 'react-router-dom';
import 'whatwg-fetch';
import '@testing-library/jest-dom/extend-expect';
import {server} from "../mocks/server";
import Home from "../components/pages/home";

beforeAll(() => {
    server.listen()
})

afterEach(() => {
    server.resetHandlers()
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

global.window = { location: { pathname: null } }

jest.mock('../components/page-intro', () => ({
    __esModule: true,
    default: jest.fn(() => (
        <div>Mocked PageIntro</div>
    )),
}));

jest.mock('../components/events-content/upcoming-events', () => ({
    __esModule: true,
    default: jest.fn(() => (
        <div>Mocked UpcomingEvents</div>
    )),
}));

jest.mock('../components/events-content/few-tickets-events', () => ({
    __esModule: true,
    default: jest.fn(() => (
        <div>Mocked FewTicketsEvents</div>
    )),
}));

describe('Home', () => {
    beforeEach(() => {
        jest.clearAllMocks()
    });

    test('renders the page intro component', () => {
        render(
            <BrowserRouter>
                <Home />
            </BrowserRouter>
        );

        expect(screen.getByText('Mocked PageIntro')).toBeInTheDocument();
    });

    test('renders the upcoming events component', () => {
        render(
            <BrowserRouter>
                <Home />
            </BrowserRouter>
        );

        expect(screen.getByText('Mocked UpcomingEvents')).toBeInTheDocument();
    });

    test('renders the few tickets events component', () => {
        render(
            <BrowserRouter>
                <Home />
            </BrowserRouter>
        );

        expect(screen.getByText('Mocked FewTicketsEvents')).toBeInTheDocument();
    });

    test('renders all components correctly', () => {
        render(
            <BrowserRouter>
                <Home />
            </BrowserRouter>
        );

        expect(screen.getByText('Mocked PageIntro')).toBeInTheDocument();
        expect(screen.getByText('Mocked UpcomingEvents')).toBeInTheDocument();
        expect(screen.getByText('Mocked FewTicketsEvents')).toBeInTheDocument();
    });

    test('renders the correct number of components', () => {
        render(
            <BrowserRouter>
                <Home />
            </BrowserRouter>
        );

        expect(screen.getAllByText(/Mocked/)).toHaveLength(3);
    });

    test('renders the page intro and the other components', () => {
        render(
            <BrowserRouter>
                <Home />
            </BrowserRouter>
        );

        const pageIntro = screen.getByText('Mocked PageIntro');
        const upcomingEvents = screen.getByText('Mocked UpcomingEvents');
        const fewTicketsEvents = screen.getByText('Mocked FewTicketsEvents');

        expect(pageIntro).toBeInTheDocument();
        expect(upcomingEvents).toBeInTheDocument();
        expect(fewTicketsEvents).toBeInTheDocument();
    });
});
