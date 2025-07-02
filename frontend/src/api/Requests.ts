import axios from 'axios'
import { createNewPlanFromTemplatePropsInterface, disciplineCurriculumInterface, disciplineInterface, semesterInterface  } from '../helper/GlobalVariable'

const domain = `http://localhost:8080/api/`

const axiosInstance = axios.create({
	baseURL: domain,
	headers: {
		'Content-Type': 'application/json',
		Accept: 'application/json',
	},
})

axiosInstance.interceptors.request.use(
	request => {
		console.log(`${request.method?.toUpperCase()} ${request.url}`, request)
		return request
	},
	error => {
		console.error('REQ FAIL: ', error)
		return Promise.reject(error)
	}
)

axiosInstance.interceptors.response.use(
	response => {
		console.log('RES: ', response)
		return response
	},
	async error => {
		console.error(
			`${error.config.method.toUpperCase()}: ${error.config.url} ${error.code}`,
			error
		);
		if (error.response?.data?.message) {
			return Promise.resolve(error.response);
		}
		return Promise.reject(error);
	}
)
export const Requests = {
	async getPlans(curriculumId: number) {
		try {
			const response = await axiosInstance.get(
				`/curriculum/${curriculumId}/all`
			)
			return response.data
		} catch (error) {
			console.error('Error fetching plans:', error)
			return []
		}
	},

	async getAllCurriculums() {
		try {
			const response = await axiosInstance.get('/curriculum/')
			return response.data
		} catch (error) {
			console.error('Error fetching all curriculums:', error)
			return []
		}
	},

	async importSingleFile(file: File) {
		try {
			const formData = new FormData()
			formData.append('file', file)

			const response = await axiosInstance.post('/import/single', formData, {
				headers: {
					'Content-Type': 'multipart/form-data',
				},
			})
			return response.data
		} catch (error) {
			console.error('Error importing file:', error)
			return []
		}
	},
	async importMultiFile(files: File[]) {
		try {
			const formData = new FormData()
			files.forEach(file => {
				formData.append('file', file) // Добавляем каждый файл отдельно
			})

			const response = await axiosInstance.post('/import/multiple', formData, {
				headers: {
					'Content-Type': 'multipart/form-data',
				},
			})
			return response.data
		} catch (error) {
			console.error('Error importing file:', error)
			return []
		}
	},

	async importDirectory(filepath: FormData) {
		try {
			const response = await axiosInstance.post('/import/directory', filepath, {
				headers: {
					'Content-Type': 'multipart/form-data',
				},
			})
			return response.data
		} catch (error) {
			console.error('Error fetching all curriculums:', error)
			return []
		}
	},

	async getCourse(season: string) {
		try {
			const response = await axiosInstance.get(`/export/${season}`)
			return response.data
		} catch (error) {
			console.error('Error fetching all courses:', error)
			return []
		}
	},

	// discipline brakepoints ==================================================================>

	async getAllDisciplines() {
		try {
			const response = await axiosInstance.get('/discipline/all')
			return response.data
		} catch (error) {
			console.error('Error fetching all disciplines:', error)
			return []
		}
	},

	async getDiscipline(discipline_id: number) {
		try {
			const response = await axiosInstance.get(`/discipline/${discipline_id}`)
			return response.data
		} catch (error) {
			console.error('Error fetching discipline:', error)
			return null
		}
	},

	async updateDiscipline(disciplineList: disciplineInterface[]) {
		try {
			const response = await axiosInstance.patch(
				`/discipline/update`,
				disciplineList
			)
			return response.data
		} catch (error) {
			console.error('Error updating discipline:', error)
			return null
		}
	},

	async deleteDiscipline(discipline_id: number) {
		try {
			const response = await axiosInstance.delete(
				`/discipline/${discipline_id}/delete`
			)
			return response.data
		} catch (error) {
			console.error('Error deleting discipline:', error)
			return null
		}
	},

	async createDiscipline(disciplineDTO: disciplineInterface) {
		try {
			const response = await axiosInstance.post(
				`/discipline/create`,
				disciplineDTO
			)
			return response.data
		} catch (error) {
			console.error('Error creating discipline:', error)
			return null
		}
	},

	// disciplineCurriculum brakepoints ==================================================================>

	async updateDisciplineCurriculum(
		disciplineCurriculumDTO: disciplineCurriculumInterface[]
	) {
		try {
			const response = await axiosInstance.patch(
				`/curriculum/update`,
				disciplineCurriculumDTO
			)
			return response.data
		} catch (error) {
			console.error('Error updating discipline curriculum:', error)
			return null
		}
	},

	async createNewPlanFromTemplate(
		createPlanDTO: createNewPlanFromTemplatePropsInterface
	) {
		try {
			const response = await axiosInstance.post(
				`/curriculum/plans/create`,
				createPlanDTO
			)
			return response.data
		} catch (error) {
			console.error('Error creating new plan from template')
			return null
		}
	},

	// semester brakepoints =============================================================================>

	async updateSemesters(semesterList: semesterInterface[]) {
		try {
			const response = await axiosInstance.patch(
				`/semester/update`,
				semesterList
			)
			return response.data
		} catch (error) {
			console.error('Error updating semester')
			return null
		}
	},

	// curriculum brakepoints =============================================================================>

	async getAllPackages(curriculum_id: number) {
		try {
			const response = await axiosInstance.get(
				`/curriculum/${curriculum_id}/packages`
			)
			return response.data
		} catch (error) {
			console.error('Error fetching all packages')
			return []
		}
	},

	// group brakepoints =============================================================================>
	async getAllGroup() {
		try {
			const response = await axiosInstance.get('/group/')
			return response.data
		} catch (error) {
			console.error('Error fetching all curriculums:', error)
			return []
		}
	},
}
