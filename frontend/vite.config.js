import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Vite 설정 객체를 defineConfig 함수로 감싸서 export default 해주어야 합니다.
export default defineConfig({
    plugins: [react()],
    server: {
        port: 5173,
        // 개발 중 CORS 에러 방지를 위해 백엔드(8080)로 프록시 설정
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true,
                secure: false,
            },
        },
    },
});