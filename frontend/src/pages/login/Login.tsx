import React, { useState } from 'react';
import "../Styles.css";
import Footer from '../../components/footer/Footer';

const Login = () => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });

    const [error, setError] = useState<string>('');
    const [success, setSuccess] = useState<string>('');

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setSuccess('');
    
        try {
            const response = await fetch('http://localhost:8080/user/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });
    
            if (response.ok) {
                const token = await response.text();
    
                if (token) {
                    localStorage.setItem('authToken', token);
    
                    setSuccess('Login realizado com sucesso!');
                    setError('');
    
                    // Redirecione para a página inicial ou dashboard
                    window.location.href = '/dashboard'; // Ajuste a rota conforme necessário
                } else {
                    setError('Resposta inesperada do servidor.');
                }
            } else {
                const errorData = await response.json();
                setError(errorData.message || 'Credenciais inválidas. Tente novamente.');
            }
        } catch (err) {
            setError('Erro ao conectar com o servidor. Verifique sua conexão e tente novamente.');
        }
    };    

    return (
        <div className="container d-flex justify-content-center align-items-center min-vh-100">
            <div className="card shadow-lg p-4 w-50">
                <h1 className="text-center mb-4">Login</h1>
                <form onSubmit={handleSubmit}>
                    {error && <div className="alert alert-danger">{error}</div>}
                    {success && (
                        <div className="alert alert-success d-flex align-items-center" role="alert">
                            <i className="bi bi-check-circle-fill me-2"></i>
                            {success}
                        </div>
                    )}

                    <div className="mb-3">
                        <label htmlFor="email" className="form-label">E-mail</label>
                        <input
                            type="email"
                            id="email"
                            className="form-control"
                            name="email"
                            value={formData.email}
                            onChange={handleInputChange}
                            required
                        />
                    </div>

                    <div className="mb-3">
                        <label htmlFor="password" className="form-label">Senha</label>
                        <input
                            type="password"
                            id="password"
                            className="form-control"
                            name="password"
                            value={formData.password}
                            onChange={handleInputChange}
                            required
                        />
                    </div>

                    <button type="submit" className= 'btn-primary w-100'>Entrar</button>
                </form>
                <div className="text-center mt-3">
                    <p>Não tem uma conta? <a href="/register">Cadastre-se aqui</a></p>
                </div>
            </div>
            <Footer />
        </div>
    );
};

export default Login;