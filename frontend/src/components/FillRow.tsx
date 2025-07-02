import { TableCell, TableRow, TextField } from '@mui/material'
import { styled } from '@mui/material/styles'
import React, { Fragment, useEffect, useState, useRef } from 'react'
import {
	disciplineCurriculumInterface,
	disciplineInterface,
	planRowInterface,
	semesterInterface,
} from '../helper/GlobalVariable'

interface FillRowProps {
	plan: planRowInterface
	semesterCount: number
	onUpdate?: (updatedPlan: planRowInterface) => void
	isEditable?: boolean
}

interface EditableCellProps {
	value: string | number
	onSave: (value: string | number) => void
	editable?: boolean
	numeric?: boolean
	columnIndex?: number
}

// Стилизованные компоненты
const StyledTableCell = styled(TableCell)({
	textAlign: 'center',
	padding: '2px',
	border: '1px solid rgb(11, 1, 1)',
})

const NameCell = styled(TableCell)(({ theme }) => ({
	textAlign: 'left',
	padding: '2px',
	border: '1px solid rgb(11, 1, 1)',
}))


const EditableCell: React.FC<EditableCellProps> = ({
	value,
	onSave,
	editable,
	columnIndex,
}) => {
	const [editValue, setEditValue] = useState(value)
	const [inputWidth, setInputWidth] = useState<number>(0)
	const [isEditing, setIsEditing] = useState(false)

	const spanRef = useRef<HTMLSpanElement>(null)
	const inputRef = useRef<HTMLInputElement>(null)

	useEffect(() => {
		setEditValue(value)
	}, [value])

	useEffect(() => {
		if (spanRef.current) {
			setInputWidth(spanRef.current.offsetWidth)
		}
	}, [editValue])

	useEffect(() => {
		if (isEditing && inputRef.current) {
			inputRef.current.focus()
		}
	}, [isEditing])

	const handleCellClick = () => {
		if (editable) setIsEditing(true)
	}

	const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		setEditValue(e.target.value)
	}

	const handleBlur = () => {
		setIsEditing(false)
		onSave(editValue)
	}

	const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
		if (e.key === 'Enter') {
			setIsEditing(false)
			onSave(editValue)
		}
	}

	if (!editable) {
		return (
			<>
				<span
					ref={spanRef}
					style={{
						position: 'absolute',
						visibility: 'hidden',
						whiteSpace: 'nowrap',
						fontSize: 'inherit',
						fontFamily: 'inherit',
						fontWeight: 'inherit',
					}}
				>
					{value}
				</span>
				{value}
			</>
		)
	}

	if (!isEditing) {
		return (
			<span
				onDoubleClick={handleCellClick}
				style={{ cursor: 'pointer', display: 'block', minHeight: 24 }}
			>
				{value === '' ? '\u00A0' : value}
			</span>
		)
	}

	return (
		<TextField
			value={editValue}
			onChange={handleChange}
			onBlur={handleBlur}
			onKeyDown={handleKeyDown}
			inputRef={inputRef}
			variant='standard'
			fullWidth
			sx={{
				width:
					columnIndex === 1
						? '100%'
						: inputWidth + 10,
				background: 'transparent',
				'& input': {
					padding: 0,
					margin: 0,
					fontSize: 'inherit',
					fontFamily: 'inherit',
					lineHeight: 'inherit',
					height: 'auto',
				},
			}}
			slotProps={{
				input: {
					disableUnderline: true,
				},
			}}
		/>
	)
}

const formatRange = (semesters: number[]): string => {
	if (!semesters.length) return ' '
	if (semesters.length === 1) return semesters[0].toString()
	const sorted = [...semesters].sort((a, b) => a - b)
	if (sorted.length === sorted[sorted.length - 1] - sorted[0] + 1) {
		return `${sorted[0]}-${sorted[sorted.length - 1]}`
	}
	return sorted.join(', ')
}

export default function FillRow({
	plan,
	semesterCount,
	onUpdate,
	isEditable
}: FillRowProps) {
	const [localPlan, setLocalPlan] = useState<planRowInterface>({ ...plan })

	useEffect(() => {
		setLocalPlan({ ...plan })
	}, [plan])

	const allSemesters = Array.from(
		{ length: semesterCount },
		(_, i) => i + 1
	).map(semNum => {
		return (
			localPlan.semesters.find(sem => sem.semester === semNum) || {
				auditHours: '',
				creditsECTS: '',
				hasExam: false,
				hasCredit: false,
			}
		)
	})

	const updateDisciplineField = (
		field: keyof disciplineInterface,
		value: string
	) => {
		const updatedPlan = {
			...localPlan,
			discipline: { ...localPlan.discipline, [field]: value },
		}
		setLocalPlan(updatedPlan)
		if (onUpdate) onUpdate(updatedPlan)
	}

	const updateDisciplineCurriculumField = (
		field: keyof disciplineCurriculumInterface,
		value: string | number
	) => {
		const updatedPlan = {
			...localPlan,
			disciplineCurriculum: {
				...localPlan.disciplineCurriculum,
				[field]: value,
			},
		}
		setLocalPlan(updatedPlan)
		if (onUpdate) onUpdate(updatedPlan)
	}

	const updateSemesterField = (
		index: number,
		field: keyof semesterInterface,
		value: string | number | boolean
	) => {
		const semesterNum = index + 1
		const updatedSemesters = [...localPlan.semesters]

		// Находим индекс семестра в массиве (если он существует)
		const semesterIndex = updatedSemesters.findIndex(
			sem => sem.semester === semesterNum
		)

		// Обновляем существующий семестр
		updatedSemesters[semesterIndex] = {
			...updatedSemesters[semesterIndex],
			[field]: value,
		}

		const updatedPlan = { ...localPlan, semesters: updatedSemesters }
		setLocalPlan(updatedPlan)
		if (onUpdate) onUpdate(updatedPlan)
	}

	return (
		<TableRow>
			{/* Шифр */}
			<StyledTableCell>
				<EditableCell
					value={localPlan.discipline.shortName || ''}
					onSave={value => updateDisciplineField('shortName', value as string)}
					editable={isEditable}
					columnIndex={0}
				/>
			</StyledTableCell>

			{/* Название дисциплины */}
			<NameCell>
				<EditableCell
					value={localPlan.discipline.name || ''}
					onSave={value => updateDisciplineField('name', value as string)}
					editable={isEditable}
					columnIndex={1}
				/>
			</NameCell>

			{/* Экзамены */}
			<StyledTableCell>
				{formatRange(
					localPlan.semesters
						.filter(sem => sem.hasExam)
						.map(sem => sem.semester)
						.filter((semester): semester is number => semester !== undefined)
				)}
			</StyledTableCell>

			{/* Зачеты */}
			<StyledTableCell>
				{formatRange(
					localPlan.semesters
						.filter(sem => sem.hasCredit)
						.map(sem => sem.semester)
						.filter((semester): semester is number => semester !== undefined)
				)}
			</StyledTableCell>

			{/* Индивидуальные задания */}
			<StyledTableCell>
				<EditableCell
					value={localPlan.disciplineCurriculum?.individualTaskType || ' '}
					onSave={value =>
						updateDisciplineCurriculumField(
							'individualTaskType',
							value as string
						)
					}
					editable={isEditable}
				/>
			</StyledTableCell>

			{/* Часы */}
			<StyledTableCell>
				<EditableCell
					value={localPlan.disciplineCurriculum?.lecHours || 0}
					onSave={value =>
						updateDisciplineCurriculumField('lecHours', value as number)
					}
					numeric={true}
					editable={isEditable}
				/>
			</StyledTableCell>

			<StyledTableCell>
				<EditableCell
					value={localPlan.disciplineCurriculum?.labHours || 0}
					onSave={value =>
						updateDisciplineCurriculumField('labHours', value as number)
					}
					numeric={true}
					editable={isEditable}
				/>
			</StyledTableCell>

			<StyledTableCell>
				<EditableCell
					value={localPlan.disciplineCurriculum?.practiceHours || 0}
					onSave={value =>
						updateDisciplineCurriculumField('practiceHours', value as number)
					}
					numeric={true}
					editable={isEditable}
				/>
			</StyledTableCell>

			{/* Семестры */}
			{allSemesters.map((semester, index) => (
				<Fragment key={index}>
					<StyledTableCell>
						<EditableCell
							value={semester.auditHours}
							onSave={value => updateSemesterField(index, 'auditHours', value)}
							numeric={true}
							editable={isEditable}
						/>
					</StyledTableCell>

					<StyledTableCell>
						<EditableCell
							value={semester.creditsECTS}
							onSave={value => updateSemesterField(index, 'creditsECTS', value)}
							numeric={true}
							editable={isEditable}
						/>
					</StyledTableCell>
				</Fragment>
			))}
		</TableRow>
	)
}
