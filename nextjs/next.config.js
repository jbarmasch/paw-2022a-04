/** @type {import('next').NextConfig} */
module.exports = {
    env: {
        API_URL: 'http://181.46.186.8:2555',
    },
    reactStrictMode: true,
    async rewrites() {
        return [
            {
                source: '/api/:path*',
                destination: 'http://181.46.186.8:2555/:path*',
            },
        ]
    },
    i18n: {
        locales: ['en', 'es'],
        defaultLocale: 'en',
    },
};
