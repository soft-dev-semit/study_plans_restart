import { Dialog, DialogTitle, DialogContent, DialogActions, Button, Select, MenuItem, TextField, Typography } from '@mui/material'
import React, { useEffect, useState } from 'react'
import { Requests } from '../api/Requests'
import { createNewPlanFromTemplatePropsInterface } from './GlobalVariable'

interface ButtonDialogProps {
    open: boolean
    onClose: () => void
    title: string
    message: string
    onConfirm: () => void
    confirmText?: string
    cancelText?: string
    type?: 'confirm' | 'delete'
    curriculumId?: number
}

interface Package {
	id: number
	nameOfPackage: string
	indexOfDiscipline: string
}


const ButtonDialog: React.FC<ButtonDialogProps> = ({
    open,
    onClose,
    title,
    onConfirm,
    confirmText,
    cancelText = 'Скасувати',
    type = 'confirm',
    curriculumId
}) => {
    const [packages, setPackages] = useState<Package[]>([]);
    const [createNewPlanFromTemplateProps, setCreateNewPlanFromTemplateProps] =
        useState<createNewPlanFromTemplatePropsInterface>({
            curriculum_id: 0,
            package_id: 0,
            suffix: ''
        })

    useEffect(() => {
        const fetchPackages = async () => {
            try {
                const resp = await Requests.getAllPackages(Number(curriculumId))
                setPackages(Array.isArray(resp) ? resp : [])
            } catch (error) {
                console.error('Error fetching packages:', error)
                setPackages([])
            }
        }
        fetchPackages()
    }, [curriculumId])

    const handleConfirm = async () => {
        console.log('Sending data:', createNewPlanFromTemplateProps)
        createNewPlanFromTemplateProps.curriculum_id = Number(curriculumId)
        const resp = await Requests.createNewPlanFromTemplate(createNewPlanFromTemplateProps)
        console.log('Response:', resp)
    }
    console.log(
			'createNewPlanFromTemplateProps:',
			createNewPlanFromTemplateProps
		)
    console.log('Selected curriculumId:', curriculumId)
    console.log(packages)

    const getConfirmButtonProps = () => {
        switch (type) {
            case 'delete':
                return {
                    color: 'error' as const,
                    text: confirmText || 'Видалити'
                }
            case 'confirm':
            default:
                return {
                    color: 'primary' as const,
                    text: confirmText || 'Підтвердити'
                }
        }
    }

    const confirmButtonProps = getConfirmButtonProps()

    return (
			<Dialog open={open} onClose={onClose} maxWidth='sm' fullWidth>
				<DialogTitle sx={{ textAlign: 'center' }}>
					Створити новий план з шаблону {title}
				</DialogTitle>
				<DialogContent>
					<TextField
						fullWidth
						margin='normal'
						label='Суфікс групи'
						value={createNewPlanFromTemplateProps.suffix}
						onChange={e => {
							console.log('New suffix:', e.target.value)
							setCreateNewPlanFromTemplateProps({
								...createNewPlanFromTemplateProps,
								suffix: e.target.value
							})
						}}
					/>
					<Select
						fullWidth
						value={createNewPlanFromTemplateProps.package_id}
						onChange={e => {
							console.log('New package_id:', e.target.value)
							setCreateNewPlanFromTemplateProps({
								...createNewPlanFromTemplateProps,
								package_id: Number(e.target.value)
							})
						}}
					>
						{Array.isArray(packages) && packages.map(pkg => (
							<MenuItem key={pkg.id} value={pkg.id}>
								{pkg.nameOfPackage}
							</MenuItem>
						))}
					</Select>
				</DialogContent>
				<DialogActions>
					<Button onClick={onClose}>{cancelText}</Button>
					<Button
						onClick={() => {
							handleConfirm()
							onConfirm()
						}}
						color={confirmButtonProps.color}
						variant='contained'
					>
						{confirmButtonProps.text}
					</Button>
				</DialogActions>
			</Dialog>
		)
}

export default ButtonDialog

