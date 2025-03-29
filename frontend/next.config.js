/** @type {import('next').NextConfig} */
const nextConfig = {
  eslint: {
    dirs: ['src'],
  },
  rewrites: async () => [
    {
      source: '/api/:path*',
      destination: `${process.env.SERVER_HOST}/api/:path*`,
    }
  ],

  reactStrictMode: true,
  swcMinify: true,

  webpack(config) {
    // Grab the existing rule that handles SVG imports
    const fileLoaderRule = config.module.rules.find((rule) =>
        rule.test?.test?.('.svg')
    );

    config.module.rules.push(
        // Reapply the existing rule, but only for svg imports ending in ?url
        {
          ...fileLoaderRule,
          test: /\.svg$/i,
          resourceQuery: /url/, // *.svg?url
        },
        // Convert all other *.svg imports to React components
        {
          test: /\.svg$/i,
          issuer: {not: /\.(css|scss|sass)$/},
          resourceQuery: {not: /url/}, // exclude if *.svg?url
          loader: '@svgr/webpack',
          options: {
            dimensions: false,
            titleProp: true,
          },
        }
    );

    // Modify the file loader rule to ignore *.svg, since we have it handled now.
    fileLoaderRule.exclude = /\.svg$/i;

    return config;
  },
  env: {
    TELEGRAM_BOT_NAME: process.env.TELEGRAM_BOT_NAME,
    SERVER_HOST: process.env.SERVER_HOST
  },
};

module.exports = nextConfig;
