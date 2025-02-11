package csit.semit.studyplansrestart.service;


import csit.semit.studyplansrestart.dto.CreateSpecialtyDTO;
import csit.semit.studyplansrestart.entity.Specialty;
import csit.semit.studyplansrestart.repository.SpecialtyRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SpecialtyService {
    SpecialtyRepository specialtyRepository;
    ModelMapper modelMapper;

    public List<Specialty> allSpecialty() {
        return specialtyRepository.findAll();
    }

    public Specialty specialtyById(Long specialty_id) {
        return specialtyRepository.findById(specialty_id).orElseThrow(() -> new RuntimeException("wrong specialty_id"));
    }

    public Long findByName(String code, int number) {
        return specialtyRepository.findByCodeAndNumber(code, number).getId();
    }

    public Specialty create(CreateSpecialtyDTO specialtyDto) {
        Optional<Specialty> specialty = specialtyRepository.findByCodeAndName(specialtyDto.getCode(), specialtyDto.getName());
        return specialty.orElseGet(() -> specialtyRepository.save(modelMapper.map(specialtyDto, Specialty.class)));

    }

    public void deleteSpecialty(Long specialty_id) {
        Specialty specialty = specialtyRepository.findById(specialty_id).orElseThrow(
                () -> new RuntimeException("Discipline not found with id: " + specialty_id));
        specialtyRepository.deleteById(specialty.getId());
    }

    public Specialty updateSpecialty (CreateSpecialtyDTO specialtyDto, Long specialty_id) {
        Specialty specialty = modelMapper.map(specialtyDto, Specialty.class);
        if(specialty_id != null){
            Specialty oldSpecialty = specialtyRepository.findById(specialty_id).orElseThrow(() -> new RuntimeException("Wrong id"));
            BeanUtils.copyProperties(specialty,oldSpecialty);
        }
        return specialtyRepository.save(specialty);
    }
}
