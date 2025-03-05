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
	}
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
}

export default function Plans() {
	const [plans, setPlans] = useState<any[]>([]);
	const [curriculums, setCurriculums] = useState<Curriculum[]>([]);
	const [semesterCount, setSemesterCount] = useState(8)
	const [courseCount, setCourseCount] = useState(4)
	const { curriculumId } = useParams()

	useEffect(() => {
		const fetchCurriculums = async () => {
			const resp = await Requests.getAllCurriculums();
			console.log(JSON.stringify(resp));
			setCurriculums(resp);
		}
		fetchCurriculums()
	}, [])

	useEffect(() => {
		if (curriculumId) {
			const currentCurriculum = curriculums.find(
				c => c.id === Number(curriculumId)
			)
			
			// Проверяем, заканчивается ли название на 'с' или 'С'
			const isShortened = currentCurriculum?.name.match(/[сС]$/);
			setSemesterCount(isShortened ? 6 : 8)
			setCourseCount(isShortened ? 3 : 4)
		}
	}, [curriculumId, curriculums])

	// Динамически создаем заголовки для курсов
	const dynamicCourseHeaders = Array.from({ length: courseCount }, (_, i) => ({
		id: i + 1,
		label: `${i + 1 === 1 ? 'I' : i + 1 === 2 ? 'II' : i + 1 === 3 ? 'III' : 'IV'} курс`,
		colSpan: 4
	}))

	// Динамически создаем заголовки для семестров
	const dynamicSemesterHeaders = Array.from(
		{ length: semesterCount }, 
		(_, i) => i + 1
	)
	
	const dynamicWeeksInSemester = Array(semesterCount).fill(20)

	useEffect(() => {
		const fetchData = async () => {
			if (curriculumId) {
				try {
					const resp = await Requests.getPlans(Number(curriculumId))
					setPlans(Array.isArray(resp) ? resp : [])
				} catch (error) {
					console.error('something wrong' + error)
					setPlans([])
				}
			}
		}
		fetchData()
	}, [curriculumId])

	return (
		<Box sx={{ display: 'flex', height: '100%' }}>
			<CurriculumList 
				curriculums={curriculums} 
				selectedId={curriculumId} 
			/>
			<Paper sx={{ flexGrow: 1, overflow: 'hidden' }}>
				{curriculumId ? (
					<TableContainer 
						sx={{ 
							height: '100%',
							maxHeight: '100vh',
							"& .MuiTable-stickyHeader": {
								// borderCollapse: 'collapse',  
							},
							"& .MuiTableHead-root": {
								position: 'sticky',
								top: 0,
								zIndex: 1,
							}
						}}
					>
						<Table 
							stickyHeader 
							sx={{
								// borderCollapse: 'collapse',
								"& .MuiTableCell-stickyHeader": {
									backgroundColor: 'white',
								}
							}}
						>
							<TableHead>
								{/* Основной заголовок */}
								<TableRow>
									<CenteredCell rowSpan={7}>Шифр за ОПП</CenteredCell>
									<CenteredCell rowSpan={7}>
										Назва навчальної дисципліни
									</CenteredCell>
									<CenteredCell colSpan={3}>Розподіл за семестрами</CenteredCell>
									<CenteredCell colSpan={3}>Кількість годин</CenteredCell>
									<CenteredCell colSpan={16}>
										Розподіл аудиторних годин на тиждень та кредитів ECTS за
										семестрами
									</CenteredCell>
								</TableRow>

								{/* Подзаголовки */}
								<TableRow>
									{/* Пропускаем первые две ячейки (rowSpan=7) */}
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
									{/* Пропускаем первые 6 ячеек (rowSpan=6) */}
									<CenteredCell colSpan={16}>С е м е с т р и</CenteredCell>
								</TableRow>

								{/* Номера семестров */}
								<TableRow>
									{dynamicSemesterHeaders.map(num => (
										<CenteredCell key={num} colSpan={2}>
											{num}
										</CenteredCell>
									))}
								</TableRow>

								{/* Количество недель */}
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
													{idx % 2 === 0 ? 'Аудиторні години' : 'Кредити ECTS'}
												</div>
											</RotatedCell>
										))}
								</TableRow>

								{/* Нумерация колонок */}
								{/* <TableRow>
									{Array(24)
										.fill(null)
										.map((_, idx) => (
											<CenteredCell key={idx}>{idx + 1}</CenteredCell>
										))}
								</TableRow> */}
							</TableHead>
							<TableBody>
								{plans.map((plan, index) => (
									<FillRow 
										key={index} 
										plan={plan} 
										semesterCount={semesterCount}
									/>
								))}
							</TableBody>
						</Table>
					</TableContainer>
				) : (
					<Box 
						sx={{ 
							height: '100%', 
							display: 'flex', 
							alignItems: 'center', 
							justifyContent: 'center',
							color: 'text.secondary'
						}}
					>
						Виберіть навчальний план зі списку
					</Box>
				)}
			</Paper>
		</Box>
	)
}