import {rest} from 'msw'

export const handlers = [
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json([{
                "events": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=1",
                "id": 1,
                "rating": 0.0,
                "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/1",
                "username": "slococo",
                "votes": 0
            }])
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json(
                [{
                    "events": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=78",
                    "id": 78,
                    "rating": 0.0,
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/78",
                    "username": "aaaaaaaaaaa",
                    "votes": 0
                }, {
                    "events": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=56",
                    "id": 56,
                    "rating": 0.0,
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/56",
                    "username": "asdasasdas",
                    "votes": 0
                }, {
                    "events": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=184",
                    "id": 184,
                    "rating": 0.0,
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/184",
                    "username": "foofoobar",
                    "votes": 0
                }, {
                    "events": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=76",
                    "id": 76,
                    "rating": 0.0,
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/76",
                    "username": "jbarmasch",
                    "votes": 0
                }, {
                    "events": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=198",
                    "id": 198,
                    "rating": 5.0,
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/198",
                    "username": "mairimashita",
                    "votes": 1
                }, {
                    "events": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=229",
                    "id": 229,
                    "rating": 0.0,
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/229",
                    "username": "marichocho",
                    "votes": 0
                }, {
                    "events": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=231",
                    "id": 231,
                    "rating": 0.0,
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/231",
                    "username": "marichochomarichocho",
                    "votes": 0
                }, {
                    "events": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=1",
                    "id": 1,
                    "rating": 0.0,
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/1",
                    "username": "marioo",
                    "votes": 0
                }, {
                    "events": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=233",
                    "id": 233,
                    "rating": 0.0,
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/233",
                    "username": "newUsernewUser",
                    "votes": 0
                }, {
                    "events": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=47",
                    "id": 47,
                    "rating": 0.0,
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/47",
                    "username": "rodrigo",
                    "votes": 0
                }, {
                    "events": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=4",
                    "id": 4,
                    "rating": 3.0,
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/4",
                    "username": "santilococo",
                    "votes": 1
                }]
            )
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/1", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.set("Access-Token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3ZDZkYTkzYi01YWY0LTRjMDMtYTMxYy0yMWU2NzNjYzFlYzQiLCJpc3MiOiJodHRwOi8vc3NoLnNsb2NvY28uY29tLmFyOjI1NTcvcGF3LTIwMjJhLTA0IiwiYXVkIjoiaHR0cDovL3NzaC5zbG9jb2NvLmNvbS5hcjoyNTU3L3Bhdy0yMDIyYS0wNCIsInN1YiI6InNhbnRpbG9jb2NvIiwiaWF0IjoxNjg4ODM2NzYzLCJleHAiOjE2ODg5MjMxNjMsImF1dGhvcml0aWVzIjpbIlJPTEVfQ1JFQVRPUiIsIlJPTEVfVVNFUiJdLCJpc1JlZnJlc2giOmZhbHNlfQ.86eFX6MWIKbDM8amSjPdQ0Z4xp3t10a6q_IgaXjMd4E"),
            ctx.set("Refresh-Token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwODI5NWU4MS0xYTdlLTQ3MTUtYmE5YS01MjRiYjMwNGFmODMiLCJpc3MiOiJodHRwOi8vc3NoLnNsb2NvY28uY29tLmFyOjI1NTcvcGF3LTIwMjJhLTA0IiwiYXVkIjoiaHR0cDovL3NzaC5zbG9jb2NvLmNvbS5hcjoyNTU3L3Bhdy0yMDIyYS0wNCIsInN1YiI6InNhbnRpbG9jb2NvIiwiaWF0IjoxNjg4ODM2NzYzLCJleHAiOjE2ODk0NDE1NjMsImF1dGhvcml0aWVzIjpbIlJPTEVfQ1JFQVRPUiIsIlJPTEVfVVNFUiJdLCJpc1JlZnJlc2giOnRydWV9.n69p-H3qkAT_ZZrw8xSEyUSNGbP3ZfaDnMRGJZ-NVNU"),
            ctx.set("User-ID", "1"),
            ctx.json({
                "events": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=1",
                "id": 1,
                "mail": "slococo@itba.edu.ar",
                "rating": 0.0,
                "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/1",
                "username": "slococo",
                "votes": 0
            })
        )
    }),
    rest.post("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users", (req, res, ctx) => {
        return res(
            ctx.status(201),
            ctx.set("Location", "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/1")
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json([
                {
                    "id": 1,
                    "name": "Adrogué",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/1"
                },
                {
                    "id": 2,
                    "name": "Agronomía",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/2"
                },
                {
                    "id": 3,
                    "name": "Area de Promoción el Triángulo",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/3"
                },
                {
                    "id": 4,
                    "name": "Belgrano",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/4"
                },
                {
                    "id": 5,
                    "name": "San Isidro",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/5"
                },
                {
                    "id": 6,
                    "name": "Villa Adelina",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/6"
                },
                {
                    "id": 7,
                    "name": "Villa Devoto",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/7"
                }
            ])
        );
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json([
                {
                    "id": 1,
                    "name": "After",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/1"
                },
                {
                    "id": 2,
                    "name": "Sports",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/2"
                },
                {
                    "id": 3,
                    "name": "Party",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/3"
                },
                {
                    "id": 4,
                    "name": "Pre-game",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/4"
                },
                {
                    "id": 5,
                    "name": "Concert",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/5"
                },
                {
                    "id": 6,
                    "name": "Stand up",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/6"
                },
                {
                    "id": 7,
                    "name": "Theater",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/7"
                }
            ])
        );
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json([
                {
                    "id": 1,
                    "name": "Free drinks",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/1"
                },
                {
                    "id": 2,
                    "name": "Live music",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/2"
                },
                {
                    "id": 3,
                    "name": "Has show",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/3"
                },
                {
                    "id": 4,
                    "name": "Sells drinks",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/4"
                },
                {
                    "id": 5,
                    "name": "Has parking space",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/5"
                },
                {
                    "id": 6,
                    "name": "Veggie options",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/6"
                },
                {
                    "id": 7,
                    "name": "Sells food",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/7"
                }
            ])
        );
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/filters", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json({})
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/1", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json(
                {
                    "events": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=198",
                    "id": 198,
                    "mail": "mairimashita@mairimashita.com",
                    "rating": 0.0,
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/198",
                    "username": "mairimashita",
                    "votes": 0
                }
            )
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1/tickets", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json(
                [
                    {
                        "booked": 0,
                        "bookings": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/eventBookings",
                        "event": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1",
                        "maxPerUser": 10,
                        "price": 23.0,
                        "qty": 11,
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tickets/199",
                        "ticketId": 199,
                        "ticketName": "dssa"
                    }
                ]
            )
        )
    }),
    rest.post("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1/bookings", (req, res, ctx) => {
        return res(
            ctx.status(201),
            ctx.set("Location", "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/bookings/lzjkJWl1PyIjmYkJBvrQZDBId3dTT2ZFSmZIZVF3LVZyak5ZN1E9PQ==")
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/bookings/lzjkJWl1PyIjmYkJBvrQZDBId3dTT2ZFSmZIZVF3LVZyak5ZN1E9PQ==", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json({
                    "code": "NwwvQGbo2n-P0VcipgJtLDA2RHZGQ0pIZVFXclFnZnNmS0VYbFE9PQ==",
                    "confirmed": false,
                    "event": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1",
                    "id": 63,
                    "image": "iVBORw0KGgoAAAANSUhEUgAAAMgAAADIAQAAAACFI5MzAAAB/ElEQVR42u2Yy25rMQhFLfFblvh1S/4tJN+98EmaDu4MBq16kpw2rEgxsHm04/zvGn/k55I1hulmMTzG1Js01JOt55pz+F4CfvZOWz1ZtoUEXMYYhqGJhDzERVuTj7UReWp8d1gepYfoqSss/NzIfsa6jiCI/Xl9004d4dryN7W4dYxvtVBHFMMlretLw2N6+LLlDeQgP18KpQw6wVKFWQtBIlSxMscZFGPvIOc694jk8LkWIrNdZRhuSyC+G0h2oDzCE9E8Rz1Z+btaRN7kr6rLGggGtSLpg5KimXt4A6GzIo1UYjbzGB0ksCiuusc0MjmfzldMjMfyq0K4QtxCVMJTBYWD6umDHw0kZwQ9j+k0kQeNop7keFW6dIhbY/GKaC3R20HGOAk+I0ZvIExXkwXBjztmVweRnalk2SNwNw/QQVRQpl6UjW+jk6+ZVUjiZkweH3YHsvZSfCnJAavcyU+Ewrr66vGlJNjnKNqVSzExfSJaTDJn5A+bHvOjhxSSnEqo8C5ecny3kJPSyzK2pw++Z3AloRX5swyJ5YrnDeTcZYgtcuewPW9PS0luxAhyZl8irM8JagkLStidgLQkdiHrIBRtUFLkD7FYdBH9pUKXZXfk9t4pqgltFodZ7SxfDQSYI5YXX/6ZuTpyFZLzdnoOqDk6yN//an4Z+QeSehvY8ZR0+AAAAABJRU5ErkJggg==",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/bookings/NwwvQGbo2n-P0VcipgJtLDA2RHZGQ0pIZVFXclFnZnNmS0VYbFE9PQ==",
                    "ticketBookings":
                        [
                            {
                                "event": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1",
                                "qty": 1, "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tickets/63",
                                "ticket": {
                                    "booked": 1,
                                    "bookings": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/eventBookings",
                                    "event": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1",
                                    "maxPerUser": 1,
                                    "price": 213.0,
                                    "qty": 2131,
                                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tickets/195",
                                    "starting": "2023-07-07T08:00",
                                    "ticketId": 195,
                                    "ticketName": "MARIA"
                                },
                                "user": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/4"
                            },
                            {
                                "event": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1", "qty": 2,
                                "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tickets/63",
                                "ticket": {
                                    "booked": 2,
                                    "bookings": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/eventBookings",
                                    "event": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1",
                                    "maxPerUser": 4,
                                    "price": 23111.0,
                                    "qty": 111,
                                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tickets/196",
                                    "starting": "2023-07-06T22:00",
                                    "ticketId": 196,
                                    "ticketName": "PRADO"
                                },
                                "user": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/4"
                            }],
                    "user": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/4"
                }
            )
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/252", (req, res, ctx) => {
        return res(
            ctx.status(200),
            // ctx.body()
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.set('Content-Type', 'application/json'),
            ctx.json(
                {
                    "attendance": 0,
                    "date": "2023-09-08T09:05",
                    "id": 1,
                    // "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/252",
                    "image": "http://pawserver.it.itba.edu.ar/paw-2022a-04/api/image/1",
                    "location": {
                        "id": 2,
                        "name": "9 de Abril",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/2"
                    },
                    "maxCapacity": 11,
                    "minPrice": 23.0,
                    "name": "Evento de prueba!!",
                    "description": "Descripción de un gran evento",
                    "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/1",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1",
                    "soldOut": false,
                    "tags": [{
                        "id": 9,
                        "name": "Offers drinks",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/9"
                    }],
                    "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1/tickets",
                    "type": {
                        "id": 1,
                        "name": "After",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/1"
                    }
                }
            )
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/2", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.set('Content-Type', 'application/json'),
            ctx.json(
                {
                    "attendance": 0,
                    "date": "2023-09-08T09:05",
                    "id": 2,
                    // "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/252",
                    "image": "http://pawserver.it.itba.edu.ar/paw-2022a-04/api/image/1",
                    "location": {
                        "id": 2,
                        "name": "9 de Abril",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/2"
                    },
                    "maxCapacity": 11,
                    "minPrice": 23.0,
                    "name": "Evento de prueba!!",
                    "description": "Descripción de un gran evento",
                    "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/1",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1",
                    "soldOut": true,
                    "tags": [{
                        "id": 9,
                        "name": "Offers drinks",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/9"
                    }],
                    "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1/tickets",
                    "type": {
                        "id": 1,
                        "name": "After",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/1"
                    }
                }
            )
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/3", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.set('Content-Type', 'application/json'),
            ctx.json(
                {
                    "attendance": 0,
                    "date": "2023-01-01T09:05",
                    "id": 3,
                    // "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/252",
                    "image": "http://pawserver.it.itba.edu.ar/paw-2022a-04/api/image/1",
                    "location": {
                        "id": 2,
                        "name": "9 de Abril",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/2"
                    },
                    "maxCapacity": 11,
                    "minPrice": 23.0,
                    "name": "Evento de prueba!!",
                    "description": "Descripción de un gran evento",
                    "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/1",
                    "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1",
                    "soldOut": true,
                    "tags": [{
                        "id": 9,
                        "name": "Offers drinks",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/9"
                    }],
                    "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/1/tickets",
                    "type": {
                        "id": 1,
                        "name": "After",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/1"
                    }
                }
            )
        )
    }),
    rest.get('http://ssh.slococo.com.ar:2555/paw-2022a-04/api/users/1/ticket-bookings', (req, res, ctx) => {
        return res(
            ctx.status(204),
        )
    }),
    rest.get('http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?recommended=:id', (req, res, ctx) => {
        return res(
            ctx.status(204),
        )
    }),
    rest.get('http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?upcoming=true', (req, res, ctx) => {
        return res(
            ctx.status(204),
        )
    }),
    rest.get('http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?fewTickets=:bool', (req, res, ctx) => {
        return res(
            ctx.status(204),
        )
    }),
    rest.get('http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?similar=:id', (req, res, ctx) => {
        return res(
            ctx.status(204),
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?userId=4", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.set("Link", "<http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?page=2>; rel='next'"),
            ctx.set("Link", "<http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?page=1>; rel='first'"),
            ctx.set("Link", "<http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?page=6>; rel='last'"),
            ctx.json([
                    {
                        "attendance": 0,
                        "date": "2023-06-29T22:00",
                        "id": 166,
                        "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/1",
                        "location": {
                            "id": 1,
                            "name": "20 de Junio",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/1"
                        },
                        "maxCapacity": 0,
                        "minPrice": -1,
                        "name": "Evento de prueba!",
                        "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/198",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/166",
                        "soldOut": false,
                        "tags": [],
                        "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/166/tickets",
                        "type": {
                            "id": 1,
                            "name": "After",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/1"
                        }
                    }
                ]
            )
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.set("Link", "<http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?page=2>; rel='next'"),
            ctx.set("Link", "<http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?page=1>; rel='first'"),
            ctx.set("Link", "<http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?page=6>; rel='last'"),
            ctx.json([
                    {
                        "attendance": 0,
                        "date": "2023-06-29T22:00",
                        "id": 166,
                        "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/1",
                        "location": {
                            "id": 1,
                            "name": "20 de Junio",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/1"
                        },
                        "maxCapacity": 0,
                        "minPrice": -1,
                        "name": "Evento de prueba!",
                        "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/198",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/166",
                        "soldOut": false,
                        "tags": [],
                        "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/166/tickets",
                        "type": {
                            "id": 1,
                            "name": "After",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/1"
                        }
                    },
                    {
                        "attendance": 0,
                        "date": "2023-07-14T22:00",
                        "id": 168,
                        "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/1",
                        "location": {
                            "id": 5,
                            "name": "Agronomía",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/5"
                        },
                        "maxCapacity": 0,
                        "minPrice": -1,
                        "name": "xxxxxxxxxxxxxx",
                        "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/198",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/168",
                        "soldOut": false,
                        "tags": [
                            {
                                "id": 8,
                                "name": "Includes live preformance",
                                "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/8"
                            }
                        ],
                        "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/168/tickets",
                        "type": {
                            "id": 5,
                            "name": "Concert",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/5"
                        }
                    },
                    {
                        "attendance": 0,
                        "date": "2023-07-14T22:00",
                        "id": 167,
                        "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/1",
                        "location": {
                            "id": 5,
                            "name": "Agronomía",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/5"
                        },
                        "maxCapacity": 0,
                        "minPrice": -1,
                        "name": "xxxxxxxxxxxxxxxxxxxxxxxxx",
                        "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/198",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/167",
                        "soldOut": false,
                        "tags": [
                            {
                                "id": 8,
                                "name": "Includes live preformance",
                                "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/8"
                            }
                        ],
                        "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/167/tickets",
                        "type": {
                            "id": 5,
                            "name": "Concert",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/5"
                        }
                    },
                    {
                        "attendance": 0,
                        "date": "2023-08-14T08:14",
                        "description": "aaaaaaaa",
                        "id": 24,
                        "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/1",
                        "location": {
                            "id": 1,
                            "name": "20 de Junio",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/1"
                        },
                        "maxCapacity": 0,
                        "minPrice": -1,
                        "name": "aaaaaaaa",
                        "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/4",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/24",
                        "soldOut": false,
                        "tags": [
                            {
                                "id": 2,
                                "name": "Free drinks",
                                "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/2"
                            }
                        ],
                        "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/24/tickets",
                        "type": {
                            "id": 1,
                            "name": "After",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/1"
                        }
                    },
                    {
                        "attendance": 0,
                        "date": "2023-09-08T12:07",
                        "description": "sasd",
                        "id": 9,
                        "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/26",
                        "location": {
                            "id": 5,
                            "name": "Agronomía",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/5"
                        },
                        "maxCapacity": 0,
                        "minPrice": -1,
                        "name": "sergio",
                        "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/6",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/9",
                        "soldOut": false,
                        "tags": [],
                        "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/9/tickets",
                        "type": {
                            "id": 6,
                            "name": "Stand up",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/6"
                        }
                    },
                    {
                        "attendance": 0,
                        "date": "2023-09-10T09:24",
                        "description": "asasas",
                        "id": 17,
                        "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/34",
                        "location": {
                            "id": 3,
                            "name": "Acassuso",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/3"
                        },
                        "maxCapacity": 0,
                        "minPrice": -1,
                        "name": "asas121",
                        "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/4",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/17",
                        "soldOut": false,
                        "tags": [
                            {
                                "id": 1,
                                "name": "Wheelchair access",
                                "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/1"
                            }
                        ],
                        "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/17/tickets",
                        "type": {
                            "id": 2,
                            "name": "Sports",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/2"
                        }
                    },
                    {
                        "attendance": 0,
                        "date": "2023-09-10T12:26",
                        "description": "asdas",
                        "id": 10,
                        "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/27",
                        "location": {
                            "id": 3,
                            "name": "Acassuso",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/3"
                        },
                        "maxCapacity": 0,
                        "minPrice": -1,
                        "name": "rsdrsxx",
                        "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/6",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/10",
                        "soldOut": false,
                        "tags": [],
                        "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/10/tickets",
                        "type": {
                            "id": 4,
                            "name": "Pre-game",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/4"
                        }
                    },
                    {
                        "attendance": 0,
                        "date": "2023-09-11T07:37",
                        "description": "dasdas",
                        "id": 5,
                        "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/23",
                        "location": {
                            "id": 2,
                            "name": "9 de Abril",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/2"
                        },
                        "maxCapacity": 0,
                        "minPrice": -1,
                        "name": "adsshola",
                        "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/1",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/5",
                        "soldOut": false,
                        "tags": [],
                        "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/5/tickets",
                        "type": {
                            "id": 2,
                            "name": "Sports",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/2"
                        }
                    },
                    {
                        "attendance": 0,
                        "date": "2023-09-11T07:37",
                        "description": "dasdas",
                        "id": 4,
                        "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/22",
                        "location": {
                            "id": 2,
                            "name": "9 de Abril",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/2"
                        },
                        "maxCapacity": 0,
                        "minPrice": -1,
                        "name": "hola",
                        "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/1",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/4",
                        "soldOut": false,
                        "tags": [],
                        "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/4/tickets",
                        "type": {
                            "id": 2,
                            "name": "Sports",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/2"
                        }
                    },
                    {
                        "attendance": 0,
                        "date": "2023-09-12T18:50",
                        "description": "asdas",
                        "id": 11,
                        "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/23",
                        "location": {
                            "id": 4,
                            "name": "Adrogué",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/4"
                        },
                        "maxCapacity": 0,
                        "minPrice": -1,
                        "name": "dasda",
                        "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/6",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/11",
                        "soldOut": false,
                        "tags": [],
                        "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/11/tickets",
                        "type": {
                            "id": 3,
                            "name": "Party",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/3"
                        }
                    },
                    {
                        "attendance": 0,
                        "date": "2023-09-12T18:50",
                        "description": "asdasxxxxx",
                        "id": 15,
                        "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/32",
                        "location": {
                            "id": 4,
                            "name": "Adrogué",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/4"
                        },
                        "maxCapacity": 0,
                        "minPrice": -1,
                        "name": "xxxxxxx",
                        "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/6",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/15",
                        "soldOut": false,
                        "tags": [],
                        "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/15/tickets",
                        "type": {
                            "id": 3,
                            "name": "Party",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/3"
                        }
                    },
                    {
                        "attendance": 0,
                        "date": "2023-09-12T18:50",
                        "description": "asdasxxxxx",
                        "id": 16,
                        "image": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/33",
                        "location": {
                            "id": 4,
                            "name": "Adrogué",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/4"
                        },
                        "maxCapacity": 0,
                        "minPrice": -1,
                        "name": "xxxxxxxs",
                        "organizer": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/6",
                        "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/16",
                        "soldOut": false,
                        "tags": [
                            {
                                "id": 1,
                                "name": "Wheelchair access",
                                "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/1"
                            },
                            {
                                "id": 2,
                                "name": "Free drinks",
                                "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/2"
                            }
                        ],
                        "tickets": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/16/tickets",
                        "type": {
                            "id": 3,
                            "name": "Party",
                            "self": "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/3"
                        }
                    }
                ]
            )
        )
    }),
]
