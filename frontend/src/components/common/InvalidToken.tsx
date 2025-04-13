import {AlertCircle} from "lucide-react";
import React from "react";

import {siteConfig} from "@/constant/config";

export default function InvalidToken() {
  return (
      <div
          className='min-h-screen bg-gradient-to-b from-white to-red-100 flex items-center justify-center p-6'>
        <div className='mb-6 p-5 bg-white text-red-600 rounded-lg border border-red-100'>
          <div className='flex items-center mb-3 gap-x-2'>
            <AlertCircle/>
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
      </div>
  )
}