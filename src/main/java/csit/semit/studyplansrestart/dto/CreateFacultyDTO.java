package csit.semit.studyplansrestart.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFacultyDTO {
    public String code;
    public String name;
}
