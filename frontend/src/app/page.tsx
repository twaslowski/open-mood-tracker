'use client';

import Image from 'next/image';
import React from 'react';

import { siteConfig } from '@/constant/config';

const MoodTrackerCover = () => {
  return (
    <div className='page-gradient-bg gradient-blue flex-col p-4'>
      <Image src='/images/moody_logo.png' alt='mood tracker logo' width={250} height={100} />

      <h1 className='text-6xl font-bold text-primary-500 mb-4'>moody</h1>

      <div className='text-xl text-primary-400 mb-8 text-center'>
        <p>Track your mood. Understand your patterns.</p>
        <p>
          Understand <b>yourself</b>.
        </p>
      </div>

      <div className='justify-center flex space-x-2'>
        <a href={`https://t.me/${siteConfig.telegramBotName}`}>
          <button className='bg-primary-500 hover:bg-primary-600 text-white font-semibold py-3 px-8 rounded-full shadow-lg transform transition duration-300 hover:scale-105'>
            Get Started
          </button>
        </a>
        <a href={`https://t.me/${siteConfig.telegramBotName}`}>
          <button className='bg-white text-primary-500 font-semibold py-3 px-8 rounded-full shadow-lg transform transition duration-300 hover:scale-105'>
            Learn More
          </button>
        </a>
      </div>
    </div>
  );
};

export default MoodTrackerCover;
