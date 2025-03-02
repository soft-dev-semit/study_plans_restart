package csit.semit.studyplansrestart.dto.StringCellDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetExamOrCreditsCellDTO {
    private int first = 0;
    private int second = 0;
    private boolean has = false;
}