import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.scss';
import App from './App';
import {HashRouter, BrowserRouter} from 'react-router-dom'
import {createTheme, ThemeProvider} from '@mui/material/styles';
import WebFont from 'webfontloader';

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
        <BrowserRouter basename="/paw-2022a-04/">
            <App/>
        </BrowserRouter>
    </ThemeProvider>
);
