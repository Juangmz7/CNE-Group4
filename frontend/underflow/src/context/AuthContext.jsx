import React, { createContext, useState, useContext, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext(null);

axios.defaults.baseURL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8081/api';

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(() => localStorage.getItem('jwt_token'));
  
  // Initialize user state from localStorage
  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem('auth_user');
    return savedUser ? JSON.parse(savedUser) : null;
  });
  
  const isAuthenticated = !!token;

  useEffect(() => {
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      localStorage.setItem('jwt_token', token);
    } else {
      delete axios.defaults.headers.common['Authorization'];
      localStorage.removeItem('jwt_token');
      localStorage.removeItem('auth_user');
    }
  }, [token]);

  useEffect(() => {
    if (user) {
      localStorage.setItem('auth_user', JSON.stringify(user));
    }
  }, [user]);

  const login = async (credentials) => {
    try {
      const response = await axios.post('/auth/login', credentials);
      
      const { id, username, accessToken } = response.data;
      
      if (!accessToken) {
        throw new Error("Invalid authentication response: Token is missing.");
      }

      axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
      
      setToken(accessToken);
      setUser({ id, username });
      
    } catch (error) {
      console.error("[AuthContext] Login failed:", error);
      throw error; 
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);