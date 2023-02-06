import React, {useState, useEffect} from 'react'

export default function User(props) {
    const initialUserState = {
        user: {},
        loading: true,
    }

    const [user, setUser] = useState(initialUserState)

    useEffect(() => {
        setUser(props.match.params.id)
    }, [props.match.params.id])

    return user.loading ? (
        <div>Loading...</div>
    ) : (
        <div className="container">
            <h1>{props.match.params.id}</h1>
        </div>
    )
}