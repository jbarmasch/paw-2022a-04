import {render, screen, waitForElement, fireEvent} from '@testing-library/react';
import Register from '../components/pages/register';
import {HashRouter, BrowserRouter} from 'react-router-dom'

test('Renders BotPass header', async () => {
    render(<Register/>);

    const info = {
        username: "mairimashita",
        email: "mairimashita@gmail.com",
        password: "irumakun",
        repeatPassword: "irumakun",
    };
    
    fireEvent.change(screen.getElementById("username-input"), {
        target: { value: info.username },
    });

    fireEvent.change(screen.getElementById("password-input"), {
        target: { value: info.password },
    });

    fireEvent.change(screen.getElementById("email-input"), {
        target: { value: info.email },
    });
    fireEvent.change(screen.getElementById("repeat-password-input"), {
        target: { value: info.repeatPassword },
    });

    expect(screen.getElementById("username-input").value).toBe(info.username);
    expect(screen.getElementById("email-input").value).toBe(info.email);
    expect(screen.getElementById("password-input").value).toBe(info.password);
    expect(screen.getElementById("repeat-password-input").value).toBe(info.repeatPassword);

    // await act(async () => {
    //     expect(await screen.getByRole('heading', {
    //         name: /BotPass/i,
    //     })).toBeInTheDocument();
    // })

    // expect(header).toBeInTheDocument();
});
