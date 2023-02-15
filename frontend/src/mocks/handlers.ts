import { rest } from 'msw'
// import { Book, Review } from './types'

export const handlers = [
    rest.get("http://181.46.186.8:2555/api/users", (req, res, ctx) => {
        return res(ctx.status(200), ctx.json({}));
    }),
    rest.post("http://181.46.186.8:2555/api/users", (req, res, ctx) => {
        return res(ctx.status(202), ctx.json({}));
    }),
    rest.get("http://181.46.186.8:2555/api/locations", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json([
                {
                    "id": 1,
                    "name": "Adrogué",
                    "self": "http://181.46.186.8:2555/api/types/1"
                },
                {
                    "id": 2,
                    "name": "Agronomía",
                    "self": "http://181.46.186.8:2555/api/types/2"
                },
                {
                    "id": 3,
                    "name": "Area de Promoción el Triángulo",
                    "self": "http://181.46.186.8:2555/api/types/3"
                },
                {
                    "id": 4,
                    "name": "Belgrano",
                    "self": "http://181.46.186.8:2555/api/types/4"
                },
                {
                    "id": 5,
                    "name": "San Isidro",
                    "self": "http://181.46.186.8:2555/api/types/5"
                },
                {
                    "id": 6,
                    "name": "Villa Adelina",
                    "self": "http://181.46.186.8:2555/api/types/6"
                },
                {
                    "id": 7,
                    "name": "Villa Devoto",
                    "self": "http://181.46.186.8:2555/api/types/7"
                }
            ])
        );
    }),
    rest.get("http://181.46.186.8:2555/api/types", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json([
                {
                    "id": 1,
                    "name": "After",
                    "self": "http://181.46.186.8:2555/api/types/1"
                },
                {
                    "id": 2,
                    "name": "Sports",
                    "self": "http://181.46.186.8:2555/api/types/2"
                },
                {
                    "id": 3,
                    "name": "Party",
                    "self": "http://181.46.186.8:2555/api/types/3"
                },
                {
                    "id": 4,
                    "name": "Pre-game",
                    "self": "http://181.46.186.8:2555/api/types/4"
                },
                {
                    "id": 5,
                    "name": "Concert",
                    "self": "http://181.46.186.8:2555/api/types/5"
                },
                {
                    "id": 6,
                    "name": "Stand up",
                    "self": "http://181.46.186.8:2555/api/types/6"
                },
                {
                    "id": 7,
                    "name": "Theater",
                    "self": "http://181.46.186.8:2555/api/types/7"
                }
            ])
        );
    }),
    rest.get("http://181.46.186.8:2555/api/tags", (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json([
                {
                    "id": 1,
                    "name": "Free drinks",
                    "self": "http://181.46.186.8:2555/api/tags/1"
                },
                {
                    "id": 2,
                    "name": "Live music",
                    "self": "http://181.46.186.8:2555/api/tags/2"
                },
                {
                    "id": 3,
                    "name": "Has show",
                    "self": "http://181.46.186.8:2555/api/tags/3"
                },
                {
                    "id": 4,
                    "name": "Sells drinks",
                    "self": "http://181.46.186.8:2555/api/tags/4"
                },
                {
                    "id": 5,
                    "name": "Has parking space",
                    "self": "http://181.46.186.8:2555/api/tags/5"
                },
                {
                    "id": 6,
                    "name": "Veggie options",
                    "self": "http://181.46.186.8:2555/api/tags/6"
                },
                {
                    "id": 7,
                    "name": "Sells food",
                    "self": "http://181.46.186.8:2555/api/tags/7"
                }
            ])
        );
    }),
]