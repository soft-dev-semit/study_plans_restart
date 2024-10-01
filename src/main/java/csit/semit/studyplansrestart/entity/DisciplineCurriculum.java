package  csit.semit.studyplansrestart.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DisciplineCurriculum {

	private Discipline discipline;
	private Curriculum curriculum;
	private int hours;
	private int auditHours;
	private int labHours;
	private int lecHours;
	private int practiceHours;
	private int independentWorkHours;
	private int credits;
	private boolean hasExam;
	private boolean hasCredit;
	private String individualTaskType;
	private int semester;
	private String block;
	private String fileURL;


}
