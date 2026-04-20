import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Profile from '../components/Profile';
import Post from '../components/Post';

const ProfileDetail = () => {
  const { profileId } = useParams();
  const navigate = useNavigate();

  const [profile, setProfile] = useState(null);
  const [posts, setPosts] = useState([]);
  const [isProfileLoading, setIsProfileLoading] = useState(true);
  const [isPostsLoading, setIsPostsLoading] = useState(false);
  const [hasFetchedPosts, setHasFetchedPosts] = useState(false);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        setIsProfileLoading(true);
        const response = await axios.get(`/user/${profileId}`);
        setProfile(response.data);
      } catch (error) {
        console.error("Error fetching profile:", error);
      } finally {
        setIsProfileLoading(false);
      }
    };
    if (profileId) fetchProfile();
  }, [profileId]);

  const handleLoadPosts = async () => {
    setIsPostsLoading(true);
    try {
      const response = await axios.get(`/post/by/${profileId}`);
      setPosts(response.data);
      setHasFetchedPosts(true);
    } catch (error) {
      console.error("Error fetching posts:", error);
    } finally {
      setIsPostsLoading(false);
    }
  };

  if (isProfileLoading) return <div style={{ padding: '20px' }}>Loading...</div>;
  if (!profile) return <div style={{ padding: '20px' }}>Profile not found.</div>;

  return (
    <div style={{ padding: '20px', maxWidth: '700px', margin: '0 auto' }}>

      {/* Profile Section */}
      <Profile 
        profile={profile}
      />

      {/* Manual Search Trigger */}
      <div style={{ textAlign: 'center', margin: '30px 0' }}>
        {!hasFetchedPosts ? (
          <button className="btn" onClick={handleLoadPosts} disabled={isPostsLoading}>
            {isPostsLoading ? 'Searching...' : 'Search for posts'}
          </button>
        ) : (
          <hr style={{ border: '0', borderTop: '1px solid #ddd' }} />
        )}
      </div>

      {hasFetchedPosts && (
        <div style={{ marginLeft: '100px', display: 'flex', flexDirection: 'column', gap: '15px'}}>
          {posts.length > 0 ? (
            posts.map(post => (
              <div key={post.id} style={{ marginLeft: '25px', borderLeft: '3px solid #eee', paddingLeft: '15px' }}>
                <Post 
                  post={{ ...post, authorName: post.authorUsername, content: post.preview }}
                  onIncreaseRating={null} 
                  onReply={null}
                  onUpdate={null}
                  onDelete={null}
                  navigate={navigate}
                />
                <input type="button" value="View Details" onClick={() => navigate(`/post/${post.id}`)} style={{
                    padding: "5px 10px",
                    backgroundColor: "#2563eb",
                    color: "white",
                    border: "none",
                    cursor: "pointer",
                }} />
              </div>
            ))
          ) : (
            <p style={{ color: '#888', textAlign: 'center' }}>No posts found.</p>
          )}
        </div>
      )}
    </div>
  );
};

export default ProfileDetail;