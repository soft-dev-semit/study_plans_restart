package csit.semit.studyplansrestart.dto.returnData;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SpecializedDisciplinesPackageDTO {
  private long id;
  private String nameOfPackage;
  private String indexOfDiscipline;
}
