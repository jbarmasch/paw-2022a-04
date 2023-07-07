import { rest } from 'msw'

export const handlers = [
    rest.get("http://ssh.slococo.com.ar:2555/api/users", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json({})
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/api/users/1", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json({})
        )
    }),
    rest.post("http://ssh.slococo.com.ar:2555/api/users", (req, res, ctx) => {
        return res(
            ctx.status(202),
            ctx.set("Location", "http://ssh.slococo.com.ar:2555/api/users/1")
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/api/locations", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json([
                {
                    "id": 1,
                    "name": "Adrogué",
                    "self": "http://ssh.slococo.com.ar:2555/api/types/1"
                },
                {
                    "id": 2,
                    "name": "Agronomía",
                    "self": "http://ssh.slococo.com.ar:2555/api/types/2"
                },
                {
                    "id": 3,
                    "name": "Area de Promoción el Triángulo",
                    "self": "http://ssh.slococo.com.ar:2555/api/types/3"
                },
                {
                    "id": 4,
                    "name": "Belgrano",
                    "self": "http://ssh.slococo.com.ar:2555/api/types/4"
                },
                {
                    "id": 5,
                    "name": "San Isidro",
                    "self": "http://ssh.slococo.com.ar:2555/api/types/5"
                },
                {
                    "id": 6,
                    "name": "Villa Adelina",
                    "self": "http://ssh.slococo.com.ar:2555/api/types/6"
                },
                {
                    "id": 7,
                    "name": "Villa Devoto",
                    "self": "http://ssh.slococo.com.ar:2555/api/types/7"
                }
            ])
        );
    }),
    rest.get("http://ssh.slococo.com.ar:2555/api/types", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json([
                {
                    "id": 1,
                    "name": "After",
                    "self": "http://ssh.slococo.com.ar:2555/api/types/1"
                },
                {
                    "id": 2,
                    "name": "Sports",
                    "self": "http://ssh.slococo.com.ar:2555/api/types/2"
                },
                {
                    "id": 3,
                    "name": "Party",
                    "self": "http://ssh.slococo.com.ar:2555/api/types/3"
                },
                {
                    "id": 4,
                    "name": "Pre-game",
                    "self": "http://ssh.slococo.com.ar:2555/api/types/4"
                },
                {
                    "id": 5,
                    "name": "Concert",
                    "self": "http://ssh.slococo.com.ar:2555/api/types/5"
                },
                {
                    "id": 6,
                    "name": "Stand up",
                    "self": "http://ssh.slococo.com.ar:2555/api/types/6"
                },
                {
                    "id": 7,
                    "name": "Theater",
                    "self": "http://ssh.slococo.com.ar:2555/api/types/7"
                }
            ])
        );
    }),
    rest.get("http://ssh.slococo.com.ar:2555/api/tags", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json([
                {
                    "id": 1,
                    "name": "Free drinks",
                    "self": "http://ssh.slococo.com.ar:2555/api/tags/1"
                },
                {
                    "id": 2,
                    "name": "Live music",
                    "self": "http://ssh.slococo.com.ar:2555/api/tags/2"
                },
                {
                    "id": 3,
                    "name": "Has show",
                    "self": "http://ssh.slococo.com.ar:2555/api/tags/3"
                },
                {
                    "id": 4,
                    "name": "Sells drinks",
                    "self": "http://ssh.slococo.com.ar:2555/api/tags/4"
                },
                {
                    "id": 5,
                    "name": "Has parking space",
                    "self": "http://ssh.slococo.com.ar:2555/api/tags/5"
                },
                {
                    "id": 6,
                    "name": "Veggie options",
                    "self": "http://ssh.slococo.com.ar:2555/api/tags/6"
                },
                {
                    "id": 7,
                    "name": "Sells food",
                    "self": "http://ssh.slococo.com.ar:2555/api/tags/7"
                }
            ])
        );
    }),
    rest.get("http://ssh.slococo.com.ar:2555/api/filters", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json({})
        )
    }),
    rest.get("http://ssh.slococo.com.ar:2555/paw-2022a-04", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.set("Link", "<http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?page=2>; rel='next'"),
            ctx.set("Link", "<http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?page=1>; rel='first'"),
            ctx.set("Link", "<http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events?page=6>; rel='last'"),
            ctx.json([
                    {
                        "attendance" : 0,
                        "date" : "2023-06-29T22:00",
                        "id" : 166,
                        "image" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/1",
                        "location" : {
                            "id" : 1,
                            "name" : "20 de Junio",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/1"
                        },
                        "maxCapacity" : 0,
                        "minPrice" : -1,
                        "name" : "Evento de prueba!",
                        "organizer" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/198",
                        "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/166",
                        "soldOut" : false,
                        "tags" : [],
                        "tickets" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/166/tickets",
                        "type" : {
                            "id" : 1,
                            "name" : "After",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/1"
                        }
                    },
                    {
                        "attendance" : 0,
                        "date" : "2023-07-14T22:00",
                        "id" : 168,
                        "image" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/1",
                        "location" : {
                            "id" : 5,
                            "name" : "Agronomía",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/5"
                        },
                        "maxCapacity" : 0,
                        "minPrice" : -1,
                        "name" : "xxxxxxxxxxxxxx",
                        "organizer" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/198",
                        "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/168",
                        "soldOut" : false,
                        "tags" : [
                            {
                                "id" : 8,
                                "name" : "Includes live preformance",
                                "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/8"
                            }
                        ],
                        "tickets" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/168/tickets",
                        "type" : {
                            "id" : 5,
                            "name" : "Concert",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/5"
                        }
                    },
                    {
                        "attendance" : 0,
                        "date" : "2023-07-14T22:00",
                        "id" : 167,
                        "image" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/1",
                        "location" : {
                            "id" : 5,
                            "name" : "Agronomía",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/5"
                        },
                        "maxCapacity" : 0,
                        "minPrice" : -1,
                        "name" : "xxxxxxxxxxxxxxxxxxxxxxxxx",
                        "organizer" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/198",
                        "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/167",
                        "soldOut" : false,
                        "tags" : [
                            {
                                "id" : 8,
                                "name" : "Includes live preformance",
                                "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/8"
                            }
                        ],
                        "tickets" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/167/tickets",
                        "type" : {
                            "id" : 5,
                            "name" : "Concert",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/5"
                        }
                    },
                    {
                        "attendance" : 0,
                        "date" : "2023-08-14T08:14",
                        "description" : "aaaaaaaa",
                        "id" : 24,
                        "image" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/1",
                        "location" : {
                            "id" : 1,
                            "name" : "20 de Junio",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/1"
                        },
                        "maxCapacity" : 0,
                        "minPrice" : -1,
                        "name" : "aaaaaaaa",
                        "organizer" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/4",
                        "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/24",
                        "soldOut" : false,
                        "tags" : [
                            {
                                "id" : 2,
                                "name" : "Free drinks",
                                "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/2"
                            }
                        ],
                        "tickets" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/24/tickets",
                        "type" : {
                            "id" : 1,
                            "name" : "After",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/1"
                        }
                    },
                    {
                        "attendance" : 0,
                        "date" : "2023-09-08T12:07",
                        "description" : "sasd",
                        "id" : 9,
                        "image" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/26",
                        "location" : {
                            "id" : 5,
                            "name" : "Agronomía",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/5"
                        },
                        "maxCapacity" : 0,
                        "minPrice" : -1,
                        "name" : "sergio",
                        "organizer" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/6",
                        "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/9",
                        "soldOut" : false,
                        "tags" : [],
                        "tickets" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/9/tickets",
                        "type" : {
                            "id" : 6,
                            "name" : "Stand up",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/6"
                        }
                    },
                    {
                        "attendance" : 0,
                        "date" : "2023-09-10T09:24",
                        "description" : "asasas",
                        "id" : 17,
                        "image" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/34",
                        "location" : {
                            "id" : 3,
                            "name" : "Acassuso",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/3"
                        },
                        "maxCapacity" : 0,
                        "minPrice" : -1,
                        "name" : "asas121",
                        "organizer" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/4",
                        "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/17",
                        "soldOut" : false,
                        "tags" : [
                            {
                                "id" : 1,
                                "name" : "Wheelchair access",
                                "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/1"
                            }
                        ],
                        "tickets" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/17/tickets",
                        "type" : {
                            "id" : 2,
                            "name" : "Sports",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/2"
                        }
                    },
                    {
                        "attendance" : 0,
                        "date" : "2023-09-10T12:26",
                        "description" : "asdas",
                        "id" : 10,
                        "image" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/27",
                        "location" : {
                            "id" : 3,
                            "name" : "Acassuso",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/3"
                        },
                        "maxCapacity" : 0,
                        "minPrice" : -1,
                        "name" : "rsdrsxx",
                        "organizer" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/6",
                        "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/10",
                        "soldOut" : false,
                        "tags" : [],
                        "tickets" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/10/tickets",
                        "type" : {
                            "id" : 4,
                            "name" : "Pre-game",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/4"
                        }
                    },
                    {
                        "attendance" : 0,
                        "date" : "2023-09-11T07:37",
                        "description" : "dasdas",
                        "id" : 5,
                        "image" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/23",
                        "location" : {
                            "id" : 2,
                            "name" : "9 de Abril",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/2"
                        },
                        "maxCapacity" : 0,
                        "minPrice" : -1,
                        "name" : "adsshola",
                        "organizer" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/1",
                        "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/5",
                        "soldOut" : false,
                        "tags" : [],
                        "tickets" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/5/tickets",
                        "type" : {
                            "id" : 2,
                            "name" : "Sports",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/2"
                        }
                    },
                    {
                        "attendance" : 0,
                        "date" : "2023-09-11T07:37",
                        "description" : "dasdas",
                        "id" : 4,
                        "image" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/22",
                        "location" : {
                            "id" : 2,
                            "name" : "9 de Abril",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/2"
                        },
                        "maxCapacity" : 0,
                        "minPrice" : -1,
                        "name" : "hola",
                        "organizer" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/1",
                        "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/4",
                        "soldOut" : false,
                        "tags" : [],
                        "tickets" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/4/tickets",
                        "type" : {
                            "id" : 2,
                            "name" : "Sports",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/2"
                        }
                    },
                    {
                        "attendance" : 0,
                        "date" : "2023-09-12T18:50",
                        "description" : "asdas",
                        "id" : 11,
                        "image" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/23",
                        "location" : {
                            "id" : 4,
                            "name" : "Adrogué",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/4"
                        },
                        "maxCapacity" : 0,
                        "minPrice" : -1,
                        "name" : "dasda",
                        "organizer" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/6",
                        "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/11",
                        "soldOut" : false,
                        "tags" : [],
                        "tickets" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/11/tickets",
                        "type" : {
                            "id" : 3,
                            "name" : "Party",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/3"
                        }
                    },
                    {
                        "attendance" : 0,
                        "date" : "2023-09-12T18:50",
                        "description" : "asdasxxxxx",
                        "id" : 15,
                        "image" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/32",
                        "location" : {
                            "id" : 4,
                            "name" : "Adrogué",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/4"
                        },
                        "maxCapacity" : 0,
                        "minPrice" : -1,
                        "name" : "xxxxxxx",
                        "organizer" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/6",
                        "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/15",
                        "soldOut" : false,
                        "tags" : [],
                        "tickets" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/15/tickets",
                        "type" : {
                            "id" : 3,
                            "name" : "Party",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/3"
                        }
                    },
                    {
                        "attendance" : 0,
                        "date" : "2023-09-12T18:50",
                        "description" : "asdasxxxxx",
                        "id" : 16,
                        "image" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/image/33",
                        "location" : {
                            "id" : 4,
                            "name" : "Adrogué",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/locations/4"
                        },
                        "maxCapacity" : 0,
                        "minPrice" : -1,
                        "name" : "xxxxxxxs",
                        "organizer" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/organizers/6",
                        "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/16",
                        "soldOut" : false,
                        "tags" : [
                            {
                                "id" : 1,
                                "name" : "Wheelchair access",
                                "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/1"
                            },
                            {
                                "id" : 2,
                                "name" : "Free drinks",
                                "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/tags/2"
                            }
                        ],
                        "tickets" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/events/16/tickets",
                        "type" : {
                            "id" : 3,
                            "name" : "Party",
                            "self" : "http://ssh.slococo.com.ar:2555/paw-2022a-04/api/types/3"
                        }
                    }
                ]
            )
        )
    }),
]
