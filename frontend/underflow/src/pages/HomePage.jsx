import React, {useState} from 'react';
import PostsList from "../components/home/PostsList";
import CreatePostInput from "../components/home/CreatePostInput";
import Header from "../components/Header";

const HomePage = () => {
  const [token] = useState(localStorage.getItem('jwt_token') || null);

  return (
    <>
      <Header />
      <div style={{ padding: '20px' }}>
        <h1>Main Feed</h1>
          <div style={{
            display: 'flex',
            flexDirection: 'column',
            gap: '10px',
            marginBottom: '20px'
          }}>
            <PostsList token={token} />
          </div>
          <div>
            <CreatePostInput token={token} />
          </div>
      </div>
    </>
  );
};

export default HomePage;