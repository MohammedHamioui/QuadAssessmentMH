package Quad.Assesment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
//bevat het resultaat na het checken van het antwoord
public class ResultDTO {
    private boolean correct;
    private String correctAnswer; //mag wel hier, dit is de response NA het checken
}
