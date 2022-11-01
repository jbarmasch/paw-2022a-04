/** @type {import('next').NextConfig} */
module.exports = {
    reactStrictMode: true,
    async rewrites() {
        return [
            {
                source: '/api/:path*',
                destination: 'http://localhost:8080/:path*',
            },
        ]
    },
    i18n: {
        locales: ['en', 'es'],
        defaultLocale: 'en',
    },
};
