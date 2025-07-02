import React, { useState } from 'react'
import { Container, Typography } from '@mui/material'
import { Requests } from '../api/Requests'
import UniversalUploader from '../components/UniversalUploader'
import JSZip from 'jszip'

export default function Import() {
	const [isLoading, setIsLoading] = useState(false)

	const handleFileSelected = async (file: File) => {
		setIsLoading(true)
		try {
			let response
			console.log('Single file selected')
			response = await Requests.importSingleFile(
				Array.isArray(file) ? file[0] : file
			)
			console.log(response)
		} catch (error) {
			console.error('Ошибка:', error)
		} finally {
			setIsLoading(false)
		}
	}

	const handleDirectorySelected = async (
		directoryPath: string,
		files: File[]
	) => {
		setIsLoading(true)
		try {
			const zip = new JSZip()
			files.forEach(file => {
				const relativePath = file.webkitRelativePath
				zip.file(relativePath, file)
			})
			const zipBlob = await zip.generateAsync({ type: 'blob' })
			const formData = new FormData()
			formData.append('file', zipBlob, `${directoryPath}.zip`)
			const response = await Requests.importDirectory(formData)
			console.log('Directory imported successfully:', response.data)
		} catch (error) {
			console.error('Error importing directory:', error)
		} finally {
			setIsLoading(false)
		}
	}

	return (
		<Container maxWidth='md' sx={{ mt: 4 }}>
			<Typography variant='h4' gutterBottom align='center'>
				Імпорт навчального плану
			</Typography>
			<Typography
				variant='body1'
				gutterBottom
				align='center'
				color='text.secondary'
			>
				Завантажте Excel або Word файл з навчальним планом або виберіть
				директорію
			</Typography>

			<UniversalUploader
				onFileSelected={handleFileSelected}
				onDirectorySelected={handleDirectorySelected}
				acceptedFileTypes={{
					'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': [
						'.xlsx',
					],
					'application/vnd.ms-excel': ['.xls'],
					// 'application/vnd.openxmlformats-officedocument.wordprocessingml.document':
					// 	['.docx'],
					// 'application/msword': ['.doc'],
				}}
				maxFiles={1}
			/>
		</Container>
	)
}
