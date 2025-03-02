import { AppBar, Toolbar, Button, Box } from '@mui/material'
import { useNavigate, useLocation } from 'react-router-dom'
import { styled } from '@mui/material/styles'


export default function Navbar() {
  const navigate = useNavigate()
  const location = useLocation()

  const isActive = (path: string) => location.pathname.includes(path)
  
  const CustomButton = styled(Button)(({ theme }) => ({
    color: 'black',
    borderRadius: 0,
    flex: 1,
    padding: '15px 0',
    borderLeft: '1px solid #e0e0e0',
    borderRight: '1px solid #e0e0e0',
    minWidth: 0,
    '&:not(:last-child)': {
      borderRight: 'none',
    },
    '&:hover': {
      backgroundColor: theme.palette.primary.light,
    },
  }))


  return (
		<AppBar 
			position='static' 
			sx={{ 
				backgroundColor: 'white', 
				boxShadow: 1,
				m: 0,
				width: '100%',
			}}
		>
			<Toolbar 
				disableGutters
				sx={{ 
					p: 0,
					minHeight: 'auto',
				}}
			>
				<Box sx={{ 
					display: "flex", 
					width: '100%',
					'& > button': {
						flexBasis: 0,
						flexGrow: 1,
					}
				}}>
					<CustomButton
						color='primary'
						variant={isActive('/profile') ? 'contained' : 'text'}
						onClick={() => navigate('/profile')}
					>
						Профіль
					</CustomButton>
					<CustomButton
						color='primary'
						variant={isActive('/plans') ? 'contained' : 'text'}
						onClick={() => navigate('/plans')}
					>
						Плани
					</CustomButton>
					<CustomButton
						color='primary'
						variant={isActive('/study-load') ? 'contained' : 'text'}
						onClick={() => navigate('/study-load')}
					>
						Навантаження
					</CustomButton>
					<CustomButton
						color='primary'
						variant={isActive('/import') ? 'contained' : 'text'}
						onClick={() => navigate('/import')}
					>
						Імпорт
					</CustomButton>
					<CustomButton
						color='primary'
						variant={isActive('/export') ? 'contained' : 'text'}
						onClick={() => navigate('/export')}
					>
						Експорт
					</CustomButton>
				</Box>
			</Toolbar>
		</AppBar>
	)
} 