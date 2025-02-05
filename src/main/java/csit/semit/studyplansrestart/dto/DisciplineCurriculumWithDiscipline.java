package csit.semit.studyplansrestart.dto;

import csit.semit.studyplansrestart.entity.Discipline;
import csit.semit.studyplansrestart.entity.Semester;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DisciplineCurriculumWithDiscipline {
    private int labHours;
    private int lecHours;
    private int practiceHours;
    private String individualTaskType;
    private String fileURL;
    private Discipline discipline;
    private List<Semester> semesters;

}
