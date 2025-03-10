package csit.semit.studyplansrestart.dto.create;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSpecialtyDTO {
    public String code;
    public String code_name;
    public String name;
    public int number;
}
