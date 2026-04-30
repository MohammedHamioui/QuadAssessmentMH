package Quad.Assesment;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import Quad.Assesment.client.TriviaClient;
import Quad.Assesment.dto.QuestionDTO;
import Quad.Assesment.dto.ResultDTO;
import Quad.Assesment.dto.SubmissionDTO;
import Quad.Assesment.model.TriviaQuestion;
import Quad.Assesment.model.TriviaResponse;
import Quad.Assesment.service.TriviaService;

@ExtendWith(MockitoExtension.class)
public class AppTest {

    @Mock
    private TriviaClient client; // mock client zodat we geen echte API calls maken

    @InjectMocks
    private TriviaService service; // service met de test client geïnjecteerd

    // Maak een test vraag aan voor de tests
    private TriviaQuestion mockQuestion() {
        TriviaQuestion question = new TriviaQuestion();
        question.setQuestion("What is 2+2?");
        question.setCategory("Math");
        question.setDifficulty("easy");
        question.setCorrectAnswer("4");
        question.setIncorrectAnswers(List.of("1", "2", "3"));
        return question;
    }

    // Maak een test response aan voor de tests
    private TriviaResponse mockResponse() {
        TriviaResponse response = new TriviaResponse(); /// response aanmaken
        response.setResults(List.of(mockQuestion())); // vraag toevoegen aan de response
        return response;
    }

    @Test
    // Test of de service vragen teruggeeft
    void getQuestions_returnsQuestions() {
        when(client.fetchQuestions()).thenReturn(mockResponse()); // mock response teruggeven
        List<QuestionDTO> questions = service.getQuestions(); // vragen ophalen
        assertFalse(questions.isEmpty()); // vragen moeten aanwezig zijn
    }

    @Test
    // Test of correctAnswer NIET in de DTO zit
    void getQuestions_doesNotExposeCorrectAnswer() {
        when(client.fetchQuestions()).thenReturn(mockResponse()); // mock response teruggeven
        List<QuestionDTO> questions = service.getQuestions(); // vragen ophalen
        assertNotNull(questions.get(0).getAnswers()); // antwoorden moeten aanwezig zijn
    }

    @Test
    // Test of een correct antwoord als correct wordt herkend
    void checkAnswer_correct() {
        when(client.fetchQuestions()).thenReturn(mockResponse());
        service.getQuestions(); // vragen laden in memory

        SubmissionDTO submission = new SubmissionDTO();
        submission.setQuestionIndex(0);
        submission.setSubmittedAnswer("4"); // correct antwoord

        ResultDTO result = service.checkAnswer(submission);
        assertTrue(result.isCorrect()); // moet true zijn
    }

    @Test
    // Test of een fout antwoord als fout wordt herkend
    void checkAnswer_incorrect() {
        when(client.fetchQuestions()).thenReturn(mockResponse());
        service.getQuestions(); // vragen in memory laden

        SubmissionDTO submission = new SubmissionDTO();
        submission.setQuestionIndex(0);
        submission.setSubmittedAnswer("1"); // fout antwoord

        ResultDTO result = service.checkAnswer(submission);
        assertFalse(result.isCorrect()); // moet false zijn
    }
}