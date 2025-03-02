package csit.semit.studyplansrestart.dto.returnData;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurriculumDTO {
    public long id;
    public String file_url;
    public String approvementURL;
    public int year;
    public long specialty_id;
    public long department_id;
}