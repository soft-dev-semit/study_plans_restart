package csit.semit.studyplansrestart.dto;

import csit.semit.studyplansrestart.entity.Curriculum;
import csit.semit.studyplansrestart.entity.Discipline;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisciplineCurriculumDTO {
    private int labHours;
    private int lecHours;
    private int practiceHours;
    private String individualTaskType;
    private String fileURL;
    private Curriculum curriculum;
    private Discipline discipline;
}
