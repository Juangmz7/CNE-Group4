import React from 'react';
import { useNavigate } from 'react-router-dom';

const HomePage = () => {
  const navigate = useNavigate();
  const mockPostId = '123e4567-e89b-12d3-a456-426614174000';

  return (
    <div style={{ padding: '20px' }}>
      <h1>Main Feed</h1>
      <button 
        onClick={() => navigate(`/post/${mockPostId}`)}
        style={{ padding: '10px', cursor: 'pointer' }}
      >
        Mock button for post
      </button>
    </div>
  );
};

export default HomePage;