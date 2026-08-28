import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev
export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000, // Portu kafa karışıklığı olmasın diye 3000'e sabitliyoruz
    // 🚀 SİHİRLİ SATIRLAR: Kod değişikliklerini anında tarayıcıya fırlatan canlı izleme motoru!
    watch: {
      usePolling: true
    },
    proxy: {
      // Önyüzde '/api' ile başlayan tüm istekleri yakala
      '/api': {
        target: 'http://localhost:8080', // Java Spring Boot backend adresi
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path,
        // İstek atılırken '/api' kısmını koruyarak yönlendiriyoruz
        configure: (proxy, _options) => {
          proxy.on('error', (err, _req, _res) => {
            console.log('Proxy Hatası:', err);
          });
        },
      },
    },
  },
});
