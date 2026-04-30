import Link from 'next/link'

export default function Home() {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-zinc-900">

      <main className="flex flex-col items-center justify-center text-center z-10 px-4">

        {/* Titel */}
        <h1 className="text-7xl font-extrabold text-white mb-4 tracking-tight">
          Trivia<span className="text-indigo-400">.</span>
        </h1>

        {/* Start knop */}
        <Link href="/trivia">
          <button className="px-10 py-4 bg-indigo-500 hover:bg-indigo-600 text-white text-lg font-bold rounded-2xl transition-all duration-300 hover:scale-105">
            Start Quiz
          </button>
        </Link>

      </main>
    </div>
  );
}