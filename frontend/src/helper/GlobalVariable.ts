
interface Semester {
	id: number
	auditHours: number | string
	creditsECTS: number | string
	hasExam: boolean
	hasCredit: boolean
	semester?: number
}

interface Discipline {
	id: number
	name: string
	shortName: string
}

interface DisciplineCurriculum {
	id: number
	labHours: number
	lecHours: number
	practiceHours: number
	individualTaskType: string
}

interface PlanRow {
	id: number
	discipline: Discipline
	disciplineCurriculum: DisciplineCurriculum
	semesters: Semester[]
}

interface CreateNewPlanFromTemplateProps {
	curriculum_id: number
	package_id: number
	suffix: string
}

export type planRowInterface = PlanRow
export type semesterInterface = Semester
export type disciplineInterface = Discipline
export type disciplineCurriculumInterface = DisciplineCurriculum
export type createNewPlanFromTemplatePropsInterface = CreateNewPlanFromTemplateProps;

