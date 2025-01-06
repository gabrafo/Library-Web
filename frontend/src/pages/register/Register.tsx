import React, { useState } from 'react';

const Register = () => {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        birthDate: '',
        zipCode: '',
        role: 'AUTHENTICATED', // Valor padrão alterado
    });

    const [error, setError] = useState<string>('');
    const [success, setSuccess] = useState<string>('');

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;

        let formattedValue = value;
        if (name === 'zipCode') {
            // Remove tudo que não for número
            formattedValue = value.replace(/\D/g, '');
            // Adiciona hífen na posição adequada (xxxxx-xxxx)
            if (formattedValue.length > 5) {
                formattedValue = formattedValue.slice(0, 5) + '-' + formattedValue.slice(5, 9);
            }
        }

        setFormData({
            ...formData,
            [name]: formattedValue,
        });
    };

    const validateBirthDate = (date: string): boolean => {
        const selectedDate = new Date(date);
        const currentDate = new Date();

        const year = selectedDate.getFullYear();
        if (year > currentDate.getFullYear() || (year <= currentDate.getFullYear() - 120)) {
            setError('Data de nascimento inválida.');
            return false;
        }

        return true;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        if (!validateBirthDate(formData.birthDate)) {
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/user/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                setSuccess('Usuário registrado com sucesso!');
                setFormData({
                    name: '',
                    email: '',
                    password: '',
                    birthDate: '',
                    zipCode: '',
                    role: 'AUTHENTICATED',
                });
            } else {
                const errorResponse = await response.json();
                setError(errorResponse.message || 'Erro ao registrar usuário. Tente novamente.');
            }
        } catch (err) {
            setError('Erro ao conectar com o servidor. Verifique sua conexão e tente novamente.');
        }
    };

    return (
        <div className="container d-flex justify-content-center align-items-center min-vh-100">
            <div className="card shadow-lg p-4 w-50">
                <h1 className="text-center mb-4">Cadastro de Usuário</h1>
                <form onSubmit={handleSubmit}>
                    {error && <div className="alert alert-danger">{error}</div>}
                    {success && (
                        <div className="alert alert-success d-flex align-items-center" role="alert">
                            <i className="bi bi-check-circle-fill me-2"></i>
                            {success}
                        </div>
                    )}

                    <div className="mb-3">
                        <label htmlFor="name" className="form-label">Nome</label>
                        <input
                            type="text"
                            id="name"
                            className="form-control"
                            name="name"
                            value={formData.name}
                            onChange={handleInputChange}
                            required
                        />
                    </div>

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
                        <div id="emailHelp" className="form-text">Nunca compartilharemos seu e-mail com ninguém.</div>
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

                    <div className="mb-3">
                        <label htmlFor="birthDate" className="form-label">Data de Nascimento</label>
                        <input
                            type="date"
                            id="birthDate"
                            className="form-control"
                            name="birthDate"
                            value={formData.birthDate}
                            onChange={handleInputChange}
                            required
                        />
                    </div>

                    <div className="mb-3">
                        <label htmlFor="zipCode" className="form-label">Código Postal</label>
                        <input
                            type="text"
                            id="zipCode"
                            className="form-control"
                            name="zipCode"
                            value={formData.zipCode}
                            onChange={handleInputChange}
                            maxLength={10} // Limita o tamanho para 10 caracteres
                            required
                        />
                    </div>

                    <div className="mb-3">
                        <label htmlFor="role" className="form-label">Papel</label>
                        <select
                            id="role"
                            className="form-select"
                            name="role"
                            value={formData.role}
                            onChange={handleInputChange}
                            required
                        >
                            <option value="AUTHENTICATED">Autenticado</option>
                            <option value="ADMIN">Administrador</option>
                        </select>
                    </div>

                    <button type="submit" className="btn btn-primary w-100">Cadastrar</button>
                </form>
                <div className="text-center mt-3">
                    <p>Já tem uma conta? <a href="/login">Faça login aqui</a></p>
                </div>
            </div>
        </div>
    );
};

export default Register;
