module.exports = {
  port: process.env.PORT || 3000,
  files: ['.src/**/*.{html,htm,css,js}'],
  server: {
    baseDir: "src",
    routes: {
      "/node_modules": "node_modules"
    }
  }
};