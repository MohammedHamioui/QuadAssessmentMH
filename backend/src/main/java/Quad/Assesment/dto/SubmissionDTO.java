package Quad.Assesment.dto;

import lombok.Data;

@Data
//input van de gebruiker
public class SubmissionDTO {
	private int questionIndex;
    private String submittedAnswer;
}
