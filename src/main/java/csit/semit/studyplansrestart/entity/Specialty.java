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
@Table(name = "specialities")
public class Specialty {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 10)
	private String code;
	@Column(nullable = false)
	private String name;

	@ToString.Exclude
	@OneToMany(mappedBy = "specialty",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	List<Curriculum> curriculumList = new LinkedList<>();

}
