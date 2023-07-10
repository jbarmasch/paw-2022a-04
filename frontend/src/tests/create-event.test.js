import {render, screen, waitForElement, fireEvent, act, wait, waitFor} from '@testing-library/react';
import CreateEvent from '../components/pages/create-event';
import {BrowserRouter, useLocation} from 'react-router-dom'
import i18n from "../i18n";
import 'whatwg-fetch';
import '@testing-library/jest-dom/extend-expect';
import {server} from "../mocks/server";
import preview from "jest-preview";
import userEvent from '@testing-library/user-event';

jest.mock("@mui/x-date-pickers", () => {
    return {
        DatePicker: jest.requireActual("@mui/x-date-pickers").DesktopDatePicker,
    };
});

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

jest.mock("../utils/useAuth", () => ({
    useAuth: jest.fn(() => {
        let user = { id: 1, role: "ROLE_USER", username: "HOLA", roles: ["ROLE_USER"] };
        return { user };
    }),
}));

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

describe("Create event component", () => {
    beforeEach(() => {
        localStorage.setItem("User-ID", '1')
        jest.clearAllMocks();

        Object.defineProperty(window, 'matchMedia', {
            writable: true,
            value: (query) => ({
                media: query,
                matches: query === '(pointer: fine)',
                onchange: () => {},
                addEventListener: () => {},
                removeEventListener: () => {},
                addListener: () => {},
                removeListener: () => {},
                dispatchEvent: () => false,
            }),
        })
    });

    afterEach(() => {
        delete window.matchMedia;
    })

    test('renders the form with correct elements', async () => {
        render(<BrowserRouter>
            <CreateEvent/>
        </BrowserRouter>);

        await waitFor(() => screen.getAllByText(i18n.t("create.type")));
        expect(screen.getByLabelText(i18n.t("create.description"))).toBeInTheDocument();
        expect(screen.getByLabelText(i18n.t("create.location"))).toBeInTheDocument();
        expect(screen.getByLabelText(i18n.t("create.type"))).toBeInTheDocument();
        expect(screen.getByLabelText(i18n.t("create.tags"))).toBeInTheDocument();
        expect(screen.getByLabelText(i18n.t("create.date"))).toBeInTheDocument();
        expect(screen.getByRole('button', { name: 'Submit' })).toBeInTheDocument();
    });

    test('submits the form with valid data', async () => {
        render(<BrowserRouter>
            <CreateEvent/>
        </BrowserRouter>);

        await waitFor(() => screen.getAllByText(i18n.t("create.type")));

        fireEvent.input(screen.getByLabelText(i18n.t("create.name")), { target: { value: 'Test Event' } });
        fireEvent.input(screen.getByLabelText(i18n.t("create.description")), { target: { value: 'Test description' } });
        fireEvent.input(screen.getByLabelText(i18n.t("create.location")), { target: { value: 'Test Location' } });

        const ticketQuantityInput = screen.getByTestId('select-type-input');
        fireEvent.change(ticketQuantityInput, { target: { value: 'Party' } });

        // fireEvent.input(screen.getByTestId("dateTimePicker"), { target: { value: Date.now()} });
        // const timePicker = screen.getByTestId('dateTimePicker');
        // const dateValue = new Date('2022-07-08T12:34:56Z');
        // fireEvent.change(timePicker, { target: { value: dateValue } });
        const startDateInput = await screen.findByRole('textbox', { name: i18n.t("create.date") });
        await userEvent.clear(startDateInput);
        await userEvent.type(startDateInput, '20200106', { delay: 1 });
        // expect(screen.getByRole('textbox', { name: /start date/i })).toHaveValue('2020-01-06');

        const submitButton = screen.getByTestId('button-submit')
        await act(async () => {
            fireEvent.click(submitButton);
        })
    });

    test('renders error message for invalid form submission', async () => {
        render(<BrowserRouter>
            <CreateEvent/>
        </BrowserRouter>);

        await waitFor(() => screen.getAllByText(i18n.t("create.type")));

        fireEvent.input(screen.getByLabelText(i18n.t("create.name")), { target: { value: 'Test Event' } });
        fireEvent.input(screen.getByLabelText(i18n.t("create.description")), { target: { value: 'Test description' } });
        fireEvent.input(screen.getByLabelText(i18n.t("create.location")), { target: { value: 'Test Location' } });

        const ticketQuantityInput = screen.getByTestId('select-type-input');
        fireEvent.change(ticketQuantityInput, { target: { value: 'Hannah Montana' } });

        const submitButton = screen.getByTestId('button-submit')
        await act(async () => {
            fireEvent.click(submitButton);
        })

        expect(screen.getAllByText('This field is required')).toHaveLength(3);
    });
});
