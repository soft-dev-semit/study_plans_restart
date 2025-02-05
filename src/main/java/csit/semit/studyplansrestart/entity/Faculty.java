package csit.semit.studyplansrestart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "faculties")
public class Faculty {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 10)
	private String code;
	@Column(nullable = false)
	private String name;

	@OneToMany(mappedBy = "faculty",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	List<Department> departments = new LinkedList<>();

	@OneToMany(mappedBy = "faculty",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	List<AcademGroup> group = new LinkedList<>();
}
