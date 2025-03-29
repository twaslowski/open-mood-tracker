'use client';

import {useEffect, useState} from 'react';
import {useSearchParams} from 'next/navigation'
import Head from 'next/head';
import {siteConfig} from "@/constant/config";

export default function ConfigurePage() {
  const searchParams = useSearchParams();
  const [token, setToken] = useState('');
  const [error, setError] = useState('');

  const validateToken = async (token: string) => {
    const result = await fetch(`/api/v1/session/${token}`);
    if (!result.ok) {
      throw new Error('Invalid token');
    }
  }

  useEffect(() => {
    const urlToken = searchParams.get('token');
    if (!urlToken) {
      setError('No session token provided. Please head back to the bot and create a session using the /configure command.');
    }
      validateToken(urlToken!).then(() => {
        sessionStorage.setItem('configToken', urlToken!);
        setToken(urlToken!);
      }).catch((error) => {
        setError(error.message);
        sessionStorage.removeItem('configToken');
        setToken('');
      });
  }, [validateToken]);

  return (
      <div className="container mx-auto p-4">
        <Head>
          <title>Bot Configuration</title>
          <meta name="description" content="Configure your Telegram bot"/>
        </Head>

        <main className="max-w-md mx-auto mt-10 p-6 bg-white rounded-lg shadow-md">
          <h1 className="text-2xl font-bold mb-6">Bot Configuration</h1>

          {!error ?  (
              <div>
                <div className="mb-4 p-3 bg-green-100 text-green-800 rounded">
                  Configuration token successfully loaded!
                </div>

                <p className="mb-4">Your token: {token}</p>

                {/* Here you would add your configuration form */}
                <div className="mt-6">
                  <h2 className="text-xl font-semibold mb-3">Bot Settings</h2>
                  <p className="text-gray-600 mb-4">
                    Configure your bot preferences below. All changes will be saved automatically.
                  </p>

                  {/* Example form placeholder */}
                  <div className="space-y-4">
                    <p>Configuration form would go here...</p>
                  </div>
                </div>
              </div>
          ) : (
              <div className="mb-4 p-3 bg-red-100 text-red-500 rounded">
                <p >No configuration token found!</p>
                <p className="mt-2 text-red-500">
                  Please use the /configure command in your
                  <a className='underline' href={`https://t.me/${siteConfig.telegramBotName}`}> Telegram Bot </a>
                  to get a valid configuration link.
                </p>
              </div>
          )}
        </main>
      </div>
  );
}