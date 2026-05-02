interface AnswerButtonProps {
  answer: string;
  onClick: () => void;
  style: string;
}

export default function AnswerButton({ answer, onClick, style }: AnswerButtonProps) {
  return (
    <button onClick={onClick} className={`p-4 rounded-xl font-semibold transition-all ${style}`}>
      {answer}
    </button>
  );
}