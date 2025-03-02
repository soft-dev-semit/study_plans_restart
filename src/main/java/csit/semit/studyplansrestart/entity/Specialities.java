package  csit.semit.studyplansrestart.entity;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "specialities")
public class Specialities {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 10)
	private String code;
	@Column(nullable = false)
	private String code_name;
	private String name;
	private int number;

	@ToString.Exclude
	@OneToMany(mappedBy = "specialty",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	List<Curriculum> curriculumList = new LinkedList<>();

}
