package csit.semit.studyplansrestart.dto.request;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileRequest {
    private MultipartFile file;
} 