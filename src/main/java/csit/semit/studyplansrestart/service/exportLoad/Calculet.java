package csit.semit.studyplansrestart.service.exportLoad;

import csit.semit.studyplansrestart.dto.returnData.CourseInfo;
import csit.semit.studyplansrestart.entity.AcademGroup;
import csit.semit.studyplansrestart.entity.HoursDiscSemester;
import csit.semit.studyplansrestart.repository.GroupRepository;
import csit.semit.studyplansrestart.repository.SemesterRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class Calculet {
    @NonNull
    private final SemesterRepository semesterRepository;
//    @NonNull
//    private final DisciplineRepository disciplineRepository;
    @NonNull
    private final GroupRepository groupRepository;
    private final Map<Long, Integer> courseMap = new HashMap<>();
    private final Map<Long, String> groupMap = new HashMap<>();
    private final Map<String, List<CourseInfo>> result = new HashMap<>();
//    private final List<Long> disciplineListId = new ArrayList<>();

    private void fillGlobal() {
//        getDiscipline();
        getGroup();
    }

//    public void getDiscipline() {
//        List<Discipline> disciplineList = disciplineRepository.findAll();
//        for (Discipline discipline : disciplineList) {
//            disciplineListId.add(discipline.getId());
//        }
//    }

    public void getGroup() {
        List<AcademGroup> groupList = groupRepository.findAll();
        int maxYear = groupList.stream().max(Comparator.comparing(AcademGroup::getYear)).get().getYear();
        for (AcademGroup academGroup : groupList) {
            if (academGroup.getName().startsWith("КН-M") || academGroup.getName().startsWith("КН-Н")) {
                int course = maxYear - academGroup.getYear() + 4;
                courseMap.put(academGroup.getCurriculum().getId(), course);
                groupMap.put(academGroup.getCurriculum().getId(), academGroup.getName());
                continue;
            }
            if (maxYear == academGroup.getYear()) {
                courseMap.put(academGroup.getCurriculum().getId(), 1);
                groupMap.put(academGroup.getCurriculum().getId(), academGroup.getName());
                continue;
            }
            if (maxYear > academGroup.getYear()) {
                int course = maxYear - academGroup.getYear() + 1;
                courseMap.put(academGroup.getCurriculum().getId(), course);
                groupMap.put(academGroup.getCurriculum().getId(), academGroup.getName());
            }
        }
    }

    public Map<String, List<CourseInfo>> getCoursesBySemester() {
        fillGlobal();
        List<CourseInfo> autumnCourses = new ArrayList<>();
        List<CourseInfo> springCourses = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : courseMap.entrySet()) {
            Long curriculumId = entry.getKey();
            Integer course = entry.getValue();
            String groupName = groupMap.get(curriculumId);
            List<HoursDiscSemester> semesters = semesterRepository.findAll();
            if(semesters.isEmpty()) {
                return null;
            }
            for (HoursDiscSemester semester : semesters) {
                if (semester.getDisciplineCurriculum().getCurriculum().getId().equals(curriculumId) && (semester.getSemester() + 1) / 2 == course) {
                    CourseInfo courseInfo = new CourseInfo(
                            semester.getDisciplineCurriculum().getDiscipline().getName(),
                            semester.getDisciplineCurriculum().getDiscipline().getShortName(),
                            groupName,
                            course,
                            semester.getSemester(),
//                            getStudentCount(curriculumId), // метод для подсчета студентов
//                            getGroupCount(curriculumId),   // метод для подсчета групп
                            semester.getCreditsECTS(),
                            calculateTotalHours(semester),
                            semester.getDisciplineCurriculum().getLecHours(),
                            semester.getDisciplineCurriculum().getLabHours(),
                            semester.getDisciplineCurriculum().getPracticeHours(),
                            semester.getDisciplineCurriculum().getIndividualTaskType(),
                            semester.isHasCredit(),
                            semester.isHasExam()
                    );

                    if (semester.getSemester() % 2 == 1) {
                        autumnCourses.add(courseInfo);
                    } else {
                        springCourses.add(courseInfo);
                    }
                }
            }
        }

        Map<String, CourseInfo> uniqueAutumnCourses = mergeCoursesWithSameProperties(autumnCourses);
        Map<String, CourseInfo> uniqueSpringCourses = mergeCoursesWithSameProperties(springCourses);

        List<CourseInfo> mergedAutumnCourses = new ArrayList<>(uniqueAutumnCourses.values());
        List<CourseInfo> mergedSpringCourses = new ArrayList<>(uniqueSpringCourses.values());

        mergedSpringCourses.sort(Comparator.comparing(CourseInfo::getCourse).thenComparing(CourseInfo::getShortName));
        mergedAutumnCourses.sort(Comparator.comparing(CourseInfo::getCourse).thenComparing(CourseInfo::getShortName));

        result.put("autumn", mergedAutumnCourses);
        result.put("spring", mergedSpringCourses);
        return result;
    }

    private Map<String, CourseInfo> mergeCoursesWithSameProperties(List<CourseInfo> courses) {
        Map<String, CourseInfo> uniqueCourses = new HashMap<>();

        for (CourseInfo course : courses) {
            String key = course.getName() + "#" +
                    course.getShortName() + "#" +
                    course.getCourse() + "#" +
                    course.getSemester() + "#" +
                    course.getEcts() + "#" +
                    course.getTotalHours() + "#" +
                    course.getLectureHours() + "#" +
                    course.getLabHours() + "#" +
                    course.getPracticeHours() + "#" +
                    course.getIndividualTask() + "#" +
                    course.getHasCredit() + "#" +
                    course.getHasExam();

            if (uniqueCourses.containsKey(key)) {
                CourseInfo existingCourse = uniqueCourses.get(key);

                Set<String> groups = new HashSet<>();

                if (existingCourse.getGroups() != null && !existingCourse.getGroups().isEmpty()) {
                    groups.addAll(Arrays.asList(existingCourse.getGroups().split(", ")));
                }

                if (course.getGroups() != null && !course.getGroups().isEmpty()) {
                    groups.addAll(Arrays.asList(course.getGroups().split(", ")));
                }

                String mergedGroups = String.join(", ", groups);
                existingCourse.setGroups(mergedGroups);
            } else {
                uniqueCourses.put(key, course);
            }
        }

        return uniqueCourses;
    }

//    private String getGroupsString(Long curriculumId) {
//        // Реализация получения строки с группами
//        return groupRepository.findByCurriculumId(curriculumId)
//                .stream()
//                .map(AcademGroup::getName)
//                .collect(Collectors.joining(", "));
//    }
//
//    private Integer getStudentCount(Long curriculumId) {
//        // Реализация подсчета количества студентов
//        return groupRepository.findByCurriculumId(curriculumId)
//                .stream()
//                .mapToInt(AcademGroup::getStudentCount)
//                .sum();
//    }
//
//    private Integer getGroupCount(Long curriculumId) {
//        // Реализация подсчета количества групп
//        return groupRepository.findByCurriculumId(curriculumId).size();
//    }

    private Integer calculateTotalHours(HoursDiscSemester semester) {
        return semester.getDisciplineCurriculum().getLecHours() +
                semester.getDisciplineCurriculum().getLabHours() +
                semester.getDisciplineCurriculum().getPracticeHours();
    }
}