'use client';
import React, { useEffect, useState } from 'react';
import { Question, Result } from '../interfaces/trivia';
import api from '../api/api';

export default function Questions() {
  const [questions, setQuestions] = useState<Question[]>([]); // State voor de vragenlijst
  const [currentIndex, setCurrentIndex] = useState(0); // State voor de huidige vraag index
  const [loading, setLoading] = useState(true); // State voor het laadproces
  const [result, setResult] = useState<Result | null>(null); // State voor het resultaat van het antwoord
  const [selectedAnswer, setSelectedAnswer] = useState<string | null>(null); // State voor het geselecteerde antwoord

  // Haal de vragen op bij het laden van de component
  useEffect(() => {
    api.get('/questions').then(res => {
      setQuestions(res.data);
      setLoading(false);
    });
  }, []);

  //verwerk het antwoord van de gebruiker en vraag de backend om te controleren of het correct is
  const handleAnswer = async (answer: string) => {
    if (selectedAnswer) return;
    setSelectedAnswer(answer);
    const res = await api.post('/checkanswers', { questionIndex: currentIndex, submittedAnswer: answer });
    setResult(res.data);
  };

  //naar de volgende vraag gaan
  const handleNext = () => {
    setCurrentIndex(currentIndex + 1);
    setResult(null);
    setSelectedAnswer(null);
  };

  // Bepaal de stijl van de antwoordknoppen op basis van het geselecteerde antwoord en het resultaat
  const getButtonStyle = (answer: string) => {
    if (!selectedAnswer) return 'bg-indigo-500 hover:bg-indigo-600 text-white';
    if (result && answer === result.correctAnswer) return 'bg-green-500 text-white';
    if (answer === selectedAnswer && !result?.correct) return 'bg-red-500 text-white';
    return 'bg-zinc-700 text-zinc-400';
  };

  if (loading) return <p className="text-white text-center mt-20">Loading...</p>;

  //wanneer je klaar bent, toon het eindscherm om terug te gaan naar de homepagina
  if (currentIndex >= questions.length) return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-zinc-900 gap-4">
      <h2 className="text-white text-3xl font-bold">Finished!</h2>
      <button onClick={() => window.location.href = '/'} className="px-6 py-3 bg-indigo-500 text-white rounded-xl hover:bg-indigo-600">
        Back to Home
      </button>
    </div>
  );

  const question = questions[currentIndex];

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-zinc-900 px-4">
      <div className="p-8 bg-zinc-800 rounded-2xl w-full max-w-xl">

        <p className="text-indigo-400 text-sm uppercase mb-1">{question.category}</p>
        <p className="text-zinc-400 text-sm mb-4">Question {currentIndex + 1} of {questions.length}</p>
        <h2 className="text-white text-2xl font-bold mb-8">{question.question}</h2>

        <div className="flex flex-col gap-3">
          {question.answers.map((answer, index) => (
            <button key={index} onClick={() => handleAnswer(answer)} className={`p-4 rounded-xl font-semibold transition-all ${getButtonStyle(answer)}`}>
              {answer}
            </button>
          ))}
        </div>
        
        {result && (
          <button onClick={handleNext} className="mt-4 w-full p-4 bg-indigo-500 text-white rounded-xl hover:bg-indigo-600 font-bold">
            {currentIndex + 1 >= questions.length ? 'Finish' : 'Next Question →'}
          </button>
        )}

      </div>
    </div>
  );
}