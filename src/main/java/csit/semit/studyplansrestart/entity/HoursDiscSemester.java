package csit.semit.studyplansrestart.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "semesters")
public class HoursDiscSemester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int auditHours;
    private int creditsECTS;
    private boolean hasExam;
    private boolean hasCredit;
    private int semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discipline_curriculum_id")
    private DisciplineCurriculum disciplineCurriculum;

    @Override
    public String toString() {
        return "Semester{" +
                "id=" + id +
                ", auditHours=" + auditHours +
                ", creditsECTS=" + creditsECTS +
                ", hasExam=" + hasExam +
                ", hasCredit=" + hasCredit +
                ", semester=" + semester +
                '}';
    }
}
