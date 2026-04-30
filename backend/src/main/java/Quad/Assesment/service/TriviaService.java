package Quad.Assesment.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

import Quad.Assesment.client.TriviaClient;
import Quad.Assesment.dto.QuestionDTO;
import Quad.Assesment.dto.ResultDTO;
import Quad.Assesment.dto.SubmissionDTO;
import Quad.Assesment.model.TriviaQuestion;

@Service
//Business logica, haalt vragen op, verbergt correctAnswer en controleert antwoorden
public class TriviaService {

    private final TriviaClient client;
    private List<TriviaQuestion> currentQuestions = new ArrayList<>();

    // TriviaClient wordt geïnjecteerd zodat we hem kunnen mocken in tests (DI)
    public TriviaService(TriviaClient client) {
        this.client = client;
    }

    // Haal vragen op en sla ze op in memory
    public List<QuestionDTO> getQuestions() {
        currentQuestions = client.fetchQuestions().getResults();
        return currentQuestions.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Vergelijk ingediend antwoord met het correcte antwoord
    public ResultDTO checkAnswer(SubmissionDTO submission) {
        TriviaQuestion question = currentQuestions.get(submission.getQuestionIndex());

        boolean isCorrect = question.getCorrectAnswer() // correctAnswer vergelijken met ingediend antwoord
                .equalsIgnoreCase(submission.getSubmittedAnswer());

        return ResultDTO.builder() // geef resultaat terug inclusief correct antwoord
                .correct(isCorrect)
                .correctAnswer(decode(question.getCorrectAnswer()))
                .build();
    }

    // Maak DTO zonder correctAnswer
    private QuestionDTO mapToDTO(TriviaQuestion question) {
        List<String> allAnswers = new ArrayList<>(question.getIncorrectAnswers());
        
        // Voeg correct antwoord toe en schud de antwoorden door elkaar
        allAnswers.add(question.getCorrectAnswer());
        Collections.shuffle(allAnswers);

        return QuestionDTO.builder() // bouw DTO zonder correctAnswer, decode HTML tekens
                .question(decode(question.getQuestion())) 
                .category(decode(question.getCategory())) 
                .difficulty(question.getDifficulty()) 
                .answers(allAnswers.stream().map(this::decode).collect(Collectors.toList()))
                .build(); // bouw DTO
    }

    // Zet HTML tekens om naar leesbare tekens bv: &amp; → &
    private String decode(String text) {
        return StringEscapeUtils.unescapeHtml4(text);
    }
}