package csit.semit.studyplansrestart.service.export;

import csit.semit.studyplansrestart.entity.Curriculum;
import csit.semit.studyplansrestart.service.CurriculumService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MainInformation {
    CurriculumService curriculumService;

    public void createSheet(Workbook workbook, Long curriculum_id) {
        Sheet mainSheet = workbook.createSheet("Основні дані");
        mainSheet.setColumnWidth(1, 652 * 256 / 7);
        mainSheet.setColumnWidth(0, 340 * 256 / 7);

        Row row = mainSheet.createRow(0);
        row.createCell(0).setCellValue("НАВЧАЛЬНИЙ ПЛАН №");
        row.createCell(1).setCellFormula("CONCATENATE(B4,\"-\",B7,B20,B2)");
        Curriculum curriculum = curriculumService.getById(curriculum_id);
        Row firstRow = mainSheet.createRow(1);
        firstRow.createCell(0).setCellValue("Форма навчання та інше ");
        firstRow.createCell(1).setCellValue("");

        addFormattedCommentToCell(workbook,mainSheet);

        Row secondRow = mainSheet.createRow(2);
        secondRow.createCell(0).setCellValue("Шифр інституту (факультету)");
        secondRow.createCell(1).setCellValue(320);
//        secondRow.createCell(1).setCellValue(curriculum.getSpecialty().getNumber());

        Row threeRow = mainSheet.createRow(3);
        threeRow.createCell(0).setCellValue("Шифр інституту (факультету)");
        threeRow.createCell(1).setCellValue("КН");
//        threeRow.createCell(1).setCellValue(curriculum.getSpecialty().getNumber());

        Row sixthRow = mainSheet.createRow(6);
        sixthRow.createCell(0).setCellValue("Номер освітньої програми");
        sixthRow.createCell(1).setCellValue(curriculum.getSpecialty().getNumber());
        Row sevenRow = mainSheet.createRow(7);
        sevenRow.createCell(0).setCellValue("Назва освітньої програми");
        sevenRow.createCell(1).setCellValue(curriculum.getSpecialty().getName());
        Row eightRow = mainSheet.createRow(8);
        eightRow.createCell(0).setCellValue("Шифр галузі знань");
        eightRow.createCell(1).setCellValue(12);
        Row nineRow = mainSheet.createRow(9);
        nineRow.createCell(0).setCellValue("Назва галузі");
        nineRow.createCell(1).setCellValue("Інформаційні технології");

        Row tenRow = mainSheet.createRow(10);
        tenRow.createCell(0).setCellValue("Шифр спеціальністі");
        tenRow.createCell(1).setCellValue(curriculum.getSpecialty().getCode());

        Row elevenRow = mainSheet.createRow(11);
        elevenRow.createCell(0).setCellValue("Назва спеціальністі");
        elevenRow.createCell(1).setCellValue(curriculum.getSpecialty().getCode_name());

        Row fourteenRow = mainSheet.createRow(14);
        fourteenRow.createCell(0).setCellValue("Рівень вищої освіти: ");
        fourteenRow.createCell(1).setCellValue("першого (бакалаврського) рівня");

        Row fifteenRow = mainSheet.createRow(15);
        fifteenRow.createCell(0).setCellValue("Кваліфікація:");
        fifteenRow.createCell(1).setCellValue("бакалавр з комп'ютерних наук ");

        Row nineteenRow = mainSheet.createRow(19);
        nineteenRow.createCell(0).setCellValue("Рік (останні 2 цифри)");
        nineteenRow.createCell(1).setCellValue(curriculum.getYear());


        Row twentyOneRow = mainSheet.createRow(21);
        twentyOneRow.createCell(0).setCellValue("Відповідальний за інформацію, телефон");
        twentyOneRow.createCell(1).setCellValue("");

        Row twentyThreeRow = mainSheet.createRow(24);
        twentyThreeRow.createCell(0).setCellValue("Форма Б1с-21  м1");
    }

    public static void addFormattedCommentToCell(Workbook workbook, Sheet mainSheet) {
        Drawing<?> drawing = mainSheet.createDrawingPatriarch();
        CreationHelper factory = workbook.getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(1);
        anchor.setRow1(1);

        Comment comment = drawing.createCellComment(anchor);
        RichTextString richText = factory.createRichTextString(
                "форма навчання: денна-не вказується, " +
                        "з - заочна, с - скорочена; д - дистанційна; " +
                        "і - іноземці; di - додатковий прийом; " +
                        "скорочена назва мови викладання (.е - англійська мова, .f - французький)"
        );
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);

        Font normalFont = workbook.createFont();
        normalFont.setBold(false);

        richText.applyFont(0, 22, boldFont);
        richText.applyFont(15, richText.length(), normalFont);

        comment.setString(richText);

//        comment.setFillColor(173, 216, 230);
        mainSheet.getRow(1).getCell(1).setCellComment(comment);
    }
}
