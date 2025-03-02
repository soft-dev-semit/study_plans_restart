package csit.semit.studyplansrestart.dto.returnData;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SemesterDTO {
    private Long id;
    private int auditHours;
    private int creditsECTS;
    private boolean hasExam;
    private boolean hasCredit;
    private int semester;
}
