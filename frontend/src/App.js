import React from 'react'
import {Route, Switch} from 'react-router-dom'
import Home from './components/pages/home'
import Events from './components/pages/events'
import CreateEvent from './components/pages/create-event'
import Bookings from './components/pages/bookings'
import Event from './components/pages/event'
import Login from './components/pages/login'
import Organizers from './components/pages/organizers'
import Recommended from './components/pages/recommended'
import MyEvents from './components/pages/my-events'
import MyEvent from './components/pages/my-event'
import Profile from './components/pages/profile'
import ThankYou from './components/pages/thank-you'
import PageNotFound from './components/pages/404'
import ServerError from './components/pages/500'
import { useAuth } from './utils/useAuth';
import { AuthContext } from './utils/context';
import {useEffect, useState} from "react";

export default function App() {
    const [user, setUser] = useState()

    return (
        <AuthContext.Provider value={{ user, setUser }}>
            <Switch>
                <Route exact path="/" component={Home}/>
                <Route exact path="/events" component={Events}/>
                <Route path="/create-event" component={CreateEvent}/>
                <Route path="/bookings" component={Bookings}/>
                <Route path="/events/:id" component={Event}/>
                <Route path="/login" component={Login}/>
                <Route exact path="/organizers" component={Organizers}/>
                <Route path="/recommended" component={Recommended}/>
                <Route exact path="/my-events" component={MyEvents}/>
                <Route path="/my-events/:id" component={MyEvent}/>
                <Route path="/profile" component={Profile}/>
                <Route path="/thank-you" component={ThankYou}/>
                <Route path="/500" component={ServerError}/>
                <Route path="/404" component={PageNotFound}/>
                <Route path="*" component={PageNotFound}/>
            </Switch>
        </AuthContext.Provider>
    )
}