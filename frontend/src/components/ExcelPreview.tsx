import React, {useState} from "react";
import {Box, Paper, Tab, Table, TableBody, TableCell, TableContainer, TableRow, Tabs, Typography} from "@mui/material";

export const ExcelPreview = ({ content }: { content: any }) => {
    const [activeSheet, setActiveSheet] = useState<string>(content.sheetNames[0])

    const handleSheetChange = (event: React.SyntheticEvent, newSheet: string) => {
        setActiveSheet(newSheet)
    }

    return (
        <Box >
            <Tabs
                value={activeSheet}
                onChange={handleSheetChange}
                variant="scrollable"
                scrollButtons="auto"
            >
                {content.sheetNames.map((name: string) => (
                    <Tab key={name} label={name} value={name} />
                ))}
            </Tabs>

            <TableContainer component={Paper} sx={{ height: '85%', mt: 2}}>
                <Table size="small" stickyHeader>
                    <TableBody>
                        {content.sheets[activeSheet].slice(0, 100).map((row: any[], rowIndex: number) => (
                            <TableRow key={rowIndex}>
                                {row.map((cell: any, cellIndex: number) => (
                                    <TableCell key={cellIndex}>
                                        {cell !== null && cell !== undefined ? cell.toString() : ''}
                                    </TableCell>
                                ))}
                            </TableRow>
                        ))}
                        {content.sheets[activeSheet].length > 100 && (
                            <TableRow>
                                <TableCell colSpan={content.sheets[activeSheet][0]?.length || 1}>
                                    <Typography variant="body2" align="center">
                                        Показаны первые 100 строк из {content.sheets[activeSheet].length}
                                    </Typography>
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    )
}