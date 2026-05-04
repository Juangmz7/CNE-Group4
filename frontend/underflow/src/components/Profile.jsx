import React from 'react';
import { useAuth } from '../context/AuthContext';
import md5 from 'md5';
import './Profile.css';

const Profile = ({ profile }) => {
  const { userId, username, email, creation, userKarma } = profile;
  console.log(profile)
  const { user } = useAuth();

  const isAuthor = user?.id === userId;
  const formattedTime = new Date(creation).toLocaleTimeString();
  const formattedDate = new Date(creation).toLocaleDateString();
  const gravatarUrl = `https://www.gravatar.com/avatar/${md5(email.trim().toLowerCase())}`;

  return (
    <div className="profile-card">
      <div className="profile-header">
        <img
          src={gravatarUrl}
          alt="avatar"
        />
        
        <div>
          <h2>
            {username} {isAuthor && "(You)"}
          </h2>
          <p className="email" >{email}</p>
        </div>
      </div>
  
      <div className="profile-body">
        <p><strong>Karma:</strong> {userKarma}</p>
        <p>
          <strong>Joined:</strong>{" "}
          {formattedDate}{" "}
          {formattedTime}
        </p>
      </div>
    </div>
  );
}

export default Profile;