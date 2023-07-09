const esModules = [
    'query-string',
    'decode-uri-component',
    'split-on-first',
    'filter-obj',
];

module.exports = {
    "moduleNameMapper": {
        "\\.(jpg|ico|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$": "<rootDir>/mocks/fileMock.js",
        // "^.+\\.(css|scss)$": "<rootDir>/mocks/cssTransform.js",
        // "^(?!.*\\.(js|jsx|mjs|cjs|ts|tsx|css|json)$)": "<rootDir>/mocks/fileTransform.js",
    },
    transformIgnorePatterns: esModules.length ? [`/node_modules/(?!${esModules.join('|')})`] : [],
    testEnvironment: "jsdom",
    // "setupFiles": [
    //     "<rootDir>/src/setupTests.js"
    // ]
}