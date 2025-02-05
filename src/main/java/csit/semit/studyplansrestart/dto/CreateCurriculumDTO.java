package csit.semit.studyplansrestart.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCurriculumDTO {
    public String file_url;
    public String approvementURL;
    public int year;
    public long specialty_id;
    public long department_id;
}
