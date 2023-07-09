import {render, screen, act} from '@testing-library/react';
import App from '../App';
import {BrowserRouter} from 'react-router-dom'
import '@testing-library/jest-dom/extend-expect';
import {server} from "../mocks/server";
import * as auth from '../utils/auth'

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


test('Renders BotPass header', async () => {
    render(<BrowserRouter>
        <App/>
    </BrowserRouter>);

    await act(async () => {
        expect(await screen.getByRole('heading', {
            name: /BotPass/i,
        })).toBeInTheDocument();
    })
});
