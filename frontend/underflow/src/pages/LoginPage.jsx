import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(''); 
    setIsLoading(true);
    
    try {
      await login({ username, password });
      navigate('/'); // Redirect to Home upon successful authentication
    } catch (err) {
      // Extract specific error message from backend response if available, else fallback
      const errorMessage = err.response?.data?.message || 'Invalid username or password. Please try again.';
      setError(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  // Clear error message when user starts typing again
  const handleInputChange = (setter) => (e) => {
    setError('');
    setter(e.target.value);
  };

  const styles = {
    container: { maxWidth: '400px', margin: '100px auto', padding: '20px', border: '1px solid #ccc', borderRadius: '8px' },
    formGroup: { display: 'flex', flexDirection: 'column', gap: '15px', marginBottom: '20px' },
    input: { padding: '10px', fontSize: '16px' },
    button: { 
      padding: '10px', 
      backgroundColor: isLoading ? '#9ca3af' : '#2563eb', 
      color: 'white', 
      border: 'none', 
      cursor: isLoading ? 'not-allowed' : 'pointer', 
      fontSize: '16px' 
    },
    errorText: { color: '#dc2626', fontSize: '14px', marginBottom: '10px', textAlign: 'center' },
    footer: { textAlign: 'center', marginTop: '15px', fontSize: '14px' },
    link: { color: '#2563eb', cursor: 'pointer', textDecoration: 'underline' }
  };

  return (
    <div style={styles.container}>
      <h2 style={{ textAlign: 'center' }}>Sign In</h2>
      
      {error && <div style={styles.errorText}>{error}</div>}
      
      <form onSubmit={handleLogin} style={styles.formGroup}>
        <input 
          type="text" 
          placeholder="Username" 
          value={username}
          onChange={handleInputChange(setUsername)} 
          style={styles.input}
          required 
          disabled={isLoading}
        />
        <input 
          type="password" 
          placeholder="Password" 
          value={password}
          onChange={handleInputChange(setPassword)} 
          style={styles.input}
          required 
          disabled={isLoading}
        />
        <button type="submit" style={styles.button} disabled={isLoading}>
          {isLoading ? 'Logging in...' : 'Login'}
        </button>
      </form>
      
      <div style={styles.footer}>
        Don't have an account? <span style={styles.link} onClick={() => navigate('/register')}>Register here</span>
      </div>
    </div>
  );
};

export default LoginPage;