import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Box, Drawer, List, ListItem, ListItemButton, ListItemText, Typography } from '@mui/material';
import DashboardIcon from '@mui/icons-material/Dashboard';
import AccountBalanceWalletIcon from '@mui/icons-material/AccountBalanceWallet';
import HistoryIcon from '@mui/icons-material/History';
import ShowChartIcon from '@mui/icons-material/ShowChart';

export default function Sidebar(): React.JSX.Element {
    const drawerWidth = 280;
    const location = useLocation(); // Anlık hangi URL'de olduğumuzu söyler
    const navigate = useNavigate(); // Sayfa değiştiren asil yönlendirici

    // Sidebar.tsx dosyasının içindeki menuItems dizisi tam olarak bu şekilde kalmalı:
    const menuItems = [
        { id: '/dashboard', text: 'Terminal Dashboard', icon: <DashboardIcon /> },
        { id: '/superchart', text: 'Superchart', icon: <ShowChartIcon /> },
        { id: '/portfolio', text: 'Portföy Dağılımı', icon: <AccountBalanceWalletIcon /> },
        { id: '/history', text: 'İşlem Geçmişi', icon: <HistoryIcon /> }, // Butona basınca tam olarak /history adresine navigate edecek
        { id: '/fed', text: 'Fed Strateji', icon: <DashboardIcon /> }
    ];


    return (
        <Box component="nav" sx={{ width: { md: drawerWidth }, flexShrink: { md: 0 } }}>
            <Drawer
                variant="permanent"
                sx={{
                    display: { xs: 'none', md: 'block' },
                    '& .MuiDrawer-paper': { width: drawerWidth, boxSizing: 'border-box', backgroundColor: '#14151a', borderRight: '1px solid #2f3336', color: '#eaecef', padding: '24px', overflowX: 'hidden' },
                }}
                open
            >
                <Box sx={{ mb: 4 }}>
                    <Typography variant="h6" sx={{ color: '#f0b90b', fontWeight: 'bold', letterSpacing: '1px' }}>BYZ-QUANT</Typography>
                    <Typography variant="caption" sx={{ color: '#848e9c', textTransform: 'uppercase' }}>Terminal v1.0</Typography>
                </Box>

                <Box sx={{ backgroundColor: '#1e2329', padding: '16px', borderRadius: '8px', borderLeft: '4px solid #f0b90b', mb: 4 }}>
                    <Typography variant="caption" sx={{ color: '#848e9c', textTransform: 'uppercase', display: 'block' }}>Toplam Varlık</Typography>
                    <Typography variant="h5" sx={{ fontWeight: 'bold', my: 0.5, color: '#fff' }}>$42,650.80</Typography>
                    <Typography variant="caption" sx={{ color: '#0ecb81', fontWeight: 'bold' }}>+4.25%</Typography>
                </Box>

                <List sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                    {menuItems.map((item) => {
                        const isActive = location.pathname === item.id;
                        return (
                            <ListItem key={item.id} disablePadding>
                                <ListItemButton
                                    onClick={() => navigate(item.id)} // Butona tıklandığında url'i resmi olarak değiştirir!
                                    sx={{
                                        borderRadius: '6px',
                                        backgroundColor: isActive ? 'rgba(240, 185, 11, 0.1)' : 'transparent',
                                        color: isActive ? '#f0b90b' : '#eaecef',
                                        '&:hover': { backgroundColor: 'rgba(255,255,255,0.02)' }
                                    }}
                                >
                                    <Box sx={{ mr: 2, display: 'flex', alignItems: 'center', color: isActive ? '#f0b90b' : '#848e9c' }}>{item.icon}</Box>
                                    <ListItemText primary={<Typography sx={{ fontSize: '14px', fontWeight: isActive ? 'bold' : 'normal' }}>{item.text}</Typography>} />
                                </ListItemButton>
                            </ListItem>
                        );
                    })}
                </List>

                <Box sx={{ mt: 'auto', pt: 2 }}>
                    <Typography variant="caption" sx={{ color: '#848e9c', textTransform: 'uppercase', display: 'block', mb: 1 }}>Son İşlemler</Typography>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', fontSize: '12px', mb: 1 }}>
                        <span style={{ color: '#f6465d', fontWeight: 'bold' }}>SELL BTC</span>
                        <span style={{ color: '#fff' }}>0.05 BTC</span>
                    </Box>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', fontSize: '12px' }}>
                        <span style={{ color: '#0ecb81', fontWeight: 'bold' }}>BUY ETH</span>
                        <span style={{ color: '#fff' }}>1.20 ETH</span>
                    </Box>
                </Box>
            </Drawer>
        </Box>
    );
}
