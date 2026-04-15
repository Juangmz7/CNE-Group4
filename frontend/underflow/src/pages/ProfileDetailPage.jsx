import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Profile from '../components/Profile';

const ProfileDetail = () => {
  const { profileId } = useParams();
  const navigate = useNavigate();

  const [profile, setProfile] = useState(null);
  const [isProfileLoading, setIsProfileLoading] = useState(true);

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

  if (isProfileLoading) return <div style={{ padding: '20px' }}>Loading...</div>;
  if (!profile) return <div style={{ padding: '20px' }}>Profile not found.</div>;

  return (
    <div style={{ padding: '20px', maxWidth: '700px', margin: '0 auto' }}>

      {/* Profile Section */}
      <Profile 
        profile={profile}
      />

    </div>
  );
};

export default ProfileDetail;