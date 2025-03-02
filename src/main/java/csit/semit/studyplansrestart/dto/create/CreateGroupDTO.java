package csit.semit.studyplansrestart.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateGroupDTO {
    public String name;
    public int year;
    public String language;
    public Long department_id;
    public Long faculty_id;
    public Long curriculum_id;
}
