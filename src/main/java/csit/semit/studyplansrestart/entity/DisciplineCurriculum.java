package csit.semit.studyplansrestart.entity;

import jakarta.persistence.*;
import java.util.LinkedList;
import java.util.List;
import lombok.*;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "academ_group_id")
  private AcademGroup academGroup;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "specialized_discipline_package_id")
  private SpecializedDisciplinesPackage specializedDisciplinesPackage;

  @ToString.Exclude
  @OneToMany(mappedBy = "disciplineCurriculum", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  List<HoursDiscSemester> semesters = new LinkedList<>();
}
