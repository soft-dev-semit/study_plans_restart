import { List, ListItem, ListItemButton, ListItemText, Paper } from '@mui/material'
import { useNavigate } from 'react-router-dom'

interface Curriculum {
  id: number
  name: string
}

interface CurriculumListProps {
  curriculums: Curriculum[]
  selectedId?: string
}

export default function CurriculumList({ curriculums, selectedId }: CurriculumListProps) {
  const navigate = useNavigate()
  return (
		(
			<Paper
				sx={{
					width: '250px',
					overflowY: 'scroll',
					borderRadius: 0,
				}}
			>
				<List>
					{curriculums.map((curriculum) => (
						<ListItem key={curriculum.id} disablePadding>
							<ListItemButton
								selected={selectedId === curriculum.id.toString()}
								onClick={() => navigate(`/plans/${curriculum.id}`)}
							>
								<ListItemText primary={curriculum.name} />
							</ListItemButton>
						</ListItem>
					))}
				</List>
			</Paper>
		)
	)
} 