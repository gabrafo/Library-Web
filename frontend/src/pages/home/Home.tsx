import React from 'react';
import './Home.css';
import Footer from '../../components/Footer';
import bookImage from '../../assets/book_home.png';


const Home: React.FC = () => {
  return (
    <div className="home-container" style={{ minHeight: '100vh', backgroundColor: '#8d4925' }}>

        <nav className="navbar navbar-expand-lg navbar-dark" style={{ backgroundColor: '#8d4925' }}>
            <div className="container-fluid">
            <a className="navbar-brand" href="/" style={{ color: 'white' }}>Biblioteca Virtual</a>
            <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarNav">
                <ul className="navbar-nav ms-auto">
                <li className="nav-item">
                    <a className="nav-link" href="/login" style={{ color: 'white' }}>Login</a>
                </li>
                <li className="nav-item">
                    <a className="nav-link" href="/register" style={{ color: 'white' }}>Cadastro</a>
                </li>
                </ul>
            </div>
            </div>
        </nav>


        <section className="hero-section" style={{ backgroundColor: '#8d4925', color: 'white', padding: '100px 0' }}>
        <div className="container text-center">
            <h1>Bem-vindo à Biblioteca Virtual!</h1>
            <p>Explore uma vasta coleção de livros, faça empréstimos e participe da nossa comunidade literária.</p>
        </div>
        </section>

        <section className="image-section text-center" style={{ padding: '10px 0' }}>
            <div className="container">
            <img 
                src={bookImage}
                alt="Biblioteca" 
                className="img-fluid rounded" 
                style={{ maxWidth: '20%', margin: '20px 0' }} 
            />
            <p className="text-white" style={{ fontSize: '10px' }}>
                Designed by <a href="https://www.freepik.com" target="_blank" style={{ color: '#ffb48a' }}>Freepik</a>
            </p>
            </div>
        </section>

        <Footer />
    </div>
  );
};

export default Home;