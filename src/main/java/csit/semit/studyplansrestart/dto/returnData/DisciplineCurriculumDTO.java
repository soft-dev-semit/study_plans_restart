package csit.semit.studyplansrestart.dto.returnData;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisciplineCurriculumDTO {
    private Long id;
    private int labHours;
    private int lecHours;
    private int practiceHours;
    private String individualTaskType;
}
