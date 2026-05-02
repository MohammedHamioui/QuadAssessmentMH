interface NextButtonProps {
  onClick: () => void;
  isLast: boolean;
}

export default function NextButton({ onClick, isLast }: NextButtonProps) {
  return (
    <button onClick={onClick} className="mt-4 w-full p-4 bg-indigo-500 text-white rounded-xl hover:bg-indigo-600 font-bold">
      {isLast ? 'Finish' : 'Next Question →'}
    </button>
  );
}