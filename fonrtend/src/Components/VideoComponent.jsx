import React from "react";

const VideoComponent = () => {
  return (
    <div className="video-wrapper">
      <video className="video"
        
        preload=""
        playsInline=""
        autoPlay
        muted
        loop
      >
        <source
          src="https://dogily.vn/wp-content/uploads/2021/10/dogilybanner002.mp4"
          type="video/mp4"
        />
      </video>
      {/* <div 
        className="text"
      >
        Bold Text
      </div> */}
    </div>
  );
};

export default VideoComponent;
