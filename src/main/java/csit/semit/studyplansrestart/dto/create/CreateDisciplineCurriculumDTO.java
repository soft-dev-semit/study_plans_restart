package csit.semit.studyplansrestart.dto.create;

import csit.semit.studyplansrestart.entity.AcademGroup;
import csit.semit.studyplansrestart.entity.Curriculum;
import csit.semit.studyplansrestart.entity.Discipline;
import csit.semit.studyplansrestart.entity.SpecializedDisciplinesPackage;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDisciplineCurriculumDTO {
  private int labHours;
  private int lecHours;
  private int practiceHours;
  private String individualTaskType;
  private String fileURL;
  private Curriculum curriculum;
  private Discipline discipline;
  private AcademGroup academGroup;
  private SpecializedDisciplinesPackage specializedDisciplinesPackage;
}
