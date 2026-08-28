import React from 'react';
import { Box, Typography } from '@mui/material';

interface HeaderProps {
    activeMenu: 'dashboard' | 'portfolio' | 'superchart' | 'comparison' | 'history' | 'fed';
}

export default function Header({ activeMenu }: HeaderProps): React.JSX.Element {
    const getHeaderContent = () => {
        switch (activeMenu) {
            case 'dashboard':
                return {
                    title: 'BYZ-QUANT // Macro Suite',
                    subtitle: 'Canlı Kripto, ABD Makro Göstergeleri ve Trend Analiz Terminali'
                };
            case 'superchart':
                return {
                    title: 'BYZ-QUANT // Advanced Analytics',
                    subtitle: 'Tam Ekran Profesyonel Teknik Analiz Modülü'
                };
            case 'comparison':
                return {
                    title: 'BYZ-QUANT // Relative Performance Matrix',
                    subtitle: 'Çoklu Kripto Varlık Getiri Kapıştırma ve Endeks Korelasyon Paneli'
                };
            case 'portfolio':
                return {
                    title: 'BYZ-QUANT // Asset Management',
                    subtitle: 'Güvenli Cüzdan Varlıkları ve Portföy Dağılım Analizi'
                };
            case 'history':
                return {
                    title: 'BYZ-QUANT // Transaction Ledger',
                    subtitle: 'Geçmiş Alım/Satım İşlemleri Kayıt Günlüğü'
                };
            // switch-case bloğuna şu kaydı çak şef:
            case 'fed':
                return {
                    title: 'BYZ-QUANT // Macro Strategy Center',
                    subtitle: 'Scott Bessent (Hazine) ve Kevin Warsh (Fed) Likidite Savaş Alanı'
                };

            default:
                return {
                    title: 'BYZ-QUANT // Financial Suite',
                    subtitle: 'MUI Template Standardized Financial Terminal'
                };
        }
    };

    const { title, subtitle } = getHeaderContent();

    return (
        <Box sx={{ borderBottom: '1px solid #2f3336', pb: 1.5, width: '100%', flexShrink: 0 }}>
            <Typography variant="h5" sx={{ color: '#f0b90b', fontWeight: 'bold' }}>
                {title}
            </Typography>
            <Typography variant="caption" sx={{ color: '#848e9c' }}>
                {subtitle}
            </Typography>
        </Box>
    );
}
