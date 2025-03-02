import axios from 'axios'

const domain = `/api/`

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
		)
		if (error.response?.data?.message) {
			return Promise.resolve(error.response)
		}
		return Promise.reject(error)
	}
)
export const Requests = {

	async getPlans(curriculumId: number) {
		try {
			const response = await axiosInstance.get(`/curriculum/${curriculumId}/all`)
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
					'Content-Type': 'multipart/form-data'
				}
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
			files.forEach((file) => {
				formData.append('file', file)
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

	async importDirectory(filepath: string) {
		try {
			const response = await axiosInstance.post('/import/directory', {
				filepath,
			})
			return response.data
		} catch (error) {
			console.error('Error fetching all curriculums:', error)
			return []
		}
	},

	async getCourse() {
		try {
			const response = await axiosInstance.get('/export/')
			return response.data
		} catch (error) {
			console.error('Error fetching all courses:', error)
			return []
		}
	}
}
