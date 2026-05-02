package Quad.Assesment.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
//bevat de set van vragen
public class TriviaResponse {
	
	@JsonProperty("response_code")
	private int responseCode;
		    
	private List<TriviaQuestion> results;

}
