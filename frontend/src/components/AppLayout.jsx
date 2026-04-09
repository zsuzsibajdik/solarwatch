import Navbar from './Navbar';

export default function AppLayout({ children }) {
  return (
    <div className="min-h-dvh bg-base-200">
      <Navbar />
      <main className="max-w-5xl mx-auto p-4 sm:p-6">
        {children}
      </main>
    </div>
  );
}

