package Quad.Assesment.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import Quad.Assesment.dto.QuestionDTO;
import Quad.Assesment.dto.ResultDTO;
import Quad.Assesment.dto.SubmissionDTO;
import Quad.Assesment.service.TriviaService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class TriviaController {

    private final TriviaService service;

    //TriviaService wordt geïnjecteerd zodat we hem kunnen mocken in tests (DI)
    public TriviaController(TriviaService service) {
        this.service = service;
    }

    //Geeft vragen terug zonder correctAnswer
    @GetMapping("/questions")
    public List<QuestionDTO> getQuestions() {
        return service.getQuestions();
    }

    //Controleert het ingediende antwoord
    @PostMapping("/checkanswers")
    public ResultDTO checkAnswer(@RequestBody SubmissionDTO submission) {
        return service.checkAnswer(submission);
    }
}