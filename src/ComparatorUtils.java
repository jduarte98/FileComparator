import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utils that are used in the comparison process
 */
public class ComparatorUtils {
    /**
     * Sets the default program date/time format.
     */
    private final SimpleDateFormat dateFormater;

    /**
     * Class Constructor
     * @param dateFormater Sets the default program date/time format.
     */
    public ComparatorUtils(SimpleDateFormat dateFormater) {
        this.dateFormater = dateFormater;
    }

    /**
     * Check the number of received arguments.
     * @param args Execution args.
     * @return True if the number of received elements is 2. False otherwise.
     */
    public boolean validateArgsSize(String[] args){
        return args.length == 2;
    }

    /**
     * Treats the received args. Retrives the args received and adds them to a list.
     * @param args Execution args.
     * @return List with the arguments received.
     */
    public List<String> digestFolderNames(String[] args){
        List<String> digestedArgs = new ArrayList<>();
        Collections.addAll(digestedArgs,args);
        return digestedArgs;
    }

    /**
     * Validates that the folders provided by the user actually exist.
     * @param folder1 Path to folder 1
     * @param folder2 Path to folder 2
     * @return True if both folders exist, False otherwise.
     */
    public boolean validateFoldersExistence(String folder1, String folder2){
        Path folder1Path = Paths.get(folder1), folder2Path = Paths.get(folder2);
        return Files.exists(folder1Path) && Files.exists(folder2Path);
    }

    /**
     * Retrieves all the files present in the specified folder.
     * @param folder Path to the folder.
     * @return List of the retrieved files.
     */
    public List<RetrievedFile> retriveFilesFromFolder(File folder){
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

    /**
     * Lists the files present on the provided folders.
     * @param filesFromFolder1 List of files from folder 1
     * @param filesFromFolder2 List of files from folder 2
     * @return Formated String containing the list of all the files from both folders.
     */
    public String listFilesFromFolders(List<RetrievedFile> filesFromFolder1, List<RetrievedFile> filesFromFolder2){
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

    /**
     * Sorts the files present in the given list alphabetically from A to Z.
     * @param retrievedFileList List of retrieved files.
     * @return True if the sort was successfull. False otherwise.
     */
    public boolean listSorter(List<RetrievedFile> retrievedFileList) {
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

    /**
     * Conparate the files from the given folders.
     * @param filesFromFolder1 List of files from folder 1
     * @param filesFromFolder2 List of files from folder 2
     * @return List containing all the comparisons performed for the files on the given folders.
     */
    public List<FileComparison> comparateFiles(List<RetrievedFile> filesFromFolder1, List<RetrievedFile> filesFromFolder2){
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

    /**
     * Generates a String explaining whether the files Match or Not.
     * @param comparisonList List containing all the comparisons performed.
     * @return "match" if all the match parameters (file names, last modified dates and file sizes) match; "do not match" otherwise.
     */
    public static String generateFinalDecision(List<FileComparison> comparisonList){
        for(FileComparison file : comparisonList){
            if(!file.finalMatch){
                return "do not match";
            }
        }
        return "match";
    }
}
