package csit.semit.studyplansrestart.service;

import csit.semit.studyplansrestart.dto.create.CreateDisciplineDTO;
import csit.semit.studyplansrestart.dto.returnData.DisciplineDTO;
import csit.semit.studyplansrestart.entity.Discipline;
import csit.semit.studyplansrestart.repository.DisciplineRepository;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DisciplineService {

  DisciplineRepository disciplineRepository;
  ModelMapper modelMapper;

  public Discipline findById(Long discipline_id) {
    return disciplineRepository
        .findById(discipline_id)
        .orElseThrow(() -> new RuntimeException("Discipline not found with id: " + discipline_id));
  }

  public Discipline create(CreateDisciplineDTO disciplineDTO) {
    Optional<Discipline> discipline =
        disciplineRepository.findByNameAndShortName(disciplineDTO.name, disciplineDTO.shortName);
    return discipline.orElseGet(
        () -> disciplineRepository.save(modelMapper.map(disciplineDTO, Discipline.class)));
  }

  public void deleteDiscipline(Long discipline_id) {
    Discipline discipline =
        disciplineRepository
            .findById(discipline_id)
            .orElseThrow(
                () -> new RuntimeException("Discipline not found with id: " + discipline_id));
    disciplineRepository.deleteById(discipline.getId());
  }

  @Transactional
  public void updateDiscipline(List<DisciplineDTO> disciplineList) {
    if (!disciplineList.isEmpty()) {
      for (DisciplineDTO disciplineDTO : disciplineList) {
        Discipline oldDiscipline =
            disciplineRepository
                .findById(disciplineDTO.getId())
                .orElseThrow(() -> new RuntimeException("Wrong id"));

        if (disciplineDTO.getName().isEmpty()) {
          oldDiscipline.setName(disciplineDTO.getName());
        }
        if (disciplineDTO.getShortName().isEmpty()) {
          oldDiscipline.setShortName(disciplineDTO.getShortName());
        }

        disciplineRepository.save(oldDiscipline);
      }
    }
  }

  public List<DisciplineDTO> allDiscipline() {
    List<Discipline> disciplines = disciplineRepository.findAll();
    Type setOfDTOType = new TypeToken<List<DisciplineDTO>>() {}.getType();
    return modelMapper.map(disciplines, setOfDTOType);
  }
}
