import { TableRow, TableCell } from '@mui/material'
import { styled } from '@mui/material/styles'
import { Fragment } from 'react/jsx-runtime'

interface Semester {
	id: number
	auditHours: number
	creditsECTS: number
	hasExam: boolean
	hasCredit: boolean
	semester: number
}

interface Discipline {
	id: number
	name: string
	shortName: string
}

interface Plan {
	id: number
	labHours: number
	lecHours: number
	practiceHours: number
	individualTaskType: string
	fileURL: string
	discipline: Discipline
	semesters: Semester[]
}

interface FillRowProps {
	plan: Plan
	semesterCount: number
}

const StyledTableCell = styled(TableCell)({
	textAlign: 'center',
	padding: '2px',
	border: '1px solid rgb(11, 1, 1)',
})

const formatSemesterRange = (semesters: number[]): string => {
	if (!semesters.length) return ' '
	if (semesters.length === 1) return semesters[0].toString()
	
	const sorted = [...semesters].sort((a, b) => a - b)
	if (sorted.length === sorted[sorted.length - 1] - sorted[0] + 1) {
		return `${sorted[0]}-${sorted[sorted.length - 1]}`
	}
	return ('')
}

export default function FillRow({ plan, semesterCount }: FillRowProps) {
	const allSemesters = Array.from(
		{ length: semesterCount }, 
		(_, i) => i + 1
	).map(semNum => {
		return plan.semesters.find(sem => sem.semester === semNum) || {
			auditHours: ' ',
			creditsECTS: ' ',
			hasExam: false,
			hasCredit: false,
		}
	})

	return (
		<TableRow>
			{/* Шифр */}
			<StyledTableCell>{plan.discipline.shortName}</StyledTableCell>

			{/* Название дисциплины */}
			<StyledTableCell>{plan.discipline.name}</StyledTableCell>

			{/* Экзамены */}
			<StyledTableCell>
				{formatSemesterRange(
					allSemesters
						.filter(sem => sem.hasExam)
						.map((sem, i) => i + 1)
				)}
			</StyledTableCell>

			{/* Зачеты */}
			<StyledTableCell>
				{formatSemesterRange(
					allSemesters
						.filter(sem => sem.hasCredit)
						.map((sem, i) => i + 1)
				)}
			</StyledTableCell>

			{/* Индивидуальные задания */}
			<StyledTableCell>{plan.individualTaskType || ' '}</StyledTableCell>

			{/* Часы */}
			<StyledTableCell>{plan.lecHours}</StyledTableCell>
			<StyledTableCell>{plan.labHours}</StyledTableCell>
			<StyledTableCell>{plan.practiceHours}</StyledTableCell>

			{/* Семестры */}
			{allSemesters.map((semester, index) => (
				<Fragment key={index}>
					<StyledTableCell>{semester.auditHours}</StyledTableCell>
					<StyledTableCell>{semester.creditsECTS}</StyledTableCell>
				</Fragment>
			))}
		</TableRow>
	)
}
