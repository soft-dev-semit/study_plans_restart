import { Box, Typography, Button } from "@mui/material";
import JSZip from "jszip";
import React, { useState, useRef, useEffect } from "react";

interface DirectoryPickerProps {
  onDirectorySelected: (directoryPath: string, files: File[]) => void;
}

const DirectoryPicker: React.FC<DirectoryPickerProps> = ({
  onDirectorySelected,
}) => {
  const [directoryPath, setDirectoryPath] = useState<string>('');
  const [files, setFiles] = useState<File[]>([]);
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (inputRef.current) {
      inputRef.current.setAttribute('webkitdirectory', 'true');
      inputRef.current.setAttribute('directory', 'true');
    }
  }, []);

  const handleDirectoryChange = (
    event: React.ChangeEvent<HTMLInputElement>
  ): void => {
    const selectedFiles = Array.from(event.target.files || []);
    if (selectedFiles.length > 0) {
      // Получаем имя директории из первого файла
      const fullPath = selectedFiles[0].webkitRelativePath;
      const directoryName = fullPath.split('/')[0];

      console.log('Selected directory:', directoryName);
      console.log('Total files:', selectedFiles.length);

      // Сохраняем все файлы для дальнейшей обработки
      setFiles(selectedFiles);
      setDirectoryPath(directoryName);

      // Передаем имя директории и все файлы в родительский компонент
      onDirectorySelected(directoryName, selectedFiles);
    }
  };

  return (
    <Box sx={{ textAlign: 'center', mt: 4 }}>
      <Typography
        variant='body1'
        gutterBottom
        align='center'
        color='text.secondary'
      >
        Завантаження директории
      </Typography>
      <input
        type='file'
        ref={inputRef}
        multiple
        onChange={handleDirectoryChange}
        style={{ display: 'none' }}
        id='directory-picker'
      />
      <label htmlFor='directory-picker'>
        <Button variant='contained' color='primary' component='span'>
          Выбрать директорию
        </Button>
      </label>
      {directoryPath && (
        <>
          <Typography
            variant='body2'
            align='center'
            color='text.secondary'
            sx={{ mt: 2 }}
          >
            Выбрана директория: {directoryPath}
          </Typography>
          <Typography variant='body2' align='center' color='text.secondary'>
            Количество файлов: {files.length}
          </Typography>
        </>
      )}
    </Box>
  );
};

export default DirectoryPicker;
