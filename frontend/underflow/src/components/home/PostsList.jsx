import {useNavigate} from "react-router-dom";
import axios from "axios";
import useSWR from "swr";
import React from "react";

const PostsList = ({token}) => {
  const navigate = useNavigate();

  const fetchAllPosts = async () => {
    const response = await axios.get('/post', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    console.log("Fetched posts:", response.data);

    return await response.data;
  };

  const { data: posts, error } = useSWR('/post', fetchAllPosts);

  return (
    <>
      {error && <p>Failed to load posts</p>}
      {!posts && !error && <p>Loading...</p>}
      {posts && posts.map((post) => (
        <div key={post.id} style={{
          padding: "10px",
          border: "1px solid #ccc",
          borderRadius: "5px",
        }}>
          <div style={{
            display: "flex",
            gap: "10px",
          }}>
            <p><b>{post.preview}</b></p>
            <p><i>{post.authorUsername}</i></p>
          </div>
          <input type="button" value="View Details" onClick={() => navigate(`/post/${post.id}`)} style={{
            padding: "5px 10px",
            backgroundColor: "#2563eb",
            color: "white",
            border: "none",
            cursor: "pointer",
          }} />
        </div>
      ))}
    </>
  )
}

export default PostsList;