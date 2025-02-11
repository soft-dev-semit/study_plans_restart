package csit.semit.studyplansrestart.dto.StringCellDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ExamsInfo {
    private int firstExam = 0;
    private int secondExam = 0;
    private boolean hasExams = false;
}
