package csit.semit.studyplansrestart.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperCongfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
