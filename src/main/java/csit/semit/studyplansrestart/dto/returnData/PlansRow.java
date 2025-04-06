package csit.semit.studyplansrestart.dto.returnData;

import lombok.*;

import java.util.List;

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
