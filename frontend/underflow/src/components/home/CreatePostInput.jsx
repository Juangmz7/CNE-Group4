import React from "react";
import axios from "axios";
import {mutate} from "swr";

const CreatePostInput = ({ token }) => {
  const [content, setContent] = React.useState("");

  const createPost = async () => {
    if (content === "") {
      throw new Error("No content");
    }

    try {
      await axios.post(
        "/post",
        {
          content,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          }
        }
      );

      await mutate('/post');
    } finally {
      setContent("");
    }
  };

  const styles = {
    input: { padding: '10px', fontSize: '16px' },
    button: {
      padding: '10px',
      backgroundColor: '#2563eb',
      color: 'white',
      border: '1px solid #2563eb',
      cursor: 'pointer',
      fontSize: '16px',
    }
  };

  return (
    <>
      <input style={styles.input} type="text" placeholder="What's on your mind?" value={content} onChange={(e) => setContent(e.target.value)} />
      <input style={{ ...styles.button, marginLeft: '10px' }} type="button" value="Create" onClick={createPost} />
    </>
  )
}

export default CreatePostInput;