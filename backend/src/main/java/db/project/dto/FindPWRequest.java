package db.project.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindPWRequest {
    private String id;
    private int pw_question;
    private String pw_answer;
}
