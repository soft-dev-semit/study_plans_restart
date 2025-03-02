package csit.semit.studyplansrestart.dto.create;

import csit.semit.studyplansrestart.entity.DisciplineCurriculum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSemesterDTO {
    private int auditHours;
    private int creditsECTS;
    private boolean hasExam;
    private boolean hasCredit;
    private int semester;
    private DisciplineCurriculum discipline_curriculum;
}
