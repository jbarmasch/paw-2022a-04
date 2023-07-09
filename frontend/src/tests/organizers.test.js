import React from 'react';
import {render, screen, waitFor} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import {server} from "../mocks/server";
import { BrowserRouter } from 'react-router-dom';
import i18n from "../i18n";
import 'whatwg-fetch';
import '@testing-library/jest-dom/extend-expect';
import Organizers from "../components/pages/organizers";

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

describe("Organizers", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test('should render organizers page', async () => {
        render(
            <BrowserRouter>
                <Organizers/>
            </BrowserRouter>
        );

        const organizer1 = await waitFor(() => screen.getByText('aaaaaaaaaaa'));
        expect(organizer1).toBeInTheDocument()
        const organizer2 = await waitFor(() => screen.getByText('jbarmasch'));
        expect(organizer2).toBeInTheDocument()
        const organizer3 = await waitFor(() => screen.getByText('santilococo'));
        expect(organizer3).toBeInTheDocument()
        const organizer4 = await waitFor(() => screen.getByText('asdasasdas'));
        expect(organizer4).toBeInTheDocument()
        const organizer5 = await waitFor(() => screen.getByText('foofoobar'));
        expect(organizer5).toBeInTheDocument()

        const ticketQuantityInput = screen.getByTestId('select-order-input');
        expect(ticketQuantityInput).toBeInTheDocument()
    });

    test('displays a message when no users are found', async () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                status: 200,
                headers: {
                    get: () => null
                },
                json: () => Promise.resolve({ data: [] }),
            })
        );

        render(
            <BrowserRouter>
                <Organizers/>
            </BrowserRouter>
        );

        await waitFor(() => screen.getByText(i18n.t("organizer.noOrganizers")));
        expect(screen.getByText(i18n.t("organizer.noOrganizers"))).toBeInTheDocument();
    });
});
