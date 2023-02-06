import 'whatwg-fetch'

// import 'node-fetch'

// Used for __tests__/testing-library.js
// Learn more: https://github.com/testing-library/jest-dom
import '@testing-library/jest-dom/extend-expect'

import { server } from './mocks/server'

beforeAll(() => {
  server.listen()
})

afterEach(() => {
  server.resetHandlers()
})

afterAll(() => {
  server.close()
})

jest.mock("react-i18next", () => ({
    useTranslation: () => ({ t: key => key }),
  }));
  
jest.mock('next/router', () => ({
useRouter() {
    return ({
    route: '/',
    pathname: '',
    query: '',
    asPath: '',
    push: jest.fn(),
    events: {
        on: jest.fn(),
        off: jest.fn()
    },
    beforePopState: jest.fn(() => null),
    prefetch: jest.fn(() => null)
    });
},
}));
  