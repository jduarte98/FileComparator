import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class ComparatorUtils {
    private SimpleDateFormat dateFormater;

    public ComparatorUtils(SimpleDateFormat dateFormater) {
        this.dateFormater = dateFormater;
    }

    public boolean validateArgsSize(String[] args){
        return args.length == 2;
    }

    public List<String> digestFolderNames(String[] args){
        List<String> digestedArgs = new ArrayList<>();
        Collections.addAll(digestedArgs,args);
        return digestedArgs;
    }

    public boolean validateFoldersExistence(String folder1, String folder2){
        Path folder1Path = Paths.get(folder1), folder2Path = Paths.get(folder2);
        return Files.exists(folder1Path) && Files.exists(folder2Path);
    }

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

    public static String generateFinalDecision(List<FileComparison> comparisonList){
        for(FileComparison file : comparisonList){
            if(!file.finalMatch){
                return "do not match";
            }
        }
        return "match";
    }
}
