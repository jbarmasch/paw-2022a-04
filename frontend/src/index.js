import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.scss';
import App from './App';
import { HashRouter, BrowserRouter } from 'react-router-dom'
import { createTheme, ThemeProvider } from '@mui/material/styles';
import WebFont from 'webfontloader';
import { Context } from './utils/context';
// import getUser from './utils/context';

WebFont.load({
    google: {
        families: ['Roboto&display=swap'],
    }
});

const theme = createTheme({
    palette: {
        primary: {
            main: '#ff6a9f',
        },
        secondary: {
            main: '#fed575',
        },
    },
});

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <ThemeProvider theme={theme}>
        <BrowserRouter>
            <Context.Provider value={Context}>
            {/* <Context.Provider value={getUser()}> */}
                <App />
            </Context.Provider>
        </BrowserRouter>
    </ThemeProvider>
);
