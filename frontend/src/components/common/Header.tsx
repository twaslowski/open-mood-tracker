'use client';

import { Menu, X} from 'lucide-react';
import Link from 'next/link';
import React, {useState} from 'react';

const Header: React.FC = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  return (
      <header className='bg-white shadow-sm absolute top-0 left-0 right-0 h-[60px]'>
        <div className='container mx-auto px-4'>
          <div className='flex items-center justify-between h-16'>
            {/* Logo */}
            <div className='flex items-center'>
              <Link href='/' className='flex items-start'>
                <span className='text-xl font-bold text-primary-600'>linker</span>
              </Link>
            </div>

            {/* Desktop Navigation */}
            <nav className='hidden md:flex justify-center items-center space-x-12 flex-grow'>
              <Link
                  href='/'
                  className='text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium'
              >
                Home
              </Link>
              <Link
                  href='/about'
                  className='text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium'
              >
                About
              </Link>
              <Link
                  href='/contact'
                  className='text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium'
              >
                Contact
              </Link>
            </nav>

            <div className='flex items-center space-x-4'>
              <div className='hidden md:block'>
              </div>
              <button
                  onClick={() => setIsMenuOpen(!isMenuOpen)}
                  className='md:hidden inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-blue-500'
              >
                <span className='sr-only'>
                  {isMenuOpen ? 'Close main menu' : 'Open main menu'}
                </span>
                {isMenuOpen ? (
                    <X className='block h-6 w-6' aria-hidden='true'/>
                ) : (
                    <Menu className='block h-6 w-6' aria-hidden='true'/>
                )}
              </button>
            </div>
          </div>
        </div>

        {/* Mobile Menu */}
        {isMenuOpen && (
            <div className='md:hidden'>
              <div className='px-2 pt-2 pb-3 space-y-1 sm:px-3'>
                <Link
                    href='/'
                    className='block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:text-gray-900 hover:bg-gray-50'
                >
                  Home
                </Link>
                <Link
                    href='/about'
                    className='block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:text-gray-900 hover:bg-gray-50'
                >
                  About
                </Link>
                <Link
                    href='/contact'
                    className='block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:text-gray-900 hover:bg-gray-50'
                >
                  Contact
                </Link>
              </div>
            </div>
        )}
      </header>
  );
};

export default Header;