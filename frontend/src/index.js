import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.scss';
import App from './App';
import {HashRouter, BrowserRouter} from 'react-router-dom'
import 'mdb-ui-kit/css/mdb.min.css';

import WebFont from 'webfontloader';

WebFont.load({
   google: {
     families: ['Roboto&display=swap'],
   }
});

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <HashRouter>
        <App/>
    </HashRouter>
);
