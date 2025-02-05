package csit.semit.studyplansrestart.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UploadDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	private long fileSize;
	private String fileName;
	private String uploadStatus;

}