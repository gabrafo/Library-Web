import React from 'react';

const Footer: React.FC = () => {
    return (
        <footer 
            className="text-center" 
            style={{ 
                backgroundColor: '#8d4925', 
                color: 'white', 
                padding: '10px', 
                position: 'absolute', 
                bottom: '0', 
                width: '100%' 
            }}
        >
            <p>&copy; 2025 Biblioteca Virtual. Todos os direitos reservados.</p>
        </footer>
    );
};

export default Footer;
