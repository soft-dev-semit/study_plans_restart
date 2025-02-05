package csit.semit.studyplansrestart.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisciplineDTO {
    public Long id;
    public String name;
    public String short_name;
}
