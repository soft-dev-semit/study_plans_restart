import React from 'react';
import {
	createBrowserRouter,
	createRoutesFromElements,
	Route,
	RouterProvider,
	Navigate,
	Outlet
} from 'react-router-dom'
import { SnackbarProvider } from 'notistack'
import { lazy, Suspense } from 'react'
import Navbar from './components/Navbar'
import { Box } from '@mui/material'

const Plans = lazy(() => import("./pages/Plans"))
const Profile = lazy(() => import("./pages/Profile"))
const Import = lazy(() => import("./pages/Import"))
const StudyLoad = lazy(() => import("./pages/StudyLoad"))
const Export = lazy(() => import("./pages/Export"))

// Компонент-обертка для layout
const Layout = () => {
	return (
		<Box sx={{ 
			display: 'flex', 
			flexDirection: 'column', 
			height: '100vh',
			m: 0,
			p: 0,
		}}>
			<Navbar />
			<Box sx={{ 
				flexGrow: 1, 
				overflow: 'hidden',
				m: 0,
				p: 0,
			}}>
				<Suspense fallback={<div>Loading...</div>}>
					<Outlet />
				</Suspense>
			</Box>
		</Box>
	)
}

function App() {
	const router = createBrowserRouter(
		createRoutesFromElements(
			<Route element={<Layout />}>
				<Route path="/" element={<Navigate to="/plans" replace />} />
				<Route path='/profile' element={<Profile />} />
				<Route path='/plans' element={<Plans />} />
				<Route path='/plans/:curriculumId' element={<Plans />} />
				<Route path='/import' element={<Import />} />
				<Route path='/export' element={<Export />} />
				<Route path='/study-load' element={<StudyLoad />} />
			</Route>
		)
	);

	return (
		<SnackbarProvider maxSnack={3}>
			<RouterProvider router={router} />
		</SnackbarProvider>
	)
}

export default App;
