package  csit.semit.studyplansrestart.entity;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
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
	private Specialities specialty;

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
