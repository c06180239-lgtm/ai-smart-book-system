import { defineConfig } from 'vite'; import vue from '@vitejs/plugin-vue';
const backend=`http://localhost:${process.env.BACKEND_PORT}`;
export default defineConfig({plugins:[vue()],server:{port:Number(process.env.FRONTEND_PORT),proxy:{'/api':backend,'/uploads':backend}}});
