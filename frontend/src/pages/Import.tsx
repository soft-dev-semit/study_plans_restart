import React, { useState } from 'react'
import { Box, Container, Typography } from '@mui/material'
import { Requests } from '../api/Requests'
import DragAndDrop from '../components/DragAndDrop'
import DirectoryPicker from '../components/DirectoryPicker'
import JSZip from 'jszip'

export default function Import() {
	const [isLoading, setIsLoading] = useState(false)

	const handleFileSelected = async (file:File) => {
		setIsLoading(true)
		try {
			let response

			if (Array.isArray(file) && file.length > 1) {
				console.log('Multiple files selected')
				response = await Requests.importMultiFile(file)
			} else {
				console.log('Single file selected')
				response = await Requests.importSingleFile(
					Array.isArray(file) ? file[0] : file
				)
			}

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
			// Создаем zip-архив
			const zip = new JSZip()
			files.forEach(file => {
				const relativePath = file.webkitRelativePath
				zip.file(relativePath, file)
			})

			const zipBlob = await zip.generateAsync({ type: 'blob' })

			const formData = new FormData()
			formData.append('file', zipBlob, `${directoryPath}.zip`)

			// Отправляем zip-архив на сервер
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
				Завантажте Excel файл з навчальним планом
			</Typography>
			<Box sx={{ mt: 4 }}>
				<DragAndDrop onFileSelected={handleFileSelected} />
			</Box>
			<Box sx={{ mt: 4 }}>
				<DirectoryPicker onDirectorySelected={handleDirectorySelected} />
			</Box>
		</Container>
	)
}
