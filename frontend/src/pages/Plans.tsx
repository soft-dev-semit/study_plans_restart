import Box from '@mui/material/Box'
import Paper from '@mui/material/Paper'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableContainer from '@mui/material/TableContainer'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import { styled } from '@mui/material/styles'
import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Requests } from '../api/Requests'
import CurriculumList from '../components/CurriculumList'
import FillRow from '../components/FillRow'
import Button from '@mui/material/Button'
import Stack from '@mui/material/Stack'
import EditIcon from '@mui/icons-material/Edit'
import SaveIcon from '@mui/icons-material/Save'
import CancelIcon from '@mui/icons-material/Cancel'
import { planRowInterface } from '../helper/GlobalVariable' // Make sure to import this interface
import PopupDialog from '../components/PopupDialog'
import ButtonDialog from '../helper/ButtonDialog'

const RotatedCell = styled(TableCell)(({ theme }) => ({
	textAlign: 'center',
	padding: '2px',
	border: '1px solid rgb(11, 1, 1)',
	backgroundColor: 'white',
	'& .rotated-text': {
		writingMode: 'vertical-rl',
		transform: 'rotate(180deg)',
		whiteSpace: 'nowrap',
		textAlign: 'center',
		minHeight: '100px',
		minWidth: '30px',
	},
}))

const CenteredCell = styled(TableCell)(({ theme }) => ({
	textAlign: 'center',
	padding: '2px',
	fontWeight: 'bold',
	verticalAlign: 'middle',
	border: '1px solid rgb(11, 1, 1)',
	backgroundColor: 'white',
	height: '30px',
}))

interface Curriculum {
	id: number
	name: string
	template: boolean
}

export default function Plans() {
	const [plans, setPlans] = useState<planRowInterface[]>([])
	const [curriculums, setCurriculums] = useState<Curriculum[]>([])
	const [semesterCount, setSemesterCount] = useState(8)
	const [courseCount, setCourseCount] = useState(4)
	const { curriculumId } = useParams()
	const [isEditable, setIsEditable] = useState(false)
	const [isLoading, setIsLoading] = useState(false)
	const [hasChanges, setHasChanges] = useState(false)
	const [changedDisciplineIds, setChangedDisciplineIds] = useState<number[]>([])
	const [changedDisciplineCurriculumIds, setChangedDisciplineCurriculumIds] = useState<number[]>([])
	const [changedSemesterIds, setChangedSemesterIds] = useState<number[]>([])
	const [showSuccessPopup, setShowSuccessPopup] = useState(false)
	const [showErrorPopup, setShowErrorPopup] = useState(false)
	const [errorMessage, setErrorMessage] = useState('')
	const [showCreatePlanDialog, setShowCreatePlanDialog] = useState(false)

	useEffect(() => {
		const fetchCurriculums = async () => {
			try {
				const [respTemplate, respGroup] = await Promise.all([
					Requests.getAllCurriculums(),
					Requests.getAllGroup()
				])
				const combinedCurriculums = [
					...(Array.isArray(respTemplate) ? respTemplate : []),
					...(Array.isArray(respGroup) ? respGroup : [])
				]
				setCurriculums(combinedCurriculums)
			} catch (error) {
				console.error('Error fetching curriculums:', error)
				setCurriculums([])
			}
		}
		fetchCurriculums()
	}, [])

	useEffect(() => {
		if (curriculumId) {
			const currentCurriculum = curriculums.find(
				c => c.id === Number(curriculumId)
			)

			if (currentCurriculum?.name.match(/[сС]$/)) {
				setSemesterCount(6)
				setCourseCount(3)
			}
			if (
				currentCurriculum?.name.match(/[мМ]/) 
				// || currentCurriculum?.name.match(/-[нН]/)
			) {
				setSemesterCount(4)
				setCourseCount(2)
			}
		}
	}, [curriculumId, curriculums])

	const dynamicCourseHeaders = Array.from({ length: courseCount }, (_, i) => ({
		id: i + 1,
		label: `${
			i + 1 === 1 ? 'I' : i + 1 === 2 ? 'II' : i + 1 === 3 ? 'III' : 'IV'
		} курс`,
		colSpan: 4,
	}))

	const dynamicSemesterHeaders = Array.from(
		{ length: semesterCount },
		(_, i) => i + 1
	)

	const dynamicWeeksInSemester = Array(semesterCount).fill(20)

	useEffect(() => {
		const fetchData = async () => {
			if (curriculumId) {
				try {
					setIsLoading(true)
					const resp = await Requests.getPlans(Number(curriculumId))
					setPlans(Array.isArray(resp) ? resp : [])
					setHasChanges(false)
				} catch (error) {
					console.error('something wrong' + error)
					setPlans([])
				} finally {
					setIsLoading(false)
				}
			}
		}
		fetchData()
	}, [curriculumId])

	// Handle toggling edit mode
	const handleToggleEdit = () => {
		setIsEditable(prev => !prev)
	}

	// Handle canceling edit mode
	const handleCancelEdit = async () => {
		// Reload the original data
		if (curriculumId) {
			try {
				setIsLoading(true)
				const resp = await Requests.getPlans(Number(curriculumId))
				setPlans(Array.isArray(resp) ? resp : [])
				setIsEditable(false)
				setHasChanges(false)
			} catch (error) {
				console.error('Error reloading data: ' + error)
			} finally {
				setIsLoading(false)
			}
		}
	}

	// Handle saving all changes
	const handleSaveChanges = async () => {
		if (curriculumId && hasChanges) {
			try {
				setIsLoading(true)

				const disciplines = plans
					.map(plan => plan.discipline)
					.filter(d => changedDisciplineIds.includes(d.id))
				console.log(changedDisciplineIds)
				const disciplineCurriculums = plans
					.map(plan => plan.disciplineCurriculum)
					.filter(dc => changedDisciplineCurriculumIds.includes(dc.id))
				console.log(changedDisciplineCurriculumIds)
				const semesters = plans
					.flatMap(plan => plan.semesters)
					.filter(s => changedSemesterIds.includes(s.id))
					.filter(s =>
						(String(s.auditHours).trim() !== '') ||
						(String(s.creditsECTS).trim() !== '')
					)
				console.log(changedSemesterIds)
				await Promise.all([
					Requests.updateDiscipline(disciplines),
					Requests.updateDisciplineCurriculum(disciplineCurriculums),
					Requests.updateSemesters(semesters)
				])

				setIsEditable(false)
				setHasChanges(false)
				setChangedDisciplineIds([])
				setChangedDisciplineCurriculumIds([])
				setChangedSemesterIds([])
				setShowSuccessPopup(true)
			} catch (error) {
				console.error('Error saving plans: ' + error)
				setErrorMessage('Помилка при збереженні змін: ' + error)
				setShowErrorPopup(true)
			} finally {
				setIsLoading(false)
			}
		}
	}

	// Handle updating a single plan row
	const handleUpdatePlan = (updatedPlan: planRowInterface) => {
		setPlans(prevPlans => {
			const newPlans = prevPlans.map(plan =>
				plan.id === updatedPlan.id ? updatedPlan : plan
			)
			setChangedDisciplineIds(ids => ids.includes(updatedPlan.discipline.id) ? ids : [...ids, updatedPlan.discipline.id])
			setChangedDisciplineCurriculumIds(ids => ids.includes(updatedPlan.disciplineCurriculum.id) ? ids : [...ids, updatedPlan.disciplineCurriculum.id])
			updatedPlan.semesters.forEach(sem =>
				setChangedSemesterIds(ids => ids.includes(sem.id) ? ids : [...ids, sem.id])
			)
			setHasChanges(true)
			return newPlans
		})
	}

	return (
		<Box sx={{ display: 'flex', height: '100%', flexDirection: 'column' }}>
			<Box sx={{ display: 'flex', flexGrow: 1, overflow: 'hidden' }}>
				<CurriculumList curriculums={curriculums} selectedId={curriculumId} />
				<Paper
					sx={{
						flexGrow: 1,
						overflow: 'hidden',
						display: 'flex',
						flexDirection: 'column',
					}}
				>
					{curriculumId && (
						<Box sx={{ padding: 1, borderBottom: '1px solid #e0e0e0' }}>
							<Stack direction='row' spacing={2} justifyContent='flex-end'>
								<Button
									variant='contained'
									onClick={() => setShowCreatePlanDialog(true)}
									disabled={isLoading}
								>
									Створити план для группи
								</Button>
								<Button
									variant='contained'
									onClick={handleToggleEdit}
									disabled={isLoading}
								>
									Прикрепити к групе
								</Button>
								{!isEditable ? (
									<Button
										variant='contained'
										startIcon={<EditIcon />}
										onClick={handleToggleEdit}
										disabled={isLoading}
									>
										Редагувати
									</Button>
								) : (
									<>
										<Button
											variant='contained'
											color='success'
											startIcon={<SaveIcon />}
											onClick={handleSaveChanges}
											disabled={isLoading || !hasChanges}
										>
											Зберегти
										</Button>
										<Button
											variant='outlined'
											color='error'
											startIcon={<CancelIcon />}
											onClick={handleCancelEdit}
											disabled={isLoading}
										>
											Скасувати
										</Button>
									</>
								)}
							</Stack>
						</Box>
					)}
					{curriculumId ? (
						<TableContainer
							sx={{
								flexGrow: 1,
								maxHeight: 'calc(100vh - 48px)', // Subtract the height of the button area
								'& .MuiTableHead-root': {
									position: 'sticky',
									top: 0,
									zIndex: 1,
								},
							}}
						>
							<Table
								stickyHeader
								sx={{
									'& .MuiTableCell-stickyHeader': {
										backgroundColor: 'white',
									},
								}}
							>
								<TableHead>
									{/* Основной заголовок */}
									<TableRow>
										<CenteredCell rowSpan={7}>Шифр за ОПП</CenteredCell>
										<CenteredCell rowSpan={7}>
											Назва навчальної дисципліни
										</CenteredCell>
										<CenteredCell colSpan={3}>
											Розподіл за семестрами
										</CenteredCell>
										<CenteredCell colSpan={3}>Кількість годин</CenteredCell>
										<CenteredCell colSpan={16}>
											Розподіл аудиторних годин на тиждень та кредитів ECTS за
											семестрами
										</CenteredCell>
									</TableRow>

									{/* Подзаголовки */}
									<TableRow>
										<RotatedCell rowSpan={6}>
											<div className='rotated-text'>Екзамени</div>
										</RotatedCell>
										<RotatedCell rowSpan={6}>
											<div className='rotated-text'>Заліки</div>
										</RotatedCell>
										<RotatedCell rowSpan={6}>
											<div className='rotated-text'>Індивідуальні завдання</div>
										</RotatedCell>
										{/* Подзаголовки для количества часов */}
										<RotatedCell rowSpan={6}>
											<div className='rotated-text'>лекції</div>
										</RotatedCell>
										<RotatedCell rowSpan={6}>
											<div className='rotated-text'>лабораторні</div>
										</RotatedCell>
										<RotatedCell rowSpan={6}>
											<div className='rotated-text'>практичні</div>
										</RotatedCell>
										{/* Курсы */}
										{dynamicCourseHeaders.map(header => (
											<CenteredCell key={header.id} colSpan={header.colSpan}>
												<div className='centered-text'>{header.label}</div>
											</CenteredCell>
										))}
									</TableRow>

									{/* Семестры */}
									<TableRow>
										<CenteredCell colSpan={16}>С е м е с т р и</CenteredCell>
									</TableRow>

									<TableRow>
										{dynamicSemesterHeaders.map(num => (
											<CenteredCell key={num} colSpan={2}>
												{num}
											</CenteredCell>
										))}
									</TableRow>

									<TableRow>
										{dynamicWeeksInSemester.map((weeks, idx) => (
											<CenteredCell key={idx} colSpan={2}>
												{weeks}
											</CenteredCell>
										))}
									</TableRow>

									{/* Часы и кредиты */}
									<TableRow>
										{Array(semesterCount * 2)
											.fill(null)
											.map((_, idx) => (
												<RotatedCell key={idx}>
													<div className='rotated-text'>
														{idx % 2 === 0
															? 'Аудиторні години'
															: 'Кредити ECTS'}
													</div>
												</RotatedCell>
											))}
									</TableRow>
								</TableHead>
								<TableBody>
									{plans.map((plan, index) => (
										<FillRow
											key={index}
											plan={plan}
											semesterCount={semesterCount}
											isEditable={isEditable}
											onUpdate={handleUpdatePlan}
										/>
									))}
								</TableBody>
							</Table>
						</TableContainer>
					) : curriculums.length === 0 ? (
						<Box
							sx={{
								height: '100%',
								display: 'flex',
								alignItems: 'center',
								justifyContent: 'center',
								color: 'text.secondary',
							}}
						>
							Завантажте навчальні плани
						</Box>
					) : (
						<Box
							sx={{
								height: '100%',
								display: 'flex',
								alignItems: 'center',
								justifyContent: 'center',
								color: 'text.secondary',
							}}
						>
							Виберіть навчальний план зі списку
						</Box>
					)}
				</Paper>
			</Box>

			<PopupDialog
				open={showSuccessPopup}
				onClose={() => setShowSuccessPopup(false)}
				title='Успіх'
				message='Зміни успішно збережено'
				onConfirm={() => setShowSuccessPopup(false)}
				confirmText='OK'
			/>

			<PopupDialog
				open={showErrorPopup}
				onClose={() => setShowErrorPopup(false)}
				title='Помилка'
				message={errorMessage}
				onConfirm={() => setShowErrorPopup(false)}
				confirmText='OK'
			/>

			<ButtonDialog
				open={showCreatePlanDialog}
				onClose={() => setShowCreatePlanDialog(false)}
				title={curriculums.find(c => c.id === Number(curriculumId))?.name || ''}
				message='Виберіть пакет та введіть суфікс групи'
				onConfirm={() => setShowCreatePlanDialog(false)}
				confirmText='Створити'
				curriculumId={
					curriculums.find(c => c.id === Number(curriculumId))?.id
				}
			/>
		</Box>
	)
}
