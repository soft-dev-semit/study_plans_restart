package csit.semit.studyplansrestart.dto.returnData;

import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PlansRow {
  private int id;
  private DisciplineCurriculumDTO disciplineCurriculum;
  private DisciplineDTO discipline;
  private List<SemesterDTO> semesters;
}
