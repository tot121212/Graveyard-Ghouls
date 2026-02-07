import { defineConfig } from 'vite'
import { svelte } from '@sveltejs/vite-plugin-svelte'

// https://vite.dev/config/
export default defineConfig({
    plugins: [svelte()],
    build: {
        outDir: '../backend/src/main/resources/static', // change this to your Spring Boot static folder
        emptyOutDir: true, // cleans folder before each build
    },
    // vite.config.js
    server: {
        proxy: {
            '/api': 'http://localhost:8080'
        }
    }
})
