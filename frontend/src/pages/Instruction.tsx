import { Box, Container, Typography } from "@mui/material";

export default function Instruction() {
    return (
			<Container maxWidth='md' sx={{ mt: 4 }}>
				<Typography variant='h4' gutterBottom align='center'>
					Інструкція
				</Typography>
				<Typography
					variant='body1'
					gutterBottom
					align='center'
					color='text.secondary'
				>
					1) Планы
				</Typography>
				<Box sx={{ mt: 4 }}>
					<Typography>
						Для того, чтобы просмотреть планы, перейдите на вкладку "Плани" в
						меню 2 кнопка. Затем выберите нужный план из списка. Функционал
						позволяет просматривать планы по неделям, а также смотреть
						количество часов и кредитов. (Для редактирования планов еще не
						добавлен функционал. Приносим извинения за неудобства)
					</Typography>
				</Box>
				<Typography
					variant='body1'
					gutterBottom
					align='center'
					color='text.secondary'
				>
					2) Навантаження
				</Typography>
				<Box sx={{ mt: 4 }}>
					<Typography>
						Для того, чтобы просмотреть навантаження, перейдите на вкладку
						"Навантаження" в меню 3 кнопка. Можно выбрать сизон (осень/весна) и
						посмотреть навантаження. (Вариант только в разработке и не является
						окончательным) (Для редактирования планов еще не добавлен
						функционал. Приносим извинения за неудобства)
					</Typography>
				</Box>
				<Typography
					variant='body1'
					gutterBottom
					align='center'
					color='text.secondary'
				>
					3) Импорт
				</Typography>
				<Box sx={{ mt: 4 }}>
					<Typography>
						Для того, чтобы просмотреть навантаження, перейдите на вкладку
						"Импорт" в меню 4 кнопка. Можно выбрать файл или директорию для
						импорта. В поле для передачи файла с помощью Drag and Drop есть
						кнопки для выбора файла или директории. Поддерживаются файлы .xlsx.
						После выбора файла или директории нажмите кнопку "Завантажити".(В
						разработке, на данным момент просто сразу оправляет для валидации на
						сервер) (Вариант только в разработке и не является окончательным) :)
					</Typography>
				</Box>
				<Typography
					variant='body1'
					gutterBottom
					align='center'
					color='text.secondary'
				>
					4) Експорт
				</Typography>
				<Box sx={{ mt: 4 }}>
					<Typography>
						В дальнейшем будет функционал для экспрта будет перенесен в
						Навантаження (Сейчас это просто пустая страница как и профиль)
					</Typography>
				</Box>
			</Container>
		)
} 