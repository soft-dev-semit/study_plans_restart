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
@ToString
@Table(name = "disciplines")
public class Discipline {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(length = 10, nullable = false)
	private String short_name;

	@ToString.Exclude
	@OneToMany(mappedBy = "discipline",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<DisciplineCurriculum> disciplineCurricula = new LinkedList<>();
}
