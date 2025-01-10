import { useEffect, useState } from "react";
import "./Dashboard.css";

interface Book {
  id: number;
  title: string;
  authors: string[]; // Alterado para lista de autores
  borrowed: boolean;
  quantity: number; // Quantidade de exemplares disponíveis
}

const Dashboard = () => {
  const [books, setBooks] = useState<Book[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [userId, setUserId] = useState<number | null>(null);

  const getUserIdFromToken = (token: string): number | null => {
    try {
      const payload = token.split(".")[1];
      const decoded = JSON.parse(atob(payload));
      return decoded.id;
    } catch (error) {
      return null;
    }
  };

  const fetchBooks = async () => {
    try {
      const token = localStorage.getItem("authToken");
      if (!token) {
        setError("Token JWT não encontrado!");
        return;
      }

      const userIdFromToken = getUserIdFromToken(token);
      if (!userIdFromToken) {
        setError("Erro ao obter o ID do usuário a partir do token.");
        return;
      }

      setUserId(userIdFromToken);

      const response = await fetch("http://localhost:8080/book/all", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Falha ao buscar livros");
      }

      const data = await response.json();
      console.log("Livros recebidos: ", data);

      setBooks(data);
    } catch (err) {
      setError((err as Error).message);
    }
  };

  const borrowBook = async (bookId: number) => {
    if (!userId) {
      setError("Usuário não encontrado");
      return;
    }

    try {
      const token = localStorage.getItem("authToken");
      if (!token) {
        setError("Token JWT não encontrado!");
        return;
      }

      const response = await fetch(`http://localhost:8080/loan/borrow?bookId=${bookId}&userId=${userId}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Falha ao solicitar empréstimo");
      }

      setBooks((prevBooks) =>
        prevBooks.map((book) =>
          book.id === bookId ? { ...book, borrowed: true } : book
        )
      );
    } catch (err) {
      setError((err as Error).message);
    }
  };

  const returnBook = async (bookId: number) => {
    if (!userId) {
      setError("Usuário não encontrado");
      return;
    }

    try {
      const token = localStorage.getItem("authToken");
      if (!token) {
        setError("Token JWT não encontrado!");
        return;
      }

      const response = await fetch(`http://localhost:8080/loan/return?bookId=${bookId}&userId=${userId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Falha ao devolver livro");
      }

      setBooks((prevBooks) =>
        prevBooks.map((book) =>
          book.id === bookId ? { ...book, borrowed: false } : book
        )
      );
    } catch (err) {
      setError((err as Error).message);
    }
  };

  useEffect(() => {
    fetchBooks();
  }, []);

  return (
    <div className="dashboard-container">
      <h2>Gerenciamento de Livros</h2>

      {error && <p className="error">{error}</p>}

      <table className="book-table">
        <thead>
          <tr>
            <th>Título</th>
            <th>Autores</th>
            <th>Status</th>
            <th>Exemplares disponíveis</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {books.length > 0 ? (
            books.map((book) => (
              <tr key={book.id}>
                <td>{book.title}</td>
                <td>{book.
                
                authors.join(", ")}</td>
                <td>{book.borrowed ? "Emprestado" : "Disponível"}</td>
                <td>{book.quantity}</td>
                <td>
                  {book.borrowed ? (
                    <button onClick={() => returnBook(book.id)}>Devolver</button>
                  ) : (
                    <button onClick={() => borrowBook(book.id)}>Solicitar Empréstimo</button>
                  )}
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={5}>Nenhum livro encontrado</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default Dashboard;
