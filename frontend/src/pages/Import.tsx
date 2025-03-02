import { Box, Container, Typography } from '@mui/material'
import DragAndDrop from '../components/DragAndDrop'
import { useState } from 'react'
import { Requests } from '../api/Requests'

export default function Import() {
  const [isLoading, setIsLoading] = useState(false)

    const handleFileSelected = async (file: File | File[]) => {
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


    return (
    <Container maxWidth="md" sx={{ mt: 4 }}>
      <Typography variant="h4" gutterBottom align="center">
        Імпорт навчального плану
      </Typography>
      <Typography variant="body1" gutterBottom align="center" color="text.secondary">
        Завантажте Excel файл з навчальним планом
      </Typography>
      <Box sx={{ mt: 4 }}>
        <DragAndDrop onFileSelected={handleFileSelected} />
      </Box>
    </Container>
  )
} 