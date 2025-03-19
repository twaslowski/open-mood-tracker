import React from 'react';
import Image from "next/image";

const MoodTrackerCover = () => {
  return (
      <div className="flex-grow min-h-screen bg-gradient-to-b from-white to-blue-100 flex flex-col items-center justify-center p-4">

        <Image src="/images/moody_logo.png"
               alt="mood tracker logo"
               width={250}
               height={100}
        />

        {/* Title */}
        <h1 className="text-6xl font-bold text-primary-500 mb-4">moody</h1>

        {/* Subtitle */}
        <p className="text-xl text-primary-400 mb-8">Track your mood, understand your patterns</p>

        {/* Get Started Button */}
        <a href="https://t.me/open_mood_tracker_bot">
        <button
            className="bg-primary-500 hover:bg-primary-600 text-white font-semibold py-3 px-8 rounded-full shadow-lg transform transition duration-300 hover:scale-105">
          Get Started
        </button>
        </a>
      </div>
  );
};

export default MoodTrackerCover;