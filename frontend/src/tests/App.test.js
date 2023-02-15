import {render, screen, waitForElement, act} from '@testing-library/react';
import App from '../App';
import {HashRouter, BrowserRouter} from 'react-router-dom'

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
