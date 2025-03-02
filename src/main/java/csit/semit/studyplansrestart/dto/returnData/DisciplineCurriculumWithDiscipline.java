package csit.semit.studyplansrestart.dto.returnData;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DisciplineCurriculumWithDiscipline {
    private Long id;
    private int labHours;
    private int lecHours;
    private int practiceHours;
    private String individualTaskType;
    private String fileURL;
    private DisciplineDTO discipline;
    private List<SemesterDTO> semesters;

}
