import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PDFGenerator {
    private final SimpleDateFormat dateFormater;
    private final ConsoleWriter consoleWriter;
    private final List<FileComparison> comparisonReportsList;
    private final String folder1;
    private final String folder2;
    private final UserData userData;


    public PDFGenerator(SimpleDateFormat dateFormater, ConsoleWriter consoleWriter, List<FileComparison> comparisonReportsList, String folder1, String folder2, UserData userData) {
        this.dateFormater = dateFormater;
        this.consoleWriter = consoleWriter;
        this.comparisonReportsList = comparisonReportsList;
        this.folder1 = folder1;
        this.folder2 = folder2;
        this.userData = userData;
    }

    public boolean getAssertionReport(){
        try {
            String pathToDesktop = System.getProperty("user.home") + File.separator + "Desktop/";
            if(new File(pathToDesktop + "/FileComparator").mkdirs()){
                consoleWriter.printInfo("FileComparator directory created on " + pathToDesktop);
            }else{
                consoleWriter.printInfo("FileComparator directory already exists on " + pathToDesktop);
            }
            Document document = new Document();
            document.setPageSize(PageSize.A4.rotate());
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pathToDesktop + "FileComparator/assertion_results_"+ timestamp + ".pdf"));
            consoleWriter.printInfo("Creating PDF file on " + pathToDesktop + " ...");
            consoleWriter.printInfo("Created PDF File: assertion_results_" + timestamp + ".pdf on " + pathToDesktop);
            document.open();
            setDocumentAttributes(document);
            generatePDFHeading(document);
            generateFileNamesComparisonTable(document);
            generateLastModifiedDateComparisonTable(document);
            generateSizeComparisonTable(document);
            generateFinalMatchTable(document);
            generatePDFFooter(document);
            document.close();
            writer.close();
            return true;
        }
        catch (DocumentException | FileNotFoundException e) {
            consoleWriter.replyWithError("Error occurred while generating report!");
        }
        return false;
    }

    private void setDocumentAttributes(Document document){
        document.addAuthor("FileComparator commanded by " + userData.getLoggedUser());
        document.addCreationDate();
        document.addTitle("Assertion Report " + dateFormater.format(new Date()));
        document.addSubject("File Comparison");
        document.addCreator("FileComparator");
    }

    private void generatePDFHeading(Document document) throws DocumentException {
        document.add(new Paragraph(("                                                                                 Assertion Report")));
        document.add(new Paragraph("\n\n"));
        document.add(new Paragraph("This Assertion Report was created by the comparison of the folders bellow, by FileComparator, running on " + userData.getComputerName() + "PC, by the User " + userData.getLoggedUser() + ".\n"));
        document.add(new Paragraph("\nFolders:\n  - " + folder1 + "\n  - " + folder2 + "\n"));
        document.add(new Paragraph("\n"));
    }

    private void generateFileNamesComparisonTable(Document document) throws DocumentException {
        document.add(new Paragraph("File Names Comparison:\n"));
        document.add(new Paragraph("\n"));
        PdfPTable namesComparisonTable = new PdfPTable(3);
        PdfPCell folder1FileName = new PdfPCell(new Paragraph("Folder 1: File Name"));
        PdfPCell folder2FileName = new PdfPCell(new Paragraph("Folder 2: File Name"));
        PdfPCell fileNamesMatch = new PdfPCell(new Paragraph("Match"));
        namesComparisonTable.addCell(folder1FileName);
        namesComparisonTable.addCell(folder2FileName);
        namesComparisonTable.addCell(fileNamesMatch);
        for(FileComparison fileComparison : comparisonReportsList){
            namesComparisonTable.addCell(new PdfPCell((new Paragraph(fileComparison.fileFromFolder1.getFileName()))));
            namesComparisonTable.addCell(new PdfPCell((new Paragraph(fileComparison.fileFromFolder2.getFileName()))));
            namesComparisonTable.addCell(new PdfPCell((new Paragraph(String.valueOf(fileComparison.fileNamesMatch)))));
        }
        document.add(namesComparisonTable);
        document.add(new Paragraph("\n"));
    }

    private void generateLastModifiedDateComparisonTable(Document document) throws DocumentException {
        document.add(new Paragraph("Last Modified Date Comparison:\n"));
        document.add(new Paragraph("\n"));
        PdfPTable modifiedDatesComparisonTable = new PdfPTable(3);
        PdfPCell folder1LastModified = new PdfPCell(new Paragraph("Folder 1: Last Modififed Date"));
        PdfPCell folder2LastModified = new PdfPCell(new Paragraph("Folder 2: Last Modified Date"));
        PdfPCell lastModifedDatesMatch = new PdfPCell(new Paragraph("Match"));
        modifiedDatesComparisonTable.addCell(folder1LastModified);
        modifiedDatesComparisonTable.addCell(folder2LastModified);
        modifiedDatesComparisonTable.addCell(lastModifedDatesMatch);
        for(FileComparison fileComparison : comparisonReportsList){
            modifiedDatesComparisonTable.addCell(new PdfPCell((new Paragraph(fileComparison.fileFromFolder1.getLastModifiedDate()))));
            modifiedDatesComparisonTable.addCell(new PdfPCell((new Paragraph(fileComparison.fileFromFolder2.getLastModifiedDate()))));
            modifiedDatesComparisonTable.addCell(new PdfPCell((new Paragraph(String.valueOf(fileComparison.fileLastModifiedDatesMatch)))));
        }
        document.add(modifiedDatesComparisonTable);
        document.add(new Paragraph("\n"));
    }

    private void generateSizeComparisonTable(Document document) throws DocumentException {
        document.add(new Paragraph("Size Comparison:\n"));
        document.add(new Paragraph("\n"));
        PdfPTable sizeComparisonTable = new PdfPTable(3);
        PdfPCell folder1FileSize = new PdfPCell(new Paragraph("Folder 1: File Size"));
        PdfPCell folder2FileSize = new PdfPCell(new Paragraph("Folder 2: FileSize"));
        PdfPCell fileSizesMatch = new PdfPCell(new Paragraph("Match"));
        sizeComparisonTable.addCell(folder1FileSize);
        sizeComparisonTable.addCell(folder2FileSize);
        sizeComparisonTable.addCell(fileSizesMatch);
        for(FileComparison fileComparison : comparisonReportsList){
            sizeComparisonTable.addCell(new PdfPCell((new Paragraph(fileComparison.fileFromFolder1.getLastModifiedDate()))));
            sizeComparisonTable.addCell(new PdfPCell((new Paragraph(fileComparison.fileFromFolder2.getLastModifiedDate()))));
            sizeComparisonTable.addCell(new PdfPCell((new Paragraph(String.valueOf(fileComparison.fileLastModifiedDatesMatch)))));
        }
        document.add(sizeComparisonTable);
        document.add(new Paragraph("\n"));
    }

    private void generateFinalMatchTable(Document document) throws DocumentException {
        document.add(new Paragraph("Comparison Results:\n"));
        document.add(new Paragraph("\n"));
        PdfPTable resultsTable = new PdfPTable(4);

        PdfPCell fileNamesMatch = new PdfPCell(new Paragraph("File Names Match"));
        PdfPCell lastModifedDatesMatch = new PdfPCell(new Paragraph("Last Modified Dates Match"));
        PdfPCell fileSizesMatch = new PdfPCell(new Paragraph("Sizes Match"));
        PdfPCell finalMatch = new PdfPCell(new Paragraph("Final Match"));
        resultsTable.addCell(fileNamesMatch);
        resultsTable.addCell(lastModifedDatesMatch);
        resultsTable.addCell(fileSizesMatch);
        resultsTable.addCell(finalMatch);
        for(FileComparison fileComparison : comparisonReportsList){
            resultsTable.addCell(new PdfPCell((new Paragraph(String.valueOf(fileComparison.fileNamesMatch)))));
            resultsTable.addCell(new PdfPCell((new Paragraph(String.valueOf(fileComparison.fileLastModifiedDatesMatch)))));
            resultsTable.addCell(new PdfPCell((new Paragraph(String.valueOf(fileComparison.fileSizesMatch)))));
            resultsTable.addCell(new PdfPCell((new Paragraph(String.valueOf(fileComparison.finalMatch)))));
        }
        document.add(resultsTable);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\nAs seen in the table above, the files on both folders " + ComparatorUtils.generateFinalDecision(comparisonReportsList) + "."));

    }

    private void generatePDFFooter(Document document) throws DocumentException {
        document.add(new Paragraph(("\n\n\n                                                                        Generated on: " + dateFormater.format(new Date()))));
    }
}
