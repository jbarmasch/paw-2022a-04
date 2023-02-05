import { render, screen, act } from '@testing-library/react'
import Events from '@/pages/[locale]/events'
import { appWithTranslation } from 'next-i18next'
import { NextRouter } from 'next/router'
import React, { FC, ReactElement } from 'react';

// jest.mock("swr");

describe('Events', () => {
    it('renders a heading', async () => {
        // localStorage.setItem('Access-Token', "ricardo")
        // localStorage.setItem('Refresh-Token', "julian")
        // useSWR.mockReturnValue({ "data": [{ data: [] }], "error": null });
        // render(<Events />)

        // await act(async () => {
        //     await new Promise((resolve) => setTimeout(resolve, 3000))
        //     const input = screen.getByLabelText("name-input")

        //     fireEvent.change(input, { target: { value: '123' } })
        //     expect(hasInputValue(input, "123")).toBe(true)
        // })

    })
});
