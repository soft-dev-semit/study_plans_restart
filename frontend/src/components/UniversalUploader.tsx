import React, { useState, useRef, useEffect } from 'react'
import { useDropzone } from 'react-dropzone'
import {
	Box,
	Typography,
	Button,
	Dialog,
	DialogActions,
	DialogContent,
	DialogContentText,
	DialogTitle,
	List,
	ListItem,
	ListItemIcon,
	ListItemText,
	ListItemButton,
	Paper,
	Tab,
	Tabs,
	CircularProgress
} from '@mui/material'
import CloudUploadIcon from '@mui/icons-material/CloudUpload'
import UploadFileIcon from '@mui/icons-material/UploadFile'
import FolderIcon from '@mui/icons-material/Folder'
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile'
import DescriptionIcon from '@mui/icons-material/Description'
import ImageIcon from '@mui/icons-material/Image'
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf'
import ArticleIcon from '@mui/icons-material/Article'
import VisibilityIcon from '@mui/icons-material/Visibility'
import * as XLSX from 'xlsx'
import {ExcelPreview} from "./ExcelPreview";

interface UniversalUploaderProps {
	onFileSelected?: (file: File) => void
	onDirectorySelected?: (directoryPath: string, files: File[]) => void
	acceptedFileTypes?: Record<string, string[]>
	maxFiles?: number
	onUpload?: (files: File[]) => void
	excludePatterns?: RegExp[]
}

interface TabPanelProps {
	children?: React.ReactNode
	index: number
	value: number
}

function TabPanel(props: TabPanelProps) {
	const { children, value, index, ...other } = props

	return (
		<div
			role="tabpanel"
			hidden={value !== index}
			id={`file-preview-tabpanel-${index}`}
			aria-labelledby={`file-preview-tab-${index}`}
			{...other}
		>
			{value === index && (
				<Box sx={{ p: 3 }}>
					{children}
				</Box>
			)}
		</div>
	)
}

const UniversalUploader: React.FC<UniversalUploaderProps> = ({
		 onFileSelected,
		 onDirectorySelected,
		 acceptedFileTypes = {
			 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': [
				 '.xlsx',
			 ],
			 'application/vnd.ms-excel': ['.xls'],
			 'application/vnd.openxmlformats-officedocument.wordprocessingml.document': [
				 '.docx',
			 ],
			 'application/msword': ['.doc'],
		 },
		 maxFiles = 1,
		 onUpload,

	 }) => {
	const [directoryPath, setDirectoryPath] = useState<string>('')
	const [files, setFiles] = useState<File[]>([])
	const [uploadMode, setUploadMode] = useState<'file' | 'directory' | null>(null)
	const [confirmDialogOpen, setConfirmDialogOpen] = useState<boolean>(false)
	const [previewDialogOpen, setPreviewDialogOpen] = useState<boolean>(false)
	const [selectedFileIndex, setSelectedFileIndex] = useState<number>(-1)
	const [fileContentDialogOpen, setFileContentDialogOpen] = useState<boolean>(false)
	const [fileContent, setFileContent] = useState<any>(null)
	const [isLoadingContent, setIsLoadingContent] = useState<boolean>(false)
	const [previewTabIndex, setPreviewTabIndex] = useState<number>(0)
	const directoryInputRef = useRef<HTMLInputElement>(null)

	useEffect(() => {
		if (directoryInputRef.current) {
			directoryInputRef.current.setAttribute('webkitdirectory', 'true')
			directoryInputRef.current.setAttribute('directory', 'true')
		}
	}, [])

	const handleDirectoryChange = (
		event: React.ChangeEvent<HTMLInputElement>
	): void => {
		const fileList = event.target.files;
		if (!fileList || fileList.length === 0) return;

		const selectedFiles = Array.from(fileList).filter(
			file => !file.name.endsWith('.DS_Store') && !file.webkitRelativePath.endsWith('.DS_Store')
		) as File[];

		if (selectedFiles.length > 0) {
			const fullPath = selectedFiles[0].webkitRelativePath;
			const directoryName = fullPath.split('/')[0];

			console.log('Selected directory:', directoryName);
			console.log('Total files:', selectedFiles.length);

			setFiles(selectedFiles);
			setDirectoryPath(directoryName);
			setUploadMode('directory');

			if (onDirectorySelected) {
				onDirectorySelected(directoryName, selectedFiles);
			}
		}
	};

	const { getRootProps, getInputProps, isDragActive } = useDropzone({
		onDrop: acceptedFiles => {
			// Фильтруем .DS_Store файлы
			const filteredFiles = acceptedFiles.filter(
				(file: File) => !file.name.endsWith('.DS_Store')
			);

			const validFiles = filteredFiles.filter((file: File) => {
				const fileExtension = file.name.split('.').pop()?.toLowerCase() || '';
				const allowedExtensions = Object.values(acceptedFileTypes)
					.flat()
					.map(ext => ext.replace('.', ''));
				return allowedExtensions.includes(fileExtension);
			}) as File[];

			if (validFiles.length > 0) {
				setFiles(validFiles);
				setUploadMode('file');

				if (onFileSelected && validFiles.length === 1) {
					onFileSelected(validFiles[0]);
				}
			}
		},
		accept: acceptedFileTypes,
		maxFiles: maxFiles,
		noClick: true,
		noKeyboard: true,
	});

	const resetSelection = () => {
		setFiles([])
		setDirectoryPath('')
		setUploadMode(null)
	}

	// Обработчики для диалога подтверждения
	const handleOpenConfirmDialog = () => {
		setConfirmDialogOpen(true)
	}

	const handleCloseConfirmDialog = () => {
		setConfirmDialogOpen(false)
	}

	const handleConfirmUpload = () => {
		setConfirmDialogOpen(false)

		if (onUpload) {
			onUpload(files)
		}
	}

	// Обработчики для диалога предпросмотра файлов
	const handleOpenPreviewDialog = () => {
		setPreviewDialogOpen(true)
	}

	const handleClosePreviewDialog = () => {
		setPreviewDialogOpen(false)
	}

	// Обработчики для просмотра содержимого файла
	const handleOpenFileContent = (index: number) => {
		setSelectedFileIndex(index)
		setFileContent(null)
		setIsLoadingContent(true)
		setFileContentDialogOpen(true)

		const file = files[index]
		const fileExtension = file.name.split('.').pop()?.toLowerCase() || ''

		if (['xlsx', 'xls'].includes(fileExtension)) {
			const reader = new FileReader()
			reader.onload = (e) => {
				try {
					const data = new Uint8Array(e.target?.result as ArrayBuffer)
					const workbook = XLSX.read(data, { type: 'array' })

					const sheets: Record<string, any[][]> = {};
					workbook.SheetNames.forEach(name => {
						const sheet = workbook.Sheets[name];
						const sheetData = XLSX.utils.sheet_to_json(sheet, { header: 1 }) as any[][];
						sheets[name] = sheetData;
					});

					setFileContent({
						type: 'excel',
						sheets,
						sheetNames: workbook.SheetNames
					})
				} catch (error) {
					setFileContent({
						type: 'error',
						message: 'Не удалось прочитать файл Excel'
					})
				}
				setIsLoadingContent(false)
			}
			reader.onerror = () => {
				setFileContent({
					type: 'error',
					message: 'Ошибка при чтении файла'
				})
				setIsLoadingContent(false)
			}
			reader.readAsArrayBuffer(file)
		} else if (['docx', 'doc'].includes(fileExtension)) {
			// Для реальной реализации рекомендуется использовать библиотеку mammoth.js
			// Здесь мы покажем заглушку с информацией, что полноценный просмотр не реализован
			setTimeout(() => {
				setFileContent({
					type: 'word',
					preview: 'Предпросмотр содержимого Word-документов в браузере требует использования специальных библиотек (mammoth.js). В реальном проекте здесь можно реализовать конвертацию DOCX в HTML.'
				})
				setIsLoadingContent(false)
			}, 500)
		} else {
			setFileContent({
				type: 'unsupported',
				message: 'Предпросмотр для этого типа файлов не поддерживается'
			})
			setIsLoadingContent(false)
		}
	}

	const handleCloseFileContent = () => {
		setFileContentDialogOpen(false)
		setSelectedFileIndex(-1)
		setFileContent(null)
	}

	const getFileIcon = (file: File) => {
		const extension = file.name.split('.').pop()?.toLowerCase() || ''

		if (['jpg', 'jpeg', 'png', 'gif', 'svg', 'webp'].includes(extension)) {
			return <ImageIcon />
		} else if (extension === 'pdf') {
			return <PictureAsPdfIcon />
		} else if (['doc', 'docx', 'txt', 'rtf'].includes(extension)) {
			return <ArticleIcon />
		} else if (['xls', 'xlsx', 'csv'].includes(extension)) {
			return <DescriptionIcon />
		} else {
			return <InsertDriveFileIcon />
		}
	}

	const formatFileSize = (bytes: number) => {
		if (bytes === 0) return '0 Bytes'

		const k = 1024
		const sizes = ['Bytes', 'KB', 'MB', 'GB']
		const i = Math.floor(Math.log(bytes) / Math.log(k))

		return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
	}

	const canPreviewContent = (file: File) => {
		const extension = file.name.split('.').pop()?.toLowerCase() || ''
		return ['xlsx', 'xls', 'docx', 'doc'].includes(extension)
	}

	const groupFilesByType = () => {
		const groups: Record<string, File[]> = {}

		files.forEach(file => {
			const extension = file.name.split('.').pop()?.toLowerCase() || 'unknown'
			if (!groups[extension]) {
				groups[extension] = []
			}
			groups[extension].push(file)
		})

		return groups
	}


	return (
		<>
			<Box sx={{ textAlign: 'center', mt: 4 }}>
				{!uploadMode ? (
					<Box
						{...getRootProps()}
						sx={{
							textAlign: 'center',
							border: '2px dashed',
							borderColor: isDragActive ? 'primary.main' : 'grey.300',
							width: '100%',
							padding: '40px',
							borderRadius: '8px',
							backgroundColor: isDragActive
								? 'action.hover'
								: 'background.paper',
							'&:hover': {
								backgroundColor: 'action.hover',
								borderColor: 'primary.main',
							},
						}}
					>
						<input {...getInputProps()} />
						<input
							type='file'
							ref={directoryInputRef}
							multiple
							onChange={handleDirectoryChange}
							style={{ display: 'none' }}
							id='directory-picker'
						/>

						<CloudUploadIcon
							sx={{ fontSize: 60, color: 'primary.main', mb: 2 }}
						/>

						<Typography variant='h6' gutterBottom>
							{isDragActive
								? 'Отпустите файл здесь'
								: 'Перетягніть файл сюди або виберіть опцію завантаження'}
						</Typography>

						<Box
							sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 2 }}
						>
							<Button
								variant='contained'
								color='primary'
								startIcon={<UploadFileIcon />}
								onClick={() => {
									const fileInput = document.createElement('input')
									fileInput.type = 'file'
									fileInput.accept = Object.values(acceptedFileTypes)
										.flat()
										.join(',')
									fileInput.multiple = maxFiles > 1

									fileInput.onchange = (e: Event) => {
										const target = e.target as HTMLInputElement
										if (target.files && target.files.length > 0) {
											const selectedFiles = Array.from(target.files) as File[]
											setFiles(selectedFiles)
											setUploadMode('file')

											if (onFileSelected && selectedFiles.length === 1) {
												onFileSelected(selectedFiles[0])
											}
										}
									}

									fileInput.click()
								}}
							>
								Обрати файл
							</Button>

							<label htmlFor='directory-picker'>
								<Button
									variant='outlined'
									color='primary'
									component='span'
									startIcon={<FolderIcon />}
								>
									Обрати директорию
								</Button>
							</label>
						</Box>

						<Typography variant='body2' color='text.secondary' sx={{ mt: 2 }}>
							Підтримуються файли{' '}
							{Object.values(acceptedFileTypes).flat().join(', ')}
						</Typography>
					</Box>
				) : (
					<Box
						sx={{
							border: '1px solid',
							borderColor: 'primary.main',
							borderRadius: '8px',
							padding: 3,
							backgroundColor: 'background.paper',
						}}
					>
						{uploadMode === 'file' ? (
							<>
								<Typography variant='body1' color='text.primary'>
									Выбран файл: {files[0]?.name}
								</Typography>
								{files.length > 1 && (
									<Typography variant='body2' color='text.secondary'>
										И еще {files.length - 1} файлов
									</Typography>
								)}
							</>
						) : (
							<>
								<Typography variant='body1' color='text.primary'>
									Выбрана директория: {directoryPath}
								</Typography>
								<Typography variant='body2' color='text.secondary'>
									Количество файлов: {files.length}
								</Typography>
							</>
						)}

						<Box
							sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 2 }}
						>
							<Button
								variant='outlined'
								color='primary'
								onClick={handleOpenPreviewDialog}
								startIcon={<InsertDriveFileIcon />}
							>
								Просмотреть файлы
							</Button>

							<Button
								variant='contained'
								color='primary'
								onClick={handleOpenConfirmDialog}
								startIcon={<CloudUploadIcon />}
							>
								Отправить
							</Button>

							<Button variant='text' color='error' onClick={resetSelection}>
								Сбросить выбор
							</Button>
						</Box>
					</Box>
				)}
			</Box>

			{/* Диалог подтверждения */}
			<Dialog open={confirmDialogOpen} onClose={handleCloseConfirmDialog}>
				<DialogTitle>Подтверждение отправки</DialogTitle>
				<DialogContent>
					<DialogContentText>
						{uploadMode === 'file'
							? `Вы уверены, что хотите отправить ${
									files.length === 1 ? 'файл' : files.length + ' файлов'
							  }?`
							: `Вы уверены, что хотите отправить директорию "${directoryPath}" (${files.length} файлов)?`}
					</DialogContentText>
				</DialogContent>
				<DialogActions>
					<Button onClick={handleCloseConfirmDialog} color='primary'>
						Отмена
					</Button>
					<Button
						onClick={handleConfirmUpload}
						color='primary'
						variant='contained'
					>
						Подтвердить
					</Button>
				</DialogActions>
			</Dialog>

			{/* Диалог предпросмотра файлов */}
			<Dialog
				open={previewDialogOpen}
				onClose={handleClosePreviewDialog}
				maxWidth='md'
				fullWidth
			>
				<DialogTitle>
					Выбранные файлы
					<Typography variant='subtitle2' color='text.secondary'>
						Всего: {files.length} файлов
					</Typography>
				</DialogTitle>
				<DialogContent dividers>
					<Tabs
						value={previewTabIndex}
						onChange={(e, newIndex) => setPreviewTabIndex(newIndex)}
						sx={{ borderBottom: 1, borderColor: 'divider', mb: 2 }}
					>
						<Tab label='Список файлов' />
						<Tab label='По типам' disabled={uploadMode === 'directory'} />
					</Tabs>

					<TabPanel value={previewTabIndex} index={0}>
						{files.length === 0 ? (
							<Typography variant='body1' align='center'>
								Нет выбранных файлов
							</Typography>
						) : (
							<Paper
								variant='outlined'
								sx={{ maxHeight: 400, overflow: 'auto', p: 1 }}
							>
								<List dense>
									{files.slice(0, 100).map((file, index) => (
										<ListItem
											key={index}
											secondaryAction={
												canPreviewContent(file) && (
													<Button
														size='small'
														startIcon={<VisibilityIcon />}
														onClick={() => handleOpenFileContent(index)}
													>
														Просмотр
													</Button>
												)
											}
											disablePadding
										>
											<ListItemButton>
												<ListItemIcon>{getFileIcon(file)}</ListItemIcon>
												<ListItemText
													primary={
														uploadMode === 'directory'
															? file.webkitRelativePath
															: file.name
													}
													secondary={formatFileSize(file.size)}
												/>
											</ListItemButton>
										</ListItem>
									))}
									{files.length > 100 && (
										<ListItem>
											<ListItemText
												primary={`... и еще ${files.length - 100} файлов`}
											/>
										</ListItem>
									)}
								</List>
							</Paper>
						)}
					</TabPanel>

					<TabPanel value={previewTabIndex} index={1}>
						{Object.entries(groupFilesByType()).map(([ext, filesOfType]) => (
							<Box key={ext} sx={{ mb: 3 }}>
								<Typography
									variant='subtitle1'
									sx={{ display: 'flex', alignItems: 'center', mb: 1 }}
								>
									{getFileIcon(filesOfType[0])}
									<Box component='span' sx={{ ml: 1 }}>
										{ext.toUpperCase()} файлы ({filesOfType.length})
									</Box>
								</Typography>
								<Paper
									variant='outlined'
									sx={{ maxHeight: 250, overflow: 'auto', p: 1 }}
								>
									<List dense>
										{filesOfType.map((file, index) => (
											<ListItem
												key={index}
												secondaryAction={
													canPreviewContent(file) && (
														<Button
															size='small'
															startIcon={<VisibilityIcon />}
															onClick={() =>
																handleOpenFileContent(
																	files.findIndex(f => f === file)
																)
															}
														>
															Просмотр
														</Button>
													)
												}
												disablePadding
											>
												<ListItemButton>
													<ListItemText
														primary={file.name}
														secondary={formatFileSize(file.size)}
													/>
												</ListItemButton>
											</ListItem>
										))}
									</List>
								</Paper>
							</Box>
						))}
					</TabPanel>
				</DialogContent>
				<DialogActions>
					<Button onClick={handleClosePreviewDialog} color='primary'>
						Закрыть
					</Button>
					<Button
						onClick={() => {
							handleClosePreviewDialog()
							handleOpenConfirmDialog()
						}}
						color='primary'
						variant='contained'
					>
						Перейти к отправке
					</Button>
				</DialogActions>
			</Dialog>

			{/* Диалог просмотра содержимого файла */}
			<Dialog
				open={fileContentDialogOpen}
				onClose={handleCloseFileContent}
				maxWidth='lg'
				fullWidth
			>
				{selectedFileIndex >= 0 && (
					<>
						<DialogTitle>
							Просмотр файла: {files[selectedFileIndex]?.name}
						</DialogTitle>
						<DialogContent dividers>
							{isLoadingContent ? (
								<Box sx={{ display: 'flex', justifyContent: 'center', p: 4 }}>
									<CircularProgress />
								</Box>
							) : fileContent ? (
								<>
									{fileContent.type === 'excel' && (
										<ExcelPreview content={fileContent} />
									)}
									{fileContent.type === 'word' && (
										<Paper sx={{ p: 3 }}>
											<Typography variant='body1'>
												{fileContent.preview}
											</Typography>
										</Paper>
									)}
									{fileContent.type === 'error' ||
										(fileContent.type === 'unsupported' && (
											<Box sx={{ p: 2 }}>
												<Typography color='error'>
													{fileContent.message}
												</Typography>
											</Box>
										))}
								</>
							) : (
								<Typography>Не удалось загрузить содержимое файла</Typography>
							)}
						</DialogContent>
						<DialogActions>
							<Button onClick={handleCloseFileContent} color='primary'>
								Закрыть
							</Button>
						</DialogActions>
					</>
				)}
			</Dialog>
		</>
	)
}

export default UniversalUploader