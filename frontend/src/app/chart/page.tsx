import mockData from '@/__mocks__/generated_graph_data.json';

import MentalHealthChart from './Chart';

export default function Home() {
  return (
    <main className='min-h-screen bg-gray-50 p-8'>
      <MentalHealthChart data={mockData} />
    </main>
  );
}
