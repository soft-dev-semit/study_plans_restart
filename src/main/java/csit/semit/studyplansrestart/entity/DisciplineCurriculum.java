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
@Table(name = "discipline_curriculums")
public class DisciplineCurriculum {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int labHours;
	private int lecHours;
	private int practiceHours;
	private String individualTaskType;
	private String fileURL;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "discipline_id")
	private Discipline discipline;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curriculum_id")
	private Curriculum curriculum;

	@ToString.Exclude
	@OneToMany(mappedBy = "disciplineCurriculum", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<Semester> semesters = new LinkedList<>();
}
