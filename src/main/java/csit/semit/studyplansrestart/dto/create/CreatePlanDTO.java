package csit.semit.studyplansrestart.dto.create;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePlanDTO {
  private long curriculum_id;
  private long package_id;
  private String suffix;
}
