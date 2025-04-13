import {siteConfig} from "@/constant/config";
import React from "react";

export default function InvalidToken() {
  return (
      <div className='mb-6 p-5 bg-red-50 text-red-600 rounded-lg border border-red-100'>
        <div className='flex items-center mb-3'>
          <svg
              className='w-5 h-5 mr-2 text-red-500'
              fill='none'
              stroke='currentColor'
              viewBox='0 0 24 24'
              xmlns='http://www.w3.org/2000/svg'
          >
            <path
                strokeLinecap='round'
                strokeLinejoin='round'
                strokeWidth={2}
                d='M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z'
            />
          </svg>
          <span className='font-medium'>No configuration token found!</span>
        </div>
        <p className='mt-2 text-red-600 text-sm'>
          Please use the{' '}
          <code className='bg-red-100 px-1 py-0.5 rounded text-red-700'>/configure</code>{' '}
          command in your
          <a
              className='text-red-700 font-medium underline ml-1 hover:text-red-800 transition-colors'
              href={`https://t.me/${siteConfig.telegramBotName}`}
          >
            Telegram Bot{' '}
          </a>
          to get a valid configuration link.
        </p>
      </div>
  )
}