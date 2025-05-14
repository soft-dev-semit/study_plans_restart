package csit.semit.studyplansrestart.service;

import csit.semit.studyplansrestart.dto.create.SpecializedDisciplinesPackageDTO;
import csit.semit.studyplansrestart.entity.SpecializedDisciplinesPackage;
import csit.semit.studyplansrestart.repository.SpecializedDisciplinesPackageRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SpecializedDisciplinesPackageService {
  SpecializedDisciplinesPackageRepository packageRepository;
  ModelMapper modelMapper;

  public SpecializedDisciplinesPackage create(SpecializedDisciplinesPackageDTO packageDTO) {
    Optional<SpecializedDisciplinesPackage> packageDiscipline =
        packageRepository.findByNameOfPackageAndIndexOfDiscipline(
            packageDTO.getNameOfPackage(), packageDTO.getIndexOfDiscipline());

    return packageDiscipline.orElseGet(
        () ->
            packageRepository.save(
                modelMapper.map(packageDTO, SpecializedDisciplinesPackage.class)));
  }
}
