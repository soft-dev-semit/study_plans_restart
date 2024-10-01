package  csit.semit.studyplansrestart.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Discipline {

	private Long id;
	private String code;
	private String name;
	private String short_name;

}
