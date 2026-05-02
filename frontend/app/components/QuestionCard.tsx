interface QuestionCardProps {
  question: string;
  category: string;
  currentIndex: number;
  total: number;
}

export default function QuestionCard({ question, category, currentIndex, total }: QuestionCardProps) {
  return (
    <>
      <p className="text-indigo-400 text-sm uppercase mb-1">{category}</p>
      <p className="text-zinc-400 text-sm mb-4">Question {currentIndex + 1} of {total}</p>
      <h2 className="text-white text-2xl font-bold mb-8">{question}</h2>
    </>
  );
}