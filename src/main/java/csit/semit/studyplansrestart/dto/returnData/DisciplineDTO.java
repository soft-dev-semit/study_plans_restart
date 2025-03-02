package csit.semit.studyplansrestart.dto.returnData;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisciplineDTO {
    public Long id;
    public String name;
    public String shortName;
}
