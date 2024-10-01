package  csit.semit.studyplansrestart.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AcademGroup {

	private Long id;
	private String code;
	private String name;
	private int year;
	private Department department;
	private Curriculum curriculum;
	private Specialty specialty;


}
