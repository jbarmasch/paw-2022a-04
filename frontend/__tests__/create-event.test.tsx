import { render, screen, act, waitForElementToBeRemoved, waitFor, fireEvent } from '@testing-library/react'
import CreateEvent from '@/pages/[locale]/create-event'
import { appWithTranslation } from 'next-i18next'
import { NextRouter } from 'next/router'
import React, { FC, ReactElement } from 'react';
// import { MySwrConfig } from '../components/MySwrConfig';
// import { rest } from "msw";
// import { setupServer } from "msw/node";

// const server = setupServer(
//     rest.post("http://localhost:8080/api/users/test", (req, res, ctx) => {
//         return res(ctx.status(200), ctx.json({}));
//     })
// );

// beforeAll(() => server.listen());
// afterEach(() => server.resetHandlers());
// afterAll(() => server.close());
// jest.mock("swr");

describe('IndexPage', () => {
    it('renders a heading', async () => {
        localStorage.setItem('Access-Token', "ricardo")
        localStorage.setItem('Refresh-Token', "julian")
        render(<CreateEvent />)

    // let res = await fetch("http://181.46.186.8:2555/api/locations")
    // let ans = await res.json()
    // console.log(ans)

        // await act(async () => {
        //     await new Promise((resolve) => setTimeout(resolve, 5000))
        //     await waitForElementToBeRemoved(screen.queryByText("Loading..."));

        //     const input = screen.getByLabelText("name-input")

        //     fireEvent.change(input, { target: { value: '123' } })
        //     expect(hasInputValue(input, "123")).toBe(true)
        // })

        // await waitFor(() =>
        //     expect(screen.getByText('LA PORONGA DE SOTUY')).toBeInTheDocument()
        // )

        expect(await screen.findByText('LA PORONGA DE SOTUYO')).toBeInTheDocument();

        const input = screen.getByLabelText("name-input")
        // const input = screen.getByRole('input', { name: 'name-input' })

        fireEvent.change(input, { target: { value: '123' } })
        // expect(hasInputValue(input, "123")).toBe(true)
        // expect(screen.getByLabelText("name-input")).toHaveValue("123");
        expect(input).toHaveValue("123");
    })
});

// import useSWR from "swr";
// jest.mock("swr");

// test("new-test", async () => { 
//     localStorage.setItem('Access-Token', "ricardo")
//     localStorage.setItem('Refresh-Token', "julian")
//     useSWR.mockReturnValue(
//         { "location": [{ data: [{
//             "id": 1,
//             "name": "a"
//         }] }], "error": null },
//         { "tags": [{ data: [{
//             "id": 1,
//             "name": "a"
//         }] }], "error": null },
//         { "types": [{ data: [{
//             "id": 1,
//             "name": "a"
//         }] }], "error": null },
//         );

//     render(<CreateEvent />)


//     // let res = await fetch("http://181.46.186.8:2555/api/locations")
//     // let ans = await res.json()

//     const input = screen.getByLabelText("name-input")

//     fireEvent.change(input, { target: { value: '123' } })
//     expect(hasInputValue(input, "123")).toBe(true)
// })
