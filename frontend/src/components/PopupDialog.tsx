import { Dialog, DialogTitle, DialogContent, DialogActions, Button } from '@mui/material'
import React from 'react'

interface PopupDialogProps {
    open: boolean
    onClose: () => void
    title: string
    message: string
    onConfirm: () => void
    confirmText?: string
    cancelText?: string
}

const PopupDialog: React.FC<PopupDialogProps> = ({
    open,
    onClose,
    title,
    message,
    onConfirm,
    confirmText = 'Підтвердити',
    cancelText = 'Скасувати'
}) => {
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>{title}</DialogTitle>
            <DialogContent>
                {message}
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>{cancelText}</Button>
                <Button onClick={onConfirm} color="primary" variant="contained">
                    {confirmText}
                </Button>
            </DialogActions>
        </Dialog>
    )
}

export default PopupDialog 