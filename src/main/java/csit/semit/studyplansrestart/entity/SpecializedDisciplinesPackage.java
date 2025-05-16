package csit.semit.studyplansrestart.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SpecializedDisciplinesPackage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nameOfPackage;
  private String indexOfDiscipline;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "specializedDisciplinesPackage")
  List<DisciplineCurriculum> disciplineCurricula = new ArrayList<>();
}
