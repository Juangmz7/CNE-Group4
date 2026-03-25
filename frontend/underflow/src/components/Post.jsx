import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import './Post.css';

const Post = ({ post, onIncreaseRating, onReply, onUpdate, onDelete, isDetailView = false }) => {
  const { id, authorName, authorId, content, rating, creation } = post;
  const { user } = useAuth();
  
  const [isEditing, setIsEditing] = useState(false);
  const [editContent, setEditContent] = useState(content);
  
  const [showReplyForm, setShowReplyForm] = useState(false);
  const [replyContent, setReplyContent] = useState('');

  const isAuthor = user?.id === authorId;
  const formattedDate = new Date(creation).toLocaleDateString();

  const handleSaveUpdate = async () => {
    await onUpdate(id, editContent);
    setIsEditing(false);
  };

  return (
    <div className="post-card">
      <div className="post-header">
        <strong>{authorName} {isAuthor && "(You)"}</strong>
        <span>{formattedDate}</span>
      </div>
      
      <div className="post-body">
        {isEditing ? (
          <textarea 
            value={editContent} 
            onChange={(e) => setEditContent(e.target.value)}
            style={{ width: '100%', padding: '10px', borderRadius: '4px' }}
          />
        ) : (
          <p>{content}</p>
        )}
      </div>
      
      <div className="post-footer">
        <div>
          <span>Rating: {rating}</span>
          <button className="btn" onClick={() => onIncreaseRating(id)}>+1</button>
        </div>
        
        <div style={{ display: 'flex', gap: '8px' }}>
          {/* Author Actions */}
          {isAuthor && (
            <>
              {isEditing ? (
                <button className="btn" style={{backgroundColor: '#10b981', color: 'white'}} onClick={handleSaveUpdate}>Save</button>
              ) : (
                <button className="btn" onClick={() => setIsEditing(true)}>Edit</button>
              )}
              <button className="btn" style={{backgroundColor: '#ef4444', color: 'white'}} onClick={() => onDelete(id)}>Delete</button>
            </>
          )}

          <button className="btn" onClick={() => setShowReplyForm(!showReplyForm)}>
            {showReplyForm ? 'Cancel' : 'Reply'}
          </button>
        </div>
      </div>

      {showReplyForm && (
        <div className="reply-section" style={{ marginTop: '10px' }}>
          <textarea
            style={{ width: '100%', padding: '8px' }}
            placeholder="Write a reply..."
            value={replyContent}
            onChange={(e) => setReplyContent(e.target.value)}
          />
          <button className="btn" onClick={() => { onReply(id, replyContent); setReplyContent(''); setShowReplyForm(false); }}>
            Send
          </button>
        </div>
      )}
    </div>
  );
};

export default Post;