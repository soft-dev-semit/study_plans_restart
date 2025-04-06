package csit.semit.studyplansrestart.dto.returnData;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseInfo {
    private String name;
    private String shortName;
    private String groups;
    private Integer course;
    private Integer semester;
//    private Integer studentCount;
//    private Integer groupCount;
    private Integer ects;
    private Integer totalHours;
    private Integer lectureHours;
    private Integer labHours;
    private Integer practiceHours;
    private String individualTask;
    private Boolean hasCredit;
    private Boolean hasExam;
}
