import { defineConfig } from 'vite'
import { svelte } from '@sveltejs/vite-plugin-svelte'

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
                        console.log(`[VITE PROXY] Sending request to backend (REST): ${req.url}`);
                    });
                    proxy.on('proxyRes', (proxyRes, req, res) => {
                        console.log(`[VITE PROXY] Received response from backend (REST): ${req.url} → ${proxyRes.statusCode}`);
                    });
                }
            },
            "/app": {
                target: "http://localhost:9797",
                changeOrigin: true,
                // Log every request before it goes to the backend
                configure: (proxy) => {
                    proxy.on('proxyReq', (proxyReq, req, res) => {
                        console.log(`[VITE PROXY] Sending request to backend (Websocket): ${req.url}`);
                    });
                    proxy.on('proxyRes', (proxyRes, req, res) => {
                        console.log(`[VITE PROXY] Received response from backend (Websocket): ${req.url} → ${proxyRes.statusCode}`);
                    });
                }
            }
        }
    }
});