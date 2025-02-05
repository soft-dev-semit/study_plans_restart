package  csit.semit.studyplansrestart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "curriculums")
public class Curriculum {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	private String name;
	private int year;
	private String file_url;
	private String approvementURL;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "specialty_id")
	private Specialty specialty;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;

	@ToString.Exclude
	@OneToMany(mappedBy = "curriculum",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<DisciplineCurriculum> disciplineCurricula = new LinkedList<>();

	@ToString.Exclude
	@OneToMany(mappedBy = "curriculum",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<AcademGroup> academGroups = new LinkedList<>();
}
