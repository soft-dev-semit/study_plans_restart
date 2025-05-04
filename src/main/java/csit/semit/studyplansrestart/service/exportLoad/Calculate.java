package csit.semit.studyplansrestart.service.exportLoad;

import csit.semit.studyplansrestart.controller.ImportController;
import csit.semit.studyplansrestart.dto.returnData.CourseInfo;
import csit.semit.studyplansrestart.entity.AcademGroup;
import csit.semit.studyplansrestart.entity.HoursDiscSemester;
import csit.semit.studyplansrestart.repository.GroupRepository;
import csit.semit.studyplansrestart.repository.SemesterRepository;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Calculate {
  @Autowired private SemesterRepository semesterRepository;
  @Autowired private GroupRepository groupRepository;
  private final Map<Long, Integer> courseMap = new HashMap<>();
  private final Map<Long, String> groupMap = new HashMap<>();
  private static final Logger logger = LoggerFactory.getLogger(ImportController.class);

  public void getGroup() {
    List<AcademGroup> groupList = groupRepository.findAll();

    int maxYear = groupList.stream().mapToInt(AcademGroup::getYear).max().orElse(0);

    Pattern pattern = Pattern.compile("([A-ZА-Яа-яІіЇї]{2}-[МНмнMNmn]\\d{3})");

    courseMap.clear();
    groupMap.clear();

    for (AcademGroup academGroup : groupList) {
      Long curriculumId = academGroup.getCurriculum().getId();
      String groupName = academGroup.getName();
      int year = academGroup.getYear();
      boolean matches = pattern.matcher(groupName).matches();

      int course;
      if (maxYear == year && !matches) {
        course = 1;
      } else if (matches) {
        course = maxYear - year + 5;
      } else {
        course = maxYear - year + 1;
      }

      courseMap.put(curriculumId, course);
      groupMap.put(curriculumId, groupName);
    }
  }

  public List<CourseInfo> getCoursesBySemester(String season) {
    getGroup();

    if (courseMap.isEmpty()) {
      return Collections.emptyList();
    }

    List<HoursDiscSemester> allSemesters = semesterRepository.findAll();
    if (allSemesters.isEmpty()) {
      return Collections.emptyList();
    }

    List<CourseInfo> courses = new ArrayList<>(courseMap.size() * 2);

    boolean isAutumn = "autumn".equals(season);

    for (Map.Entry<Long, Integer> entry : courseMap.entrySet()) {
      Long curriculumId = entry.getKey();
      Integer course = entry.getValue();
      String groupName = groupMap.get(curriculumId);

      for (HoursDiscSemester semester : allSemesters) {
        if (!semester.getDisciplineCurriculum().getCurriculum().getId().equals(curriculumId)) {
          continue;
        }
        boolean semesterByCourse =
            course >= 5
                ? (semester.getSemester() + 1) / 2 == course - 4
                : (semester.getSemester() + 1) / 2 == course;
        if (!semesterByCourse) {
          continue;
        }

        boolean isSemesterOdd = semester.getSemester() % 2 == 1;
        if ((isAutumn && isSemesterOdd) || (!isAutumn && !isSemesterOdd)) {
          courses.add(fillCourseInfo(semester, course, groupName));
        }
      }
    }

    Map<String, CourseInfo> uniqueCourses = mergeCoursesWithSameProperties(courses);

    return uniqueCourses.values().stream()
        .sorted(Comparator.comparing(CourseInfo::getCourse).thenComparing(CourseInfo::getShortName))
        .collect(Collectors.toList());
  }

  private Map<String, CourseInfo> mergeCoursesWithSameProperties(List<CourseInfo> courses) {
    Map<String, CourseInfo> uniqueCourses = new HashMap<>(courses.size());
    StringBuilder keyBuilder = new StringBuilder(200);

    for (CourseInfo course : courses) {
      keyBuilder.setLength(0);
      keyBuilder
          .append(course.getName())
          .append("#")
          .append(course.getShortName())
          .append("#")
          .append(course.getCourse())
          .append("#")
          .append(course.getSemester())
          .append("#")
          .append(course.getEcts())
          .append("#")
          .append(course.getTotalHours())
          .append("#")
          .append(course.getLectureHours())
          .append("#")
          .append(course.getLabHours())
          .append("#")
          .append(course.getPracticeHours())
          .append("#")
          .append(course.getIndividualTask())
          .append("#")
          .append(course.getHasCredit())
          .append("#")
          .append(course.getHasExam());

      String key = keyBuilder.toString();

      CourseInfo existingCourse = uniqueCourses.get(key);
      if (existingCourse != null) {
        String existingGroups = existingCourse.getGroups();
        String newGroups = course.getGroups();

        Set<String> mergedGroups = new TreeSet<>();

        if (existingGroups != null && !existingGroups.isEmpty()) {
          Collections.addAll(mergedGroups, existingGroups.split(", "));
        }
        if (newGroups != null && !newGroups.isEmpty()) {
          Collections.addAll(mergedGroups, newGroups.split(", "));
        }

        existingCourse.setGroups(String.join(", ", mergedGroups));
      } else {
        uniqueCourses.put(key, course);
      }
    }
    return uniqueCourses;
  }

  private CourseInfo fillCourseInfo(HoursDiscSemester semester, int course, String groupName) {
    return new CourseInfo(
        semester.getDisciplineCurriculum().getDiscipline().getName(),
        semester.getDisciplineCurriculum().getDiscipline().getShortName(),
        groupName,
        course,
        semester.getSemester(),
        semester.getCreditsECTS(),
        calculateTotalHours(semester),
        semester.getDisciplineCurriculum().getLecHours(),
        semester.getDisciplineCurriculum().getLabHours(),
        semester.getDisciplineCurriculum().getPracticeHours(),
        semester.getDisciplineCurriculum().getIndividualTaskType(),
        semester.isHasCredit(),
        semester.isHasExam());
  }

  private Integer calculateTotalHours(HoursDiscSemester semester) {
    return semester.getDisciplineCurriculum().getLecHours()
        + semester.getDisciplineCurriculum().getLabHours()
        + semester.getDisciplineCurriculum().getPracticeHours();
  }
}
