import { ErrorBox } from '@/components/ui/error-box';

import { siteConfig } from '@/constant/config';

export default function InvalidToken() {
  return (
    <div className='page-gradient-bg gradient-rose'>
      <ErrorBox title='No configuration token found!'>
        <p>
          Please use the{' '}
          <code className='bg-red-100 px-1 py-0.5 rounded text-red-700'>/configure</code> command in
          your
          <a
            className='text-red-700 font-medium underline ml-1 hover:text-red-800 transition-colors'
            href={`https://t.me/${siteConfig.telegramBotName}`}
          >
            Telegram Bot{' '}
          </a>
          to get a valid configuration link.
        </p>
      </ErrorBox>
    </div>
  );
}
