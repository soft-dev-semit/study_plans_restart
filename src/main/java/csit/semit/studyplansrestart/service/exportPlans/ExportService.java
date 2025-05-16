package csit.semit.studyplansrestart.service.exportPlans;

import csit.semit.studyplansrestart.config.Utils;
import java.io.FileOutputStream;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExportService {
  Workbook workbook;

  @NonNull PlansNPCreate plansNPCreate;

  @NonNull MainInformation mainInformation;
  @NonNull Title title;

  public void exportExcel(long curriculum_id) {
    workbook = new XSSFWorkbook();
    Sheet plansSheet = PlansNPCreate.createHeader(workbook);
    mainInformation.createSheet(workbook, curriculum_id);
    plansNPCreate.fillCell(plansSheet, curriculum_id);
    title.titleSheet(workbook);
    Utils.setStyle(workbook);
    try (FileOutputStream fileOut = new FileOutputStream("example.xlsx")) {
      workbook.write(fileOut);
      workbook.close();
    } catch (IOException e) {
      e.getMessage();
    }
  }
}
