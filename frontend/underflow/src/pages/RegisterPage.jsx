import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const RegisterPage = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: ''
  });
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setError('');
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      await axios.post('/auth/register', formData, {
        headers: { Authorization: null } // Ensure no token is sent during registration
      });
      
      navigate('/login');
    } catch (err) {
      const errorMessage = err.response?.data?.message || 'Registration failed. Check your details.';
      setError(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  const styles = {
    container: { maxWidth: '400px', margin: '80px auto', padding: '30px', border: '1px solid #e2e8f0', borderRadius: '12px', backgroundColor: '#ffffff', boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)' },
    title: { textAlign: 'center', marginBottom: '24px', color: '#1a202c', fontSize: '24px', fontWeight: 'bold' },
    form: { display: 'flex', flexDirection: 'column', gap: '16px' },
    input: { padding: '12px', fontSize: '16px', borderRadius: '6px', border: '1px solid #cbd5e0', outline: 'none' },
    button: { 
      padding: '12px', 
      backgroundColor: isLoading ? '#9ca3af' : '#059669', 
      color: 'white', 
      border: 'none', 
      borderRadius: '6px',
      cursor: isLoading ? 'not-allowed' : 'pointer', 
      fontSize: '16px',
      fontWeight: '600',
      transition: 'background-color 0.2s'
    },
    errorBox: { padding: '10px', backgroundColor: '#fee2e2', color: '#b91c1c', borderRadius: '6px', fontSize: '14px', marginBottom: '16px', textAlign: 'center', border: '1px solid #fecaca' },
    footer: { textAlign: 'center', marginTop: '20px', fontSize: '14px', color: '#4a5568' },
    link: { color: '#2563eb', cursor: 'pointer', fontWeight: '600', textDecoration: 'none' }
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.title}>Create Account</h2>
      
      {error && <div style={styles.errorBox}>{error}</div>}
      
      <form onSubmit={handleRegister} style={styles.form}>
        <input 
          name="username"
          type="text" 
          placeholder="Username" 
          value={formData.username}
          onChange={handleInputChange} 
          style={styles.input}
          required 
          disabled={isLoading}
        />
        <input 
          name="email"
          type="email" 
          placeholder="Email address" 
          value={formData.email}
          onChange={handleInputChange} 
          style={styles.input}
          required 
          disabled={isLoading}
        />
        <input 
          name="password"
          type="password" 
          placeholder="Password" 
          value={formData.password}
          onChange={handleInputChange} 
          style={styles.input}
          required 
          disabled={isLoading}
        />
        <button type="submit" style={styles.button} disabled={isLoading}>
          {isLoading ? 'Processing...' : 'Sign Up'}
        </button>
      </form>
      
      <div style={styles.footer}>
        Already have an account? <span style={styles.link} onClick={() => navigate('/login')}>Log in</span>
      </div>
    </div>
  );
};

export default RegisterPage;