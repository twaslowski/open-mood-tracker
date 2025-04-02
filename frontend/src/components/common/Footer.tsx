import { Github } from 'lucide-react';
import Link from 'next/link';
import React from 'react';

import { siteConfig } from '@/constant/config';

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className='bg-gray-100 border-t border-gray-200 py-4 absolute bottom-0 left-0 right-0 h-[75px]'>
      <div className='container mx-auto px-4'>
        <div className='grid grid-cols-1 md:grid-cols-3 gap-16'>
          <div className='flex-shrink-0'>
            <p className='font-medium'>Tobias Waslowski</p>
            <p className='text-gray-600 text-sm'>© {currentYear} All Rights Reserved</p>
          </div>

          {/* Quick links in a horizontal layout */}
          <div className='flex items-center justify-center'>
            <ul className='flex flex-wrap gap-8 text-sm'>
              <li>
                <Link href='/' className='text-gray-600 hover:text-blue-600'>
                  Home
                </Link>
              </li>
              <li>
                <Link href='/about' className='text-gray-600 hover:text-blue-600'>
                  About
                </Link>
              </li>
              <li>
                <Link href='https://twaslowski.com/' className='text-gray-600 hover:text-blue-600'>
                  Blog
                </Link>
              </li>
              <li>
                <Link href='/contact' className='text-gray-600 hover:text-blue-600'>
                  Contact
                </Link>
              </li>
            </ul>
          </div>

          <div className='flex items-center justify-center gap-3'>
            <a
              href={siteConfig.gitHubRepository}
              target='_blank'
              rel='noopener noreferrer'
              className='text-gray-600 hover:text-black'
              aria-label='GitHub Repository'
            >
              <Github size={18} />
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
