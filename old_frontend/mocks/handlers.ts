import { rest } from 'msw'
// import { Book, Review } from './types'

export const handlers = [
    rest.get("http://181.46.186.8:2555/api/users/test", (req, res, ctx) => {
        return res(ctx.status(200), ctx.json({}));
    }),
    rest.get("http://181.46.186.8:2555/api/locations", (req, res, ctx) => {
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
            ]
            )
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
]