'use client';

import Image from 'next/image';
import React from 'react';
import {CogIcon, RocketIcon} from "lucide-react";

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
        <a href={`https://t.me/${process.env.NEXT_PUBLIC_TELEGRAM_BOT_NAME}`}>
          <button className='btn-action-primary'>
          <RocketIcon/>
          <p>Get Started</p>
          </button>
        </a>
        <a href={`/configure`}>
          <button className='btn-action-secondary'>
              <CogIcon/>
              <p>Configure Bot</p>
          </button>
        </a>
      </div>
    </div>
  );
};

export default MoodTrackerCover;
