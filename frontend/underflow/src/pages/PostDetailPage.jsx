import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Post from '../components/Post';

const PostDetail = () => {
  const { postId } = useParams();
  const navigate = useNavigate();

  const [post, setPost] = useState(null);
  const [replies, setReplies] = useState([]);
  const [isPostLoading, setIsPostLoading] = useState(true);
  const [isRepliesLoading, setIsRepliesLoading] = useState(false);
  const [hasFetchedReplies, setHasFetchedReplies] = useState(false);

  useEffect(() => {
    const fetchMainPost = async () => {
      try {
        setIsPostLoading(true);
        const response = await axios.get(`/post/${postId}`);
        setPost(response.data);
      } catch (error) {
        console.error("Error fetching main post:", error);
      } finally {
        setIsPostLoading(false);
      }
    };
    if (postId) fetchMainPost();
  }, [postId]);

  const handleLoadReplies = async () => {
    setIsRepliesLoading(true);
    try {
      const response = await axios.get(`/post/${postId}/replies`);
      setReplies(response.data);
      setHasFetchedReplies(true);
    } catch (error) {
      console.error("Error fetching replies:", error);
    } finally {
      setIsRepliesLoading(false);
    }
  };

  const handleReply = async (targetPostId, content) => {
    try {
      const response = await axios.post(`/post/${targetPostId}/reply`, { content });
      const newReply = response.data;
      // Add the new reply to the top of the conversation list
      setReplies(prev => [newReply, ...prev]);
      setHasFetchedReplies(true);
    } catch (error) {
      console.error("Reply failed:", error);
    }
  };

  const handleIncreaseRating = async (id) => {
    try {
      await axios.post(`/post/${id}/rating/increase`);
      if (post?.id === id) {
        setPost(prev => ({ ...prev, rating: prev.rating + 1 }));
      } else {
        setReplies(prev => prev.map(r => r.id === id ? { ...r, rating: r.rating + 1 } : r));
      }
    } catch (e) {
      console.error(e);
    }
  };

  const handleUpdate = async (id, newContent) => {
    try {
      const response = await axios.put(`/post/${id}`, { content: newContent });
      const updatedData = response.data;
      if (post.id === id) {
        setPost(updatedData);
      } else {
        setReplies(prev => prev.map(r => r.id === id ? updatedData : r));
      }
    } catch (error) {
      console.error("Update failed:", error);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this post permanently?")) return;
    try {
      await axios.delete(`/post/${id}`);
      if (post.id === id) {
        navigate('/'); // Redirect home if main post is gone
      } else {
        setReplies(prev => prev.filter(r => r.id !== id));
      }
    } catch (error) {
      console.error("Delete failed:", error);
    }
  };

  if (isPostLoading) return <div style={{ padding: '20px' }}>Loading...</div>;
  if (!post) return <div style={{ padding: '20px' }}>Post not found.</div>;

  return (
    <div style={{ padding: '20px', maxWidth: '700px', margin: '0 auto' }}>
      <button onClick={() => navigate('/')} style={{ marginBottom: '20px' }}>
        ← Back to Feed
      </button>

      {/* Main Post Section */}
      <Post 
        post={post} 
        onIncreaseRating={handleIncreaseRating} 
        onReply={handleReply}
        onUpdate={handleUpdate}
        onDelete={handleDelete}
        navigate={navigate}
        isDetailView={true} 
      />

      {/* Manual Search Trigger */}
      <div style={{ textAlign: 'center', margin: '30px 0' }}>
        {!hasFetchedReplies ? (
          <button className="btn" onClick={handleLoadReplies} disabled={isRepliesLoading}>
            {isRepliesLoading ? 'Searching...' : 'Search for Replies'}
          </button>
        ) : (
          <hr style={{ border: '0', borderTop: '1px solid #ddd' }} />
        )}
      </div>

      {/* Replies Conversation List */}
      {hasFetchedReplies && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
          <h3>Conversation</h3>
          {replies.length > 0 ? (
            replies.map(reply => (
              <div key={reply.id} style={{ marginLeft: '25px', borderLeft: '3px solid #eee', paddingLeft: '15px' }}>
                <Post 
                  post={reply} 
                  onIncreaseRating={handleIncreaseRating} 
                  onReply={handleReply}
                  onUpdate={handleUpdate}
                  onDelete={handleDelete}
                  navigate={navigate}
                />
              </div>
            ))
          ) : (
            <p style={{ color: '#888', textAlign: 'center' }}>No replies found.</p>
          )}
        </div>
      )}
    </div>
  );
};

export default PostDetail;