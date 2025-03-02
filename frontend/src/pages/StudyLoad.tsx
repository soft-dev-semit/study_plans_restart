import { Box, FormControl, InputLabel, MenuItem, Paper, Select, SelectChangeEvent, styled, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import { useEffect, useState } from "react";
import { Requests } from "../api/Requests";

interface PlanItem {
  name: string;
  shortName: string;
  groups: string;
  course: string | number;
  semester: string | number;
  ects: number;
  totalHours: number;
  lectureHours: number;
  labHours: number;
  practiceHours: number;
  individualTask: string | boolean;
  hasCredit: boolean; // Изменено с Credit на hasCredit для соответствия ошибкам
  hasExam: boolean;   // Изменено с Exam на hasExam для соответствия ошибкам
}

// Интерфейс для ответа от API
interface ApiResponse {
  autumn: PlanItem[];
  spring: PlanItem[];
}

 const CenteredCell = styled(TableCell)(({ theme }) => ({
		textAlign: 'center',
		padding: '2px',
		// fontWeight: 'bold',
		verticalAlign: 'middle',
		border: '1px solid rgb(11, 1, 1)',
		backgroundColor: 'white',
		height: '30px',
 }))

export default function StudyLoad() {
	// Теперь используем правильные типы вместо never[]
	const [allPlans, setAllPlans] = useState<ApiResponse>({
		autumn: [],
		spring: [],
	})
	const [selectedSeason, setSelectedSeason] = useState<'autumn' | 'spring'>(
		'autumn'
	)
	const [displayedPlans, setDisplayedPlans] = useState<PlanItem[]>([])
	const [loading, setLoading] = useState<boolean>(true)

	useEffect(() => {
		const fetchData = async () => {
			setLoading(true)
			try {
				const resp = await Requests.getCourse()

				if (resp && typeof resp === 'object') {
					const typedResp = resp as ApiResponse
					setAllPlans({
						autumn: Array.isArray(typedResp.autumn) ? typedResp.autumn : [],
						spring: Array.isArray(typedResp.spring) ? typedResp.spring : [],
					})

					// Устанавливаем планы для текущего выбранного сезона
					setDisplayedPlans(
						Array.isArray(typedResp[selectedSeason])
							? typedResp[selectedSeason]
							: []
					)
				} else {
					console.error('Unexpected response format')
					setAllPlans({ autumn: [], spring: [] })
					setDisplayedPlans([])
				}
			} catch (error) {
				console.error('Something went wrong: ' + error)
				setAllPlans({ autumn: [], spring: [] })
				setDisplayedPlans([])
			} finally {
				setLoading(false)
			}
		}

		fetchData()
	}, [selectedSeason])

	// Обновление отображаемых данных при изменении сезона
	useEffect(() => {
		setDisplayedPlans(allPlans[selectedSeason] || [])
	}, [selectedSeason, allPlans])

	// Типизируем параметр event
	const handleSeasonChange = (event: SelectChangeEvent) => {
		setSelectedSeason(event.target.value as 'autumn' | 'spring')
	}

	return (
		<Box>
			<Box sx={{ mb: 2, mt: 2 }}>
				<FormControl fullWidth>
					<InputLabel id='season-select-label'>Выберите семестр</InputLabel>
					<Select
						labelId='season-select-label'
						id='season-select'
						value={selectedSeason}
						label='Выберите семестр'
						onChange={handleSeasonChange}
					>
						<MenuItem value='autumn'>Осень</MenuItem>
						<MenuItem value='spring'>Весна</MenuItem>
					</Select>
				</FormControl>
			</Box>

			<Paper sx={{ width: '100%', overflow: 'hidden' }}>
				<TableContainer sx={{ maxHeight: '78vh' }}>
					{' '}
					{/* Установим фиксированную высоту для контейнера */}
					<Table
						stickyHeader
						sx={{
							width: '100%',
							tableLayout: 'fixed', // Фиксируем ширину колонок
							'& .MuiTableCell-stickyHeader': {
								backgroundColor: 'white',
								zIndex: 1, // Убедимся, что заголовок всегда находится поверх содержимого
							},
						}}
					>
						<TableHead>
							<TableRow>
								<CenteredCell>#</CenteredCell>
								<CenteredCell>name</CenteredCell>
								<CenteredCell>shortName</CenteredCell>
								<CenteredCell>group</CenteredCell>
								<CenteredCell>course</CenteredCell>
								<CenteredCell>semester</CenteredCell>
								<CenteredCell>ects</CenteredCell>
								<CenteredCell>totalHours</CenteredCell>
								<CenteredCell>lectureHours</CenteredCell>
								<CenteredCell>labHours</CenteredCell>
								<CenteredCell>practiceHours</CenteredCell>
								<CenteredCell>individualTask</CenteredCell>
								<CenteredCell>Credit</CenteredCell>
								<CenteredCell>Exam</CenteredCell>
							</TableRow>
						</TableHead>
						<TableBody>
							{loading ? (
								<TableRow>
									<CenteredCell colSpan={13}>Загрузка данных...</CenteredCell>
								</TableRow>
							) : displayedPlans.length === 0 ? (
								<TableRow>
									<CenteredCell colSpan={13}>
										Нет данных для отображения
									</CenteredCell>
								</TableRow>
							) : (
								displayedPlans.map((plan, index) => (
									<TableRow key={index}>
										<CenteredCell>{index + 1}</CenteredCell>
										<CenteredCell>{plan.name}</CenteredCell>
										<CenteredCell>{plan.shortName}</CenteredCell>
										<CenteredCell>{plan.groups}</CenteredCell>
										<CenteredCell>{plan.course}</CenteredCell>
										<CenteredCell>{plan.semester}</CenteredCell>
										<CenteredCell>{plan.ects}</CenteredCell>
										<CenteredCell>{plan.totalHours}</CenteredCell>
										<CenteredCell>{plan.lectureHours}</CenteredCell>
										<CenteredCell>{plan.labHours}</CenteredCell>
										<CenteredCell>{plan.practiceHours}</CenteredCell>
										<CenteredCell>{plan.individualTask}</CenteredCell>
										<CenteredCell>{plan.hasCredit ? 'З' : ''}</CenteredCell>
										<CenteredCell>{plan.hasExam ? 'E' : ''}</CenteredCell>
									</TableRow>
								))
							)}
						</TableBody>
					</Table>
				</TableContainer>
				<Box sx={{ p: 2, display: 'flex', justifyContent: 'flex-end' }}>
					Всего записей: {displayedPlans.length}
				</Box>
			</Paper>
		</Box>
	)
}