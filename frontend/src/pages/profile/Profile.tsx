import { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";
import './Profile.css';

interface Book {
  id: number;
  title: string;
}

interface Address {
  street: string;
  city: string;
  zipCode: string;
}

interface UserProfile {
  name: string;
  email: string;
  birthDate: string;
  address: Address;
  borrowedBooks: Book[];
}

const token = localStorage.getItem("authToken");
if (token) {
  const decodedToken: any = jwtDecode(token);
  console.log("Decoded Token:", decodedToken);
}

const Profile = () => {
  const [profile, setProfile] = useState<UserProfile | null>(null);

  useEffect(() => {
    const fetchUserProfile = async () => {
      const token = localStorage.getItem("authToken");
      if (!token) {
        console.error("Token JWT não encontrado!");
        return;
      }
  
      const decodedToken = JSON.parse(atob(token.split('.')[1]));
      const userId = decodedToken.id; // Pegando o ID do usuário do JWT
  
      try {
        const response = await fetch(`http://localhost:8080/user/${userId}`, {
          method: "GET",
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });
  
        if (!response.ok) {
          throw new Error(`Erro ao buscar perfil: ${response.status}`);
        }
  
        const data = await response.json();
        setProfile(data);
      } catch (error) {
        console.error("Erro ao buscar perfil:", error);
      }
    };
  
    fetchUserProfile();
  }, []);  

  if (!profile) {
    return <div>Carregando perfil...</div>;
  }

  return (
    <div className="profile-container">
      <h2>Perfil do Usuário</h2>
      <div className="profile-info">
        <div className="profile-item">
          <strong>Nome:</strong>
          <p>{profile.name}</p>
        </div>
        <div className="profile-item">
          <strong>Email:</strong>
          <p>{profile.email}</p>
        </div>
        <div className="profile-item">
          <strong>Data de Nascimento:</strong>
          <p>{new Date(profile.birthDate).toLocaleDateString()}</p>
        </div>
        <div className="profile-item">
          <strong>Endereço:</strong>
          <p>{profile.address.street}, {profile.address.city} - {profile.address.zipCode}</p>
        </div>

        <div className="profile-item">
          <strong>Livros Emprestados:</strong>
          <ul>
            {profile.borrowedBooks.length > 0 ? (
              profile.borrowedBooks.map((book) => (
                <li key={book.id}>
                  {book.title}
                </li>
              ))
            ) : (
              <p>Você não possui livros emprestados.</p>
            )}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default Profile;