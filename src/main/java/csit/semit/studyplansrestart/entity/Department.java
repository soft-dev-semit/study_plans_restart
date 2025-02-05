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
@Table(name = "departments")
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	@Column(length = 10)
//	private String code;

	@Column(length = 100, nullable = false)
	private String name;

	@Column(length = 6, nullable = false)
	private String short_name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "faculty_id")
	private Faculty faculty;

	@ToString.Exclude
	@OneToMany(mappedBy = "department",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<Curriculum> curriculumList = new LinkedList<>();
}
