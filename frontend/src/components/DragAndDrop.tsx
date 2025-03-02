import { useDropzone } from 'react-dropzone'
import Box from '@mui/material/Box'
import { Typography } from '@mui/material'
import CloudUploadIcon from '@mui/icons-material/CloudUpload'

interface DragAndDropProps {
	onFileSelected: (file: File) => void
}

export default function DragAndDrop({ onFileSelected }: DragAndDropProps) {
	const { getRootProps, getInputProps, isDragActive } = useDropzone({
		onDrop: (acceptedFiles: any) => {
			const validFiles = acceptedFiles.filter((file: any) => {
				const allowedExtensions = ['xlsx', 'xls']
				const fileExtension = file.name.split('.').pop()?.toLowerCase() || ''
				return allowedExtensions.includes(fileExtension)
			})

			if (validFiles.length > 0) {
				onFileSelected(validFiles[0])
			}
		},
		accept: {
			'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': ['.xlsx'],
			'application/vnd.ms-excel': ['.xls']
		},
		maxFiles: 1
	})

	return (
		<Box
			{...getRootProps()}
			sx={{
				textAlign: 'center',
				mt: 2,
				border: '2px dashed',
				borderColor: isDragActive ? 'primary.main' : 'grey.300',
				width: '100%',
				padding: '40px',
				borderRadius: '8px',
				cursor: 'pointer',
				backgroundColor: isDragActive ? 'action.hover' : 'background.paper',
				'&:hover': {
					backgroundColor: 'action.hover',
					borderColor: 'primary.main'
				}
			}}
		>
			<input {...getInputProps()} />
			<CloudUploadIcon sx={{ fontSize: 60, color: 'primary.main', mb: 2 }} />
			<Typography variant="h6" gutterBottom>
				{isDragActive ? 
					'Отпустите файл здесь' : 
					'Перетащите Excel файл сюда или нажмите для выбора'
				}
			</Typography>
			<Typography variant="body2" color="text.secondary">
				Поддерживаются файлы .xlsx и .xls
			</Typography>
		</Box>
	)
}
