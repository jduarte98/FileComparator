import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;


public class Main {
    private  static UserData userData;
    private static ConsoleWriter consoleWriter;
    private static ComparatorUtils comparatorUtils;

    public static void main(String[] args) {
        userData = new UserData(System.getProperty("user.name"));
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        consoleWriter = new ConsoleWriter(dateFormater, userData);
        comparatorUtils = new ComparatorUtils(dateFormater);
        performFileComparison(args);
    }

    private static void performFileComparison(String[] args){
        String folder1 = "", folder2 = "";
        List<RetrievedFile> filesFromFolder1, filesFromFolder2;
        consoleWriter.banner();
        consoleWriter.printInfo("Validating Number of arguments received ...");
        if(comparatorUtils.validateArgsSize(args)){
            consoleWriter.printInfo("Number of arguments received is valid!");
            folder1= comparatorUtils.digestFolderNames(args).get(0);
            folder2 = comparatorUtils.digestFolderNames(args).get(1);
            consoleWriter.printInfo("\n  Folder 1: " + folder1 + "\n  Folder 2: " + folder2);
            consoleWriter.printInfo("Validating existence of provided folders ...");
            if(comparatorUtils.validateFoldersExistence(folder2,folder1)) {
                consoleWriter.printInfo("Provided folders exist!");
                consoleWriter.printInfo("Retrieving files from the specified folders ...");
                filesFromFolder1 = comparatorUtils.retriveFilesFromFolder(new File(folder1));
                filesFromFolder2 = comparatorUtils.retriveFilesFromFolder(new File(folder2));
                if (filesFromFolder1 != null && filesFromFolder2 != null && filesFromFolder1.size() > 0 && filesFromFolder2.size() > 0) {
                    System.out.println(comparatorUtils.listFilesFromFolders(filesFromFolder1, filesFromFolder2));
                    consoleWriter.printInfo("Files Successfully retrieved!");
                    consoleWriter.printInfo("Organizing files alphabetically ...");
                    if (comparatorUtils.listSorter(filesFromFolder1) && comparatorUtils.listSorter(filesFromFolder2)) {
                        consoleWriter.printInfo("Files successfully sorted!");
                        System.out.println(comparatorUtils.listFilesFromFolders(filesFromFolder1, filesFromFolder2));
                        consoleWriter.printInfo("Both folder have " + filesFromFolder1.size() + " items.");
                        consoleWriter.printInfo("Running File Assertion ...");
                        List<FileComparison> comparisonResultsList = comparatorUtils.comparateFiles(filesFromFolder1, filesFromFolder2);
                        if(comparisonResultsList != null){
                            consoleWriter.printInfo("File Assertion done!");
                            consoleWriter.printInfo("Generating Report ...");
                            if(new PDFGenerator(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), consoleWriter, comparisonResultsList, folder1, folder2, userData ).getAssertionReport()){
                                consoleWriter.replyWithSuccess();
                            }else{
                                consoleWriter.replyWithError("Error occurred while generating report!");
                            }
                        }else{
                            consoleWriter.replyWithError("An error occurred in the file comparisson!");
                        }
                    } else {
                        consoleWriter.replyWithError("An error happened while sorting the files!");
                    }
                } else {
                    consoleWriter.replyWithError("Unable to retrieve the files from the folders!");
                }

            }else{
                consoleWriter.replyWithError("One or more of the provided folders do not exist! Terminating execution!");
            }
        }else{
            consoleWriter.replyWithError("Can only accept 2 Args.");
        }
    }






}