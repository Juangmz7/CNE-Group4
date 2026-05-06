import React, { createContext, useState, useContext, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext(null);

axios.defaults.baseURL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8081/api';

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(() => localStorage.getItem('jwt_token'));
  const [isLoading, setIsLoading] = useState(true);
  
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

  // Validate token on app initialization
  useEffect(() => {
    const validateToken = async () => {
      if (token) {
        try {
          // Token is valid, user stays logged in
          const response = await axios.get('/ping/', {
            headers: { Authorization: `Bearer ${token}` }
          });

          // Spring Azure Functions might return a "successfully failed" response
          // If so, if the reported status is not 200, we consider the token invalid
          if (response.data?.status && response.data?.status !== 200) {
            throw new Error(response.data.message);
          }
        } catch (error) {
          // Token is invalid or expired, clear everything
          setToken(null);
          setUser(null);
          delete axios.defaults.headers.common['Authorization'];
        }
      }
      setIsLoading(false);
    };

    validateToken();
  }, [token]);

  const login = async (credentials) => {
    try {
      const response = await axios.post('/auth/login', credentials, {
        headers: { Authorization: null } // Ensure no token is sent during login
      });
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
    <AuthContext.Provider value={{ isAuthenticated, isLoading, user, login, logout, token }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);