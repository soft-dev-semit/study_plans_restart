package csit.semit.studyplansrestart.service.exportPlans;

import csit.semit.studyplansrestart.config.Utils;
import csit.semit.studyplansrestart.dto.returnData.CourseInfo;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExportService {
  Workbook workbook;

  @NonNull PlansNPCreate plansNPCreate;
  @NonNull MainInformation mainInformation;
  @NonNull Title title;

  private static final Logger logger = LoggerFactory.getLogger(ExportService.class);

  public void exportExcel(long curriculum_id) {
    workbook = new XSSFWorkbook();
    Sheet plansSheet = PlansNPCreate.createHeader(workbook);
    mainInformation.createSheet(workbook, curriculum_id);
    plansNPCreate.fillCell(plansSheet, curriculum_id);
    title.titleSheet(workbook);
    Utils.setStyle(workbook, "Plans NP");
    try (FileOutputStream fileOut = new FileOutputStream("example.xlsx")) {
      workbook.write(fileOut);
      workbook.close();
    } catch (IOException e) {
      e.getMessage();
    }
  }

  public void exportStudyLoad(Map<String, List<CourseInfo>> courseInfoMap, OutputStream out)
      throws IOException {
    workbook = new XSSFWorkbook();
    for (Map.Entry<String, List<CourseInfo>> courseInfo : courseInfoMap.entrySet()) {
      if (courseInfo.getKey().equals("autumn")) {
        PlansNPCreate.createSheetBySeason(workbook, courseInfo.getValue(), "Осінь");
        Utils.setStyleStudyLoad(workbook, "Осінь");
      } else {
        PlansNPCreate.createSheetBySeason(workbook, courseInfo.getValue(), "Весна");
        Utils.setStyleStudyLoad(workbook, "Весна");
      }
    }
    workbook.write(out);
    logger.info("Finish");
    workbook.close();
  }
}
