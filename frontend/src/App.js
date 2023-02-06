import React from 'react'
import {Route, Switch} from 'react-router-dom'
import Home from './components/pages/home'
import User from './components/pages/user'
import Events from './components/pages/events'
import CreateEvent from './components/pages/create-event'
import Bookings from './components/pages/bookings'

export default function App() {
    return (
        <Switch>
            <Route exact path="/" component={Home}/>
            <Route path="/users/:id" component={User}/>
            <Route path="/events" component={Events}/>
            <Route path="/create-event" component={CreateEvent}/>
            <Route path="/bookings" component={Bookings}/>
        </Switch>
    )
}