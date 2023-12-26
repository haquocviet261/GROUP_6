import React, { useState, useEffect } from 'react';

function ImageComponent() {
  const [imageUrl, setImageUrl] = useState('');

  useEffect(() => {
    // Fetch image URL from backend API
    fetchImageFromBackend(); // Call your backend API to get the image URL
  }, []);

  const fetchImageFromBackend = async () => {
    try {
      // Fetch image URL from your backend API
      const response = await fetch('your-backend-endpoint-for-image');
      if (response.ok) {
        const imageData = await response.json(); // Assuming your response is JSON containing image data
        setImageUrl(imageData.imageUrl); // Set the received image URL in state
      } else {
        // Handle error if fetching image fails
        console.error('Failed to fetch image');
      }
    } catch (error) {
      console.error('Error fetching image:', error);
    }
  };

  return (
    <div>
      {imageUrl ? (
        <img src={imageUrl} alt="Backend Image" />
      ) : (
        <p>Loading image...</p>
      )}
    </div>
  );
}

export default ImageComponent;
