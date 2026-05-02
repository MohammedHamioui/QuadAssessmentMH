# QuadAssessment
## Trivia Quiz

### Developer
- Mohammed Hamioui

### Technologies Used
- **Java 17** (Spring Boot)  Back-end
- **Next.js** (TypeScript)  Front-end

---

## Contents
- [Introduction](#introduction)
- [Architecture](#architecture)
- [Back-end](#back-end)
- [Unit Tests](#unit-tests)
- [Front-end](#front-end)
- [Getting Started](#getting-started)

---

## Introduction

This is a trivia quiz application that fetches questions from the [Open Trivia Database](https://opentdb.com/) API and presents them to the user through a simple interface.

The key requirement of this project is that the correct answer is **never sent to the frontend**. The backend acts as a middleware layer that hides the correct answer and only reveals it after the user has submitted their answer.

If you try to cheat by inspecting the network requests in DevTools, you won't be able to find the correct answer. The backend only sends the shuffled answers without indicating which one is correct the `correctAnswer` is stored server-side and never included in the response to the frontend.

---

## Architecture

<img width="1271" height="624" alt="image" src="https://github.com/user-attachments/assets/fb0208f8-7b0e-4974-83d5-a1a5ef32b285" />

- The **frontend** sends a `GET /api/questions` request to the backend and displays the questions.
- The **backend** fetches questions from OpenTDB, removes the correct answer from the response, and returns only the shuffled answers.
- When the user submits an answer, the frontend sends a `POST /api/checkanswers` request to the backend, which checks the answer server-side and returns the result.

> The `correctAnswer` field is stored in memory on the server and is **never included in the response to the frontend**.

---

## Back-end

The back-end is built with **Spring Boot** (Java 17) and follows a layered architecture. Below is an overview of the key concepts and libraries used.

**Dependency Injection (DI)** is used throughout the project. Instead of creating objects manually, Spring injects them automatically via constructor injection. This makes the code easier to test and more flexible.

**Lombok** is used to reduce boilerplate code. Annotations like `@Data` and `@Builder` automatically generate getters, setters, and constructors at compile time.

**RestTemplate** is used in the `TriviaClient` to make HTTP requests to the OpenTDB API. It is registered as a `@Bean` in `App.java` so Spring can inject it where needed.

**Jackson** handles the mapping between JSON and Java objects automatically. Annotations like `@JsonProperty` are used to map JSON fields with different naming conventions (e.g. `correct_answer` → `correctAnswer`).

**Apache Commons Text** is used to decode HTML entities in the API response (e.g. `&amp;` → `&`).

The back-end communicates with the [Open Trivia Database](https://opentdb.com/api_config.php) API, which provides 10 random trivia questions per request via the following endpoint:

```
GET https://opentdb.com/api.php?amount=10
```

The response is a JSON object containing a `response_code` and a `results` array with 10 questions. Each question contains a `correct_answer` and a list of `incorrect_answers`.

```json
{
    "response_code": 0,
    "results": [
        {
            "type": "boolean",
            "difficulty": "easy",
            "category": "Entertainment: Musicals &amp; Theatres",
            "question": "IMAX stands for Image Maximum.",
            "correct_answer": "True",
            "incorrect_answers": [
                "False"
            ]
        },
        {
            "type": "multiple",
            "difficulty": "easy",
            "category": "Entertainment: Cartoon &amp; Animations",
            "question": "Which of these characters live in a pineapple under the sea in the cartoon &quot;SpongeBob SquarePants&quot;.",
            "correct_answer": "SpongeBob SquarePants ",
            "incorrect_answers": [
                "Patrick Star",
                "Squidward Tentacles",
                "Mr. Krabs"
            ]
        },
        {
            "type": "multiple",
            "difficulty": "easy",
            "category": "Entertainment: Video Games",
            "question": "How many flagship monsters appear in Monster Hunter Gernerations?",
            "correct_answer": "4",
            "incorrect_answers": [
                "1",
                "2",
                "3"
            ]
        },
        {
            "type": "multiple",
            "difficulty": "hard",
            "category": "Entertainment: Japanese Anime &amp; Manga",
            "question": "Which animation studio animated &quot;Psycho Pass&quot;?",
            "correct_answer": "Production I.G",
            "incorrect_answers": [
                "Kyoto Animation",
                "Shaft",
                "Trigger"
            ]
        },
        {
            "type": "multiple",
            "difficulty": "medium",
            "category": "General Knowledge",
            "question": "After how many years would you celebrate your crystal anniversary?",
            "correct_answer": "15",
            "incorrect_answers": [
                "20",
                "10",
                "25"
            ]
        },
        {
            "type": "multiple",
            "difficulty": "easy",
            "category": "Science &amp; Nature",
            "question": "What is the powerhouse of the cell?",
            "correct_answer": "Mitochondria",
            "incorrect_answers": [
                "Ribosome",
                "Redbull",
                "Nucleus"
            ]
        },
        {
            "type": "boolean",
            "difficulty": "medium",
            "category": "History",
            "question": "In World War II, Hawker Typhoons served in the Pacific theater.",
            "correct_answer": "False",
            "incorrect_answers": [
                "True"
            ]
        },
        {
            "type": "multiple",
            "difficulty": "easy",
            "category": "Entertainment: Cartoon &amp; Animations",
            "question": "What is the name of the creatures that the protagonists of the webshow RWBY fight against?",
            "correct_answer": "Grimm",
            "incorrect_answers": [
                "Reavers",
                "Heartless",
                "Dark Ones"
            ]
        },
        {
            "type": "boolean",
            "difficulty": "easy",
            "category": "Geography",
            "question": "South Africa has more than one capital city",
            "correct_answer": "True",
            "incorrect_answers": [
                "False"
            ]
        },
        {
            "type": "multiple",
            "difficulty": "easy",
            "category": "Entertainment: Video Games",
            "question": "Rocket League is a game which features..",
            "correct_answer": "Cars",
            "incorrect_answers": [
                "Helicopters",
                "Planes",
                "Submarines"
            ]
        }
    ]
}
```

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/questions` | Returns 10 questions without `correctAnswer` |
| POST | `/api/checkanswers` | Checks the submitted answer and returns the result |

### GET /api/questions  Example Response

```json
[
  {
    "question": "What is the capital of France?",
    "category": "Geography",
    "difficulty": "easy",
    "answers": ["Berlin", "Paris", "Madrid", "Rome"]
  }
]
```

### POST /api/checkanswers  Example Request

```json
{
  "questionIndex": 0,
  "submittedAnswer": "Paris"
}
```

### POST /api/checkanswers  Example Response

```json
{
  "correct": true,
  "correctAnswer": "Paris"
}
```

### Models

#### TriviaResponse
`TriviaResponse` is the wrapper object for the full OpenTDB API response. It contains a `responseCode` indicating whether the request was successful, and a `results` list holding the 10 `TriviaQuestion` objects. Spring's `RestTemplate` automatically maps the raw JSON into this object via Jackson.

```java
@Data
//bevat de set van vragen
public class TriviaResponse {
	
	@JsonProperty("response_code")
	private int responseCode;
		    
	private List<TriviaQuestion> results;
}
```

#### TriviaQuestion
`TriviaQuestion` represents a single question from the OpenTDB API response. Each field is mapped from the raw JSON using `@JsonProperty` to handle the naming difference between JSON (snake_case) and Java (camelCase). The `correctAnswer` field is stored server-side and is never included in the response to the frontend.

```java
@Data
//mapped de JSON van de OpenTDB API naar een Java object
public class TriviaQuestion {
	private String type, difficulty, category, question;
    
    @JsonProperty("correct_answer")
    private String correctAnswer; 
    
    @JsonProperty("incorrect_answers")
    private List<String> incorrectAnswers;
}
```

### DTOs

#### QuestionDTO
Sent to the frontend after fetching questions. Does not contain `correctAnswer` this is the key security measure of the application.

```java
@Data
@Builder
//verstuurt vragen naar de frontend zonder correctAnswer
public class QuestionDTO {
    private String question, category, difficulty;
    private List<String> answers;
}
```

#### ResultDTO
Returned after the user submits an answer. `correctAnswer` is included here because the user has already answered  it is used to show the correct answer in the frontend.

```java
@Data
@Builder
//bevat het resultaat na het checken van het antwoord
public class ResultDTO {
    private boolean correct;
    private String correctAnswer; //mag wel hier, dit is de response NA het checken
}
```

#### SubmissionDTO
Received from the frontend when the user clicks an answer. The `questionIndex` is used to look up the correct answer from the server-side list.

```java
@Data
//input van de gebruiker
public class SubmissionDTO {
	private int questionIndex;
    private String submittedAnswer;
}
```

### Client
The `TriviaClient` communicates with the Open Trivia Database API to retrieve the questions and map them into Java objects via `RestTemplate`.

```java
@Component 
public class TriviaClient {

    private final RestTemplate restTemplate;

    //RestTemplate wordt geïnjecteerd zodat we hem kunnen mocken in tests (DI)
    public TriviaClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //fetch de vragen en zet ze om naar objecten
    public TriviaResponse fetchQuestions() {
        return restTemplate.getForObject("https://opentdb.com/api.php?amount=10",TriviaResponse.class);
    }
}
```

### Service
The `TriviaService` contains the core business logic of the application. It fetches questions from the `TriviaClient`, stores them in memory, and handles answer checking.

```java
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
```

**`getQuestions()`** fetches the questions from the API and stores them in `currentQuestions`. Each question is mapped to a `QuestionDTO`  without the `correctAnswer`  before being sent to the frontend.

**`checkAnswer()`** retrieves the correct answer from `currentQuestions` using the `questionIndex` and compares it to the submitted answer. The result is returned as a `ResultDTO`.

**`mapToDTO()`** combines the correct and incorrect answers into one list, shuffles them, and builds a `QuestionDTO` without the `correctAnswer`.

**`decode()`** converts HTML entities in the API response to readable characters using Apache Commons Text (e.g. `&amp;` → `&`).

### Controller
The `TriviaController` exposes two endpoints and delegates all logic to the `TriviaService`. It does not contain any business logic itself.

```java
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
```

`@CrossOrigin` allows the frontend running on `localhost:3000` to make requests to the backend on `localhost:8080`. Without this, the browser would block the requests due to CORS policy.

`@RequestBody` on the `checkAnswer` method tells Spring to automatically deserialize the incoming JSON into a `SubmissionDTO` object.

---

## Unit Tests

The unit tests cover the core business logic in `TriviaService`. Tests use **Mockito** to mock the `TriviaClient` so no real API calls are made during testing.

<img width="848" height="140" alt="image" src="https://github.com/user-attachments/assets/200536dc-a166-4bac-b815-39b31644a2b2" />

| Test | Description |
|------|-------------|
| `getQuestions_returnsQuestions` | Verifies that questions are returned |
| `getQuestions_doesNotExposeCorrectAnswer` | Verifies that `correctAnswer` is not in the DTO |
| `checkAnswer_correct` | Verifies that a correct answer returns `correct: true` |
| `checkAnswer_incorrect` | Verifies that an incorrect answer returns `correct: false` |

---

## Front-end

For the front-end of this application I decided to choose Next.js due to its built-in TypeScript support, file-based routing, and its wide adoption in the industry. TypeScript allows the use of interfaces to define the shape of the data received from the backend, making the code more readable and less error prone.

### api.js

```js
javascript
import axios from 'axios';
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});
export default api;
```

I define the base url of my backend api so i can access the endpoints.

### Interfaces

```ts
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
```

These TypeScript interfaces mirror the DTOs from the backend. `Question` matches the `QuestionDTO` 
and `Result` matches the `ResultDTO`. They ensure that the data received from the backend is 
correctly typed throughout the frontend.

### Trivia page

```tsx
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
```

First the state variables are defined.

```tsx
const [questions, setQuestions] = useState<Question[]>([]); // State voor de vragenlijst
  const [currentIndex, setCurrentIndex] = useState(0); // State voor de huidige vraag index
  const [loading, setLoading] = useState(true); // State voor het laadproces
  const [result, setResult] = useState<Result | null>(null); // State voor het resultaat van het antwoord
  const [selectedAnswer, setSelectedAnswer] = useState<string | null>(null); // State voor het geselecteerde antwoord
```

Next, `useEffect` is used to fetch the questions from the backend when the page loads. 
The questions are stored in the `questions` state. The empty array `[]` ensures the fetch 
only happens once  on initial page load.

```tsx
  // Haal de vragen op bij het laden van de component
  useEffect(() => {
    api.get('/questions').then(res => {
      setQuestions(res.data);
      setLoading(false);
    });
  }, []);
```

When the user clicks an answer, `handleAnswer` sends a `POST` request to `/checkanswers` 
with the `questionIndex` and the `submittedAnswer`. The result is stored in the `result` state. 
The `if (selectedAnswer) return` prevents the user from submitting multiple answers for the same question.

```tsx
  //verwerk het antwoord van de gebruiker en vraag de backend om te controleren of het correct is
  const handleAnswer = async (answer: string) => {
    if (selectedAnswer) return;
    setSelectedAnswer(answer);
    const res = await api.post('/checkanswers', { questionIndex: currentIndex, submittedAnswer: answer });
    setResult(res.data);
  };
```

To go to the next question, `handleNext` increments the `currentIndex` and resets the `result` 
and `selectedAnswer` state. The `currentIndex` also tracks the current question number, 
which is displayed to the user as "Question X of 10".

```tsx
  //naar de volgende vraag gaan
  const handleNext = () => {
    setCurrentIndex(currentIndex + 1);
    setResult(null);
    setSelectedAnswer(null);
  };
```

`getButtonStyle` returns the correct Tailwind CSS class for each answer button based on the result. 
If no answer is selected yet, all buttons are blue. Once an answer is submitted, the correct answer 
turns green and the wrong selected answer turns red. All other buttons turn grey.

```tsx
  const getButtonStyle = (answer: string) => {
    if (!selectedAnswer) return 'bg-indigo-500 hover:bg-indigo-600 text-white';
    if (result && answer === result.correctAnswer) return 'bg-green-500 text-white';
    if (answer === selectedAnswer && !result?.correct) return 'bg-red-500 text-white';
    return 'bg-zinc-700 text-zinc-400';
  };
```

When your finished with the questions, you will see a button that redirects you back to the homepage so you can start over again.

```tsx
//wanneer je klaar bent, toon het eindscherm om terug te gaan naar de homepagina
  if (currentIndex >= questions.length) return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-zinc-900 gap-4">
      <h2 className="text-white text-3xl font-bold">Finished!</h2>
      <button onClick={() => window.location.href = '/'} className="px-6 py-3 bg-indigo-500 text-white rounded-xl hover:bg-indigo-600">
        Back to Home
      </button>
    </div>
  );
```

The return statement renders the question and answer buttons. The category and question number 
are displayed at the top. Each answer is rendered as a button `getButtonStyle` determines 
the color based on the result. Once an answer is submitted, the "Next Question" button appears. 
When the last question is reached, the button changes to "Finish".

```tsx
 return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-zinc-900 px-4">
      <div className="p-8 bg-zinc-800 rounded-2xl w-full max-w-xl">

        <QuestionCard
          question={question.question}
          category={question.category}
          currentIndex={currentIndex}
          total={questions.length}
        />

        <div className="flex flex-col gap-3">
          {question.answers.map((answer, index) => (
            <AnswerButton
              key={index}
              answer={answer}
              onClick={() => handleAnswer(answer)}
              style={getButtonStyle(answer)}
            />
          ))}
        </div>

        {result && (
          <NextButton
            onClick={handleNext}
            isLast={currentIndex + 1 >= questions.length}
          />
        )}

      </div>
    </div>
  );
```
This is the application in its final form.

https://github.com/user-attachments/assets/cb690af6-3b41-4c39-8ec3-2dccb9447752

If you try to cheat by inspecting the network requests in DevTools, you won't be able to 
find the correct answer. The backend only sends the shuffled answers without indicating 
which one is correct  the `correctAnswer` is stored server-side and never included in 
the response to the frontend.

<img width="1920" height="914" alt="image" src="https://github.com/user-attachments/assets/2bb6bd63-7456-4412-a881-428c1c5919b3" />

---

## Getting Started

### Prerequisites
- Java 17
- Maven
- Node.js

### Run the Back-end
1. Clone the repository
2. Navigate to the back-end folder
3. Run the application:
```bash
mvn spring-boot:run
```
The back-end will start on `http://localhost:8080`

### Run the Front-end
1. Switch to the `frontend` branch
2. Install dependencies:
```bash
npm install
```
3. Start the application:
```bash
npm run dev
```
The front-end will start on `http://localhost:3000`
