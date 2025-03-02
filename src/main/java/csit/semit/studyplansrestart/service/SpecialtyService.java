package csit.semit.studyplansrestart.service;


import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import csit.semit.studyplansrestart.dto.create.CreateSpecialtyDTO;
import csit.semit.studyplansrestart.entity.Specialities;
import csit.semit.studyplansrestart.repository.SpecialtyRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SpecialtyService {
    SpecialtyRepository specialtyRepository;
    ModelMapper modelMapper;

    public List<Specialities> allSpecialty() {
        return specialtyRepository.findAll();
    }

    public Specialities specialtyById(Long specialty_id) {
        return specialtyRepository.findById(specialty_id).orElseThrow(() -> new RuntimeException("wrong specialty_id"));
    }

    public Long findByName(String code, int number) {
        return specialtyRepository.findByCodeAndNumber(code, number).getId();
    }

    public Specialities create(CreateSpecialtyDTO specialtyDto) {
        Optional<Specialities> specialty = specialtyRepository.findByCodeAndName(specialtyDto.getCode(), specialtyDto.getName());
        return specialty.orElseGet(() -> specialtyRepository.save(modelMapper.map(specialtyDto, Specialities.class)));

    }

    public void deleteSpecialty(Long specialty_id) {
        Specialities specialty = specialtyRepository.findById(specialty_id).orElseThrow(
                () -> new RuntimeException("Discipline not found with id: " + specialty_id));
        specialtyRepository.deleteById(specialty.getId());
    }

    public Specialities updateSpecialty (CreateSpecialtyDTO specialtyDto, Long specialty_id) {
        Specialities specialty = modelMapper.map(specialtyDto, Specialities.class);
        if(specialty_id != null){
            Specialities oldSpecialty = specialtyRepository.findById(specialty_id).orElseThrow(() -> new RuntimeException("Wrong id"));
            BeanUtils.copyProperties(specialty,oldSpecialty);
        }
        return specialtyRepository.save(specialty);
    }
}
