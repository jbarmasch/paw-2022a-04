import {render, screen, waitForElement, fireEvent, act, wait, waitFor, within} from '@testing-library/react';
import Event from '../components/pages/event';
import { BrowserRouter, useLocation, useHistory} from 'react-router-dom';
import i18n from "../i18n";
import 'whatwg-fetch';
import '@testing-library/jest-dom/extend-expect';
import preview from 'jest-preview';
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

describe("Event component", () => {
    beforeEach(() => {
        localStorage.setItem("User-ID", '1')
        jest.clearAllMocks();
    });

    test('should render event details', async () => {
        render(
            <BrowserRouter>
                <Event match={{params: {id: 1}, isExact: true, path: "", url: ""}}/>
            </BrowserRouter>
        );

        const eventName = await waitFor(() => screen.getByText('Evento de prueba!!'));
        expect(eventName).toBeInTheDocument();

        const eventDescription = screen.getByText('Descripción de un gran evento');
        expect(eventDescription).toBeInTheDocument();

        const eventLocation = screen.getByText('9 de Abril');
        expect(eventLocation).toBeInTheDocument();

        const eventType = screen.getByText('After');
        expect(eventType).toBeInTheDocument();

        const eventDate = screen.getByText('9/8/2023, 9:05 AM');
        expect(eventDate).toBeInTheDocument();
    });

    test('should render ticket details', async () => {
        render(
            <BrowserRouter>
                <Event match={{params: {id: 1}, isExact: true, path: "", url: ""}}/>
            </BrowserRouter>
        );

        const tickets = await waitFor(() => screen.getAllByRole('row'));
        expect(tickets.length).toBeGreaterThan(0);

        const ticketRowByTicketName = screen.getByText('dssa', { closest: 'row' });
        expect(ticketRowByTicketName).toBeInTheDocument();

        // Assert ticket price
        let ticketRow = screen.getByRole('row', { name: /dssa/ })
        expect(within(ticketRow).getByText("$23")).toBeInTheDocument()
    });

    test('should submit booking form', async () => {
        render(
            <BrowserRouter>
                <Event match={{params: {id: 1}, isExact: true, path: "", url: ""}}/>
            </BrowserRouter>
        );

        const selectQty = i18n.t("event.selectQty")
        await waitFor(() => screen.getByLabelText(selectQty));

        const ticketQuantityInput = screen.getByTestId('select-ticket-input');
        fireEvent.change(ticketQuantityInput, { target: { value: '2' } });

        const submitButton = screen.getByRole('button', { name: i18n.t("book") });
        await act(async () => {
            fireEvent.click(submitButton);
        })

        await waitFor(() => {
            expect(mockHistoryPush).toHaveBeenNthCalledWith(
                1,
                {
                    "pathname": "/thank-you",
                    "state": {
                        "booking": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/bookings/lzjkJWl1PyIjmYkJBvrQZDBId3dTT2ZFSmZIZVF3LVZyak5ZN1E9PQ==",
                        "event": {
                        "attendance": 0,
                            "date": "2023-09-08T09:05",
                            "description": "Descripción de un gran evento",
                            "id": 1,
                            "image": "http://pawserver.it.itba.edu.ar/paw-2022a-04/api/image/1",
                            "location": {
                                "id": 2,
                                "name": "9 de Abril",
                                "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/2"
                            },
                            "maxCapacity": 11,
                            "minPrice": 23,
                            "name": "Evento de prueba!!",
                            "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/1",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1",
                            "soldOut": false,
                            "tags": [
                                {
                                    "id": 9,
                                    "name": "Offers drinks",
                                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/9"
                                }
                                ],
                            "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1/tickets",
                            "type": {
                                "id": 1,
                                    "name": "After",
                                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/1"
                            }
                        }
                    }
                },
            );
        })
    });

    test('should redirect to 404 page if event date is in the past', async () => {
        render(
            <BrowserRouter>
                <Event match={{params: {id: 3}, isExact: true, path: "", url: ""}}/>
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(mockHistoryPush).toHaveBeenNthCalledWith(1, '/404');
        });
    });

    test('should display "Sold Out" message for sold out events', async () => {
        render(
            <BrowserRouter>
                <Event match={{params: {id: 2}, isExact: true, path: "", url: ""}}/>
            </BrowserRouter>
        );

        const eventName = await waitFor(() => screen.getByText('Evento de prueba!!'));
        expect(eventName).toBeInTheDocument();

        preview.debug()

        const soldOutMessage = await waitFor(() => screen.getByText(i18n.t("event.soldOut")));
        expect(soldOutMessage).toBeInTheDocument();
    });

    test('should display error snackbar when booking fails', async () => {
        render(
            <BrowserRouter>
                <Event match={{params: {id: 1}, isExact: true, path: "", url: ""}}/>
            </BrowserRouter>
        );

        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: false,
                status: 500,
                statusText: 'Internal Server Error',
            })
        );

        const selectQty = i18n.t("event.selectQty")
        await waitFor(() => screen.getByLabelText(selectQty));

        const ticketQuantityInput = screen.getByTestId('select-ticket-input');
        fireEvent.change(ticketQuantityInput, { target: { value: '2' } });

        const submitButton = screen.getByRole('button', { name: i18n.t("book") });
        await act(async () => {
            fireEvent.click(submitButton);
        })

        const errorSnackbar = await waitFor(() => screen.getByText(i18n.t("error.api")));
        expect(errorSnackbar).toBeInTheDocument();
    });
})