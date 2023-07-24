import java.io.File;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.ListItem;

public class Main {
    private static final SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String loggedUser = System.getProperty("user.name");
    private static String machineName = "";
    //color cole info and error
    //concatenate date and time in souts


    public static void main(String[] args) {
        String folder1 = "", folder2 = "";
        List<RetrievedFile> filesFromFolder1, filesFromFolder2;
        banner();
        printInfo("Validating Number of arguments received ...");
        if(validateArgsSize(args)){
            printInfo("Number of arguments received is valid!");
            folder1= digestFolderNames(args).get(0);
            folder2 = digestFolderNames(args).get(1);
            printInfo("\n  Folder 1: " + folder1 + "\n  Folder 2: " + folder2);
            printInfo("Validating existence of provided folders ...");
            if(validateFoldersExistence(folder2,folder1)) {
                printInfo("Provided folders exist!");
                printInfo("Retrieving files from the specified folders ...");
                filesFromFolder1 = retriveFilesFromFolder(new File(folder1));
                filesFromFolder2 = retriveFilesFromFolder(new File(folder2));
                if (filesFromFolder1 != null && filesFromFolder2 != null && filesFromFolder1.size() > 0 && filesFromFolder2.size() > 0) {
                    System.out.println(listFilesFromFolders(filesFromFolder1, filesFromFolder2));
                    printInfo("Files Successfully retrieved!");
                    printInfo("Organizing files alphabetically ...");
                    if (listSorter(filesFromFolder1) && listSorter(filesFromFolder2)) {
                        printInfo("Files successfully sorted!");
                        System.out.println(listFilesFromFolders(filesFromFolder1, filesFromFolder2));
                        printInfo("Both folder have " + filesFromFolder1.size() + " items.");
                        printInfo("Running File Assertion ...");
                        List<FileComparison> comparisonResultsList = comparateFiles(filesFromFolder1, filesFromFolder2);
                        if(comparisonResultsList != null){
                            printInfo("File Assertion done!");
                            printInfo("Generating Report ...");
                            // generate assertion report kinda pdf !!!!
                            if(generateAssertionReport(comparisonResultsList, folder1, folder2)){
                                // success
                                System.out.println("SUCCESS: File Assertion Report Generated! File Comparisson Terminated! Closinf Comparator ...");
                                System.exit(0);
                            }else{
                                replyWithError("Error occurred while generating report!");
                            }
                        }else{
                            replyWithError("An error occurred in the file comparisson!");
                        }
                    } else {
                        replyWithError("An error happened while sorting the files!");
                    }
                } else {
                    replyWithError("Unable to retrieve the files from the folders!");
                }

            }else{
                replyWithError("One or more of the provided folders do not exist! Terminating execution!");
            }
        }else{
            replyWithError("Can only accept 2 Args.");
        }
    }

    private static void getComputerName(){
        try{
            machineName = InetAddress.getLocalHost().getHostName();
        }catch (Exception e){
            machineName = "-";
        }
    }

    private static void banner(){
        System.out.println("\n\u001B[42m### FileComparer (c) João Duarte ###\u001B[0m\n");
        getComputerName();
        System.out.println(">>> Computer: " + machineName + " <<<");
        System.out.println(">>> \u001B[32mWelcome, " + loggedUser + "\u001B[0m <<<\n");
    }

    private static void replyWithError(String errorMessage){
        System.out.println("[" + dateFormater.format(new Date()) + "] \u001B[31mERROR:\u001B[0m " + errorMessage + " \u001B[31mTerminating Execution ...\u001B[0m");
        System.exit(0);
    }

    private static void printInfo(String message){
        System.out.println("[" + dateFormater.format(new Date()) + "] \u001B[34mINFO:\u001B[0m " + message);
    }

    private static boolean validateArgsSize(String[] args){
        return args.length == 2;
    }

    private static List<String> digestFolderNames(String[] args){
        List<String> digestedArgs = new ArrayList<>();
        Collections.addAll(digestedArgs,args);
        return digestedArgs;
    }

    private static boolean validateFoldersExistence(String folder1, String folder2){
        Path folder1Path = Paths.get(folder1), folder2Path = Paths.get(folder2);
        return Files.exists(folder1Path) && Files.exists(folder2Path);
    }

    private static List<RetrievedFile> retriveFilesFromFolder(File folder){
        try{
            List<RetrievedFile> retrievedFiles = new ArrayList<>();
            for(File file : Objects.requireNonNull(folder.listFiles())){
                retrievedFiles.add(new RetrievedFile(file.getName(), dateFormater.format(file.lastModified()), (double)(file.length() / 1024)));
            }
            return retrievedFiles;
        }catch (Exception e){
            return null;
        }
    }

    private static String listFilesFromFolders(List<RetrievedFile> filesFromFolder1, List<RetrievedFile> filesFromFolder2){
        StringBuilder filesFolder1 = new StringBuilder();
        StringBuilder filesFolder2 = new StringBuilder();
        for(RetrievedFile file : filesFromFolder1){
            filesFolder1.append("  \nFile:\n    Name = ").append(file.getFileName()).append("\n    Last Modified: ").append(file.getLastModifiedDate()).append("\n    Size: ").append(file.getFileSize()).append(" kb\n");
        }
        for(RetrievedFile file : filesFromFolder2){
            filesFolder2.append("  \nFile:\n    Name = ").append(file.getFileName()).append("\n    Last Modified: ").append(file.getLastModifiedDate()).append("\n    Size: ").append(file.getFileSize()).append(" kb\n");
        }
        return "\n--------\nFolder 1\n--------\n" + filesFolder1 + "\n--------\nFolder 2\n--------\n" + filesFolder2;
    }

    private static boolean listSorter(List<RetrievedFile> retrievedFileList) {
        try {
            retrievedFileList.sort(new Comparator<RetrievedFile>() {
                @Override
                public int compare(final RetrievedFile retrievedFile1, final RetrievedFile retrievedFile2) {
                    return retrievedFile1.getFileName().compareTo(retrievedFile2.getFileName());
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static List<FileComparison> comparateFiles(List<RetrievedFile> filesFromFolder1, List<RetrievedFile> filesFromFolder2){
        try{
            int i;
            List<FileComparison> comparisonList = new ArrayList<>();
            for(i = 0; i < filesFromFolder1.size(); i++){
                comparisonList.add(new FileComparison(filesFromFolder1.get(i), filesFromFolder2.get(i), Objects.equals(filesFromFolder1.get(i).getFileName(), filesFromFolder2.get(i).getFileName()), Objects.equals(filesFromFolder1.get(i).getLastModifiedDate(), filesFromFolder2.get(i).getLastModifiedDate()), filesFromFolder1.get(i).getFileSize() == filesFromFolder2.get(i).getFileSize()));
            }
            return comparisonList;
        }catch (Exception e){
            return null;
        }
    }

    private static boolean generateAssertionReport(List<FileComparison> comparisonReportsList, String folder1, String folder2){
        try {
            String pathToDesktop = System.getProperty("user.home") + File.separator + "Desktop/";
            if(new File(pathToDesktop + "/FileComparator").mkdirs()){
                printInfo("FileComparator directory created on " + pathToDesktop);
            }else{
                printInfo("FileComparator directory already exists on " + pathToDesktop);
            }
            Document document = new Document();
            document.setPageSize(PageSize.A4.rotate());
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pathToDesktop + "FileComparator/assertion_results_"+ timestamp + ".pdf"));
            printInfo("Creating PDF file on " + pathToDesktop + " ...");
            printInfo("Created PDF File: assertion_results_" + timestamp + ".pdf on " + pathToDesktop);
            document.open();
            document.addAuthor("FileComparator commanded by " + loggedUser);
            document.addCreationDate();
            document.addTitle("Assertion Report " + dateFormater.format(new Date()));
            document.addSubject("File Comparison");
            document.addCreator("FileComparator");
            document.add(new Paragraph(("                                                                                 Assertion Report " + dateFormater.format(new Date()))));
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("This Assertion Report was created by the comparison of the folders bellow, by FileComparator, running on " + machineName + "PC, by the User " + loggedUser + "."));
            document.add(new Paragraph("Folders:\n  - " + folder1 + "\n  - " + folder2 + "\n"));
            document.add(new Paragraph("\n"));
            //Create Table object, Here 4 specify the no. of columns
            PdfPTable pdfPTable = new PdfPTable(10);

            //Create cells
            PdfPCell folder1FileName = new PdfPCell(new Paragraph("Folder 1: File Name"));
            PdfPCell folder2FileName = new PdfPCell(new Paragraph("Folder 2: File Name"));
            PdfPCell fileNamesMatch = new PdfPCell(new Paragraph("Match"));
            PdfPCell folder1LastModified = new PdfPCell(new Paragraph("Folder 1: Last Modififed Date"));
            PdfPCell folder2LastModified = new PdfPCell(new Paragraph("Folder 2: Last Modified Date"));
            PdfPCell lastModifedDatesMatch = new PdfPCell(new Paragraph("Match"));
            PdfPCell folder1FileSize = new PdfPCell(new Paragraph("Folder 1: File Size"));
            PdfPCell folder2FileSize = new PdfPCell(new Paragraph("Folder 2: FileSize"));
            PdfPCell fileSizesMatch = new PdfPCell(new Paragraph("Match"));
            PdfPCell finalMatch = new PdfPCell(new Paragraph("Final Match"));

            pdfPTable.addCell(folder1FileName);
            pdfPTable.addCell(folder2FileName);
            pdfPTable.addCell(fileNamesMatch);
            pdfPTable.addCell(folder1LastModified);
            pdfPTable.addCell(folder2LastModified);
            pdfPTable.addCell(lastModifedDatesMatch);
            pdfPTable.addCell(folder1FileSize);
            pdfPTable.addCell(folder2FileSize);
            pdfPTable.addCell(fileSizesMatch);
            pdfPTable.addCell(finalMatch);

            for(FileComparison fileComparison : comparisonReportsList){
                pdfPTable.addCell(new PdfPCell((new Paragraph(fileComparison.fileFromFolder1.getFileName()))));
                pdfPTable.addCell(new PdfPCell((new Paragraph(fileComparison.fileFromFolder2.getFileName()))));
                pdfPTable.addCell(new PdfPCell((new Paragraph(String.valueOf(fileComparison.fileNamesMatch)))));
                pdfPTable.addCell(new PdfPCell((new Paragraph(fileComparison.fileFromFolder1.getLastModifiedDate()))));
                pdfPTable.addCell(new PdfPCell((new Paragraph(fileComparison.fileFromFolder2.getLastModifiedDate()))));
                pdfPTable.addCell(new PdfPCell((new Paragraph(String.valueOf(fileComparison.fileLastModifiedDatesMatch)))));
                pdfPTable.addCell(new PdfPCell((new Paragraph(String.valueOf(fileComparison.fileFromFolder1.getFileSize())))));
                pdfPTable.addCell(new PdfPCell((new Paragraph(String.valueOf(fileComparison.fileFromFolder2.getFileSize())))));
                pdfPTable.addCell(new PdfPCell((new Paragraph(String.valueOf(fileComparison.fileSizesMatch)))));
                pdfPTable.addCell(new PdfPCell((new Paragraph(String.valueOf(fileComparison.finalMatch)))));
            }


            //Add content to the document using Table objects.
            document.add(pdfPTable);

            document.close();
            writer.close();
            return true;
        }
        catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}