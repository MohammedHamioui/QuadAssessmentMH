export interface Question {
  question: string;
  category: string;
  difficulty: string;
  answers: string[];
}

export interface Result {
  correct: boolean;
  correctAnswer: string;
}