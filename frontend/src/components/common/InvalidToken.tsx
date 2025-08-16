import { ErrorBox } from '@/components/ui/error-box';

export default function InvalidToken() {
  return (
    <div className='page-gradient-bg gradient-rose'>
      <ErrorBox title='No configuration token found!'>
        <p>
          Please text the
          <a
              className='text-red-700 font-medium underline ml-1 hover:text-red-800 transition-colors'
              href={`https://t.me/${process.env.NEXT_PUBLIC_TELEGRAM_BOT_NAME}`}
          >Telegram Bot</a>
          {' '} and use the {' '}
          <code className='bg-red-100 px-1 py-0.5 rounded text-red-700'>/configure</code>
          {' '} command to get a valid configuration link.
        </p>
      </ErrorBox>
    </div>
  );
}
