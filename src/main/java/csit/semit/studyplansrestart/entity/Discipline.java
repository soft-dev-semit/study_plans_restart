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
@Table(name = "disciplines")
public class Discipline {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(length = 10, nullable = false, name = "short_name")
	private String shortName;

	@ToString.Exclude
	@OneToMany(mappedBy = "discipline",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<DisciplineCurriculum> disciplineCurricula = new LinkedList<>();

	@Override
	public String toString() {
		return "Discipline{" +
				"id=" + id +
				", name='" + name + '\'' +
				", shortName='" + shortName + '\'' +
				'}';
	}
}
