const path = require('path')

module.exports = {
    sassOptions: {
        includePaths: [path.join(__dirname, 'styles')],
    },
    compress: true,
    images: {
        loader: 'custom',
        loaderFile: './utils/image-loader.js',
    },
    typescript: {
        ignoreBuildErrors: true,
    },
    // trailingSlash: true,
    // exportPathMap: async function (
    //     defaultPathMap,
    //     { dev, dir, outDir, distDir, buildId }
    // ) {
    //     return {
    //         '/': { page: '/' },
    //         '/events': { page: '/events' },
    //     }
    // },
    webpack: (config) => {
        config.resolve = {
          ...config.resolve,
          fallback: {
            "fs": false,
            "path": false,
            "os": false,
          }
        }
        return config
      },
}