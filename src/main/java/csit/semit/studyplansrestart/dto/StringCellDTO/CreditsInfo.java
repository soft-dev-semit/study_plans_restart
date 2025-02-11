package csit.semit.studyplansrestart.dto.StringCellDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreditsInfo {
    private int firstCredit = 0;
    private int secondCredit = 0;
    private boolean hasCredits = false;
}
