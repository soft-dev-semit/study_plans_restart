package csit.semit.studyplansrestart.service;

import csit.semit.studyplansrestart.dto.CreateDisciplineDTO;
import csit.semit.studyplansrestart.dto.DisciplineDTO;
import csit.semit.studyplansrestart.entity.Discipline;
import csit.semit.studyplansrestart.repository.DisciplineRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
@AllArgsConstructor
public class DisciplineService {

    DisciplineRepository disciplineRepository;
    ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(DisciplineService.class);

    public Discipline findById(Long discipline_id) {
        return disciplineRepository.findById(discipline_id).orElseThrow(
                ()-> new RuntimeException("Discipline not found with id: " + discipline_id));
    }

    public Long create(CreateDisciplineDTO disciplineDTO) {
        return disciplineRepository.save(modelMapper.map(disciplineDTO, Discipline.class)).getId();
    }

    public void deleteDiscipline(Long discipline_id) {
        Discipline discipline = disciplineRepository.findById(discipline_id).orElseThrow(
                () -> new RuntimeException("Discipline not found with id: " + discipline_id));
        disciplineRepository.deleteById(discipline.getId());
    }

    public Discipline updateDiscipline(Long discipline_id, CreateDisciplineDTO disciplineDTO){
        Discipline discipline = modelMapper.map(disciplineDTO, Discipline.class);
        if(discipline_id != null){
            Discipline oldDiscipline = disciplineRepository.findById(discipline_id).orElseThrow(() -> new RuntimeException("Wrong id"));
            BeanUtils.copyProperties(discipline,oldDiscipline);
        }
        return disciplineRepository.save(discipline);
    }

    public List<DisciplineDTO> allDiscipline() {
            List<Discipline> disciplines = disciplineRepository.findAll();
            Type setOfDTOsType = new TypeToken<List<DisciplineDTO>>(){}.getType();
            return modelMapper.map(disciplines,setOfDTOsType);
    }
}
