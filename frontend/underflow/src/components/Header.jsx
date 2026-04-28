import {useAuth} from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

const Header = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogin = async () => {
    navigate('/login');
  }

  const handleLogout = async () => {
    logout();
    navigate('/login');
  }

  return (
    <>
      <input
        type="button"
        value={(user) ? "Logout" : "Login"}
        onClick={user ? handleLogout : handleLogin}
        style={{
          padding: "5px 10px",
          backgroundColor: "#2563eb",
          color: "white",
          border: "none",
          cursor: "pointer",
          fontSize: "1.1em",
          position: "absolute",
          top: "10px",
          right: "10px"
        }}
      />
    </>
  );
}

export default Header;