import { defineConfig } from 'vite'
import { svelte } from '@sveltejs/vite-plugin-svelte'
import { Api } from './src/domain/ApiPaths'
const {
    GameSession: { GET_PAGE, CREATE, GET_TOTAL_SESSIONS },
    User: { LOGIN },
} = Api;

// https://vite.dev/config/
export default defineConfig({
    plugins: [svelte()],
    server: {
        proxy: {
            "/api": {
                target: "http://localhost:9797",
                changeOrigin: true,
                // Log every request before it goes to the backend
                configure: (proxy) => {
                    proxy.on('proxyReq', (proxyReq, req, res) => {
                        console.log(`[VITE PROXY] Sending request to backend: ${req.url}`);
                    });
                    proxy.on('proxyRes', (proxyRes, req, res) => {
                        console.log(`[VITE PROXY] Received response from backend: ${req.url} → ${proxyRes.statusCode}`);
                    });
                }
            }
        }
    }
});