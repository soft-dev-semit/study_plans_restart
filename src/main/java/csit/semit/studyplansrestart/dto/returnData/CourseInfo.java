package csit.semit.studyplansrestart.dto.returnData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseInfo {
    private String name;            // Назва учбової дисципліни
    private String shortName;       // Скор
    private String groups;          // Групи
    private Integer course;         // Курс
    private Integer semester;       // Сем
//    private Integer studentCount;   // Кільк студ
//    private Integer groupCount;     // Кільк груп
    private Integer ects;           // ECTS
    private Integer totalHours;    // Всього
    private Integer lectureHours;  // Лек
    private Integer labHours;      // Лаб
    private Integer practiceHours; // Прак
    private String individualTask; // Інд зав
    private Boolean hasCredit;    // Зал
    private Boolean hasExam;      // Екз
}
