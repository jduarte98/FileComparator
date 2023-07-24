public class FileComparison {
    RetrievedFile fileFromFolder1, fileFromFolder2;
    boolean fileNamesMatch, fileLastModifiedDatesMatch, fileSizesMatch, finalMatch;

    public FileComparison(RetrievedFile fileFromFolder1, RetrievedFile fileFromFolder2, boolean fileNamesMatch, boolean fileLastModifiedDatesMatch, boolean fileSizesMatch) {
        this.fileFromFolder1 = fileFromFolder1;
        this.fileFromFolder2 = fileFromFolder2;
        this.fileNamesMatch = fileNamesMatch;
        this.fileLastModifiedDatesMatch = fileLastModifiedDatesMatch;
        this.fileSizesMatch = fileSizesMatch;
        this.finalMatch = fillFinalMacth();
    }

    private boolean fillFinalMacth(){
        return this.fileNamesMatch && this.fileLastModifiedDatesMatch && this.fileSizesMatch;
    }

    public String getFileFromFolder1() {
        return fileFromFolder1.toString();
    }

    public String getFileFromFolder2() {
        return fileFromFolder2.toString();
    }

    public boolean isFileNamesMatch() {
        return fileNamesMatch;
    }

    public boolean isFileLastModifiedDatesMatch() {
        return fileLastModifiedDatesMatch;
    }

    public boolean isFileSizesMatch() {
        return fileSizesMatch;
    }

    private String resolveBooleans(boolean unresolvedBoolean){
        if (unresolvedBoolean){
            return "Yes";
        }
        return "No";
    }

    @Override
    public String toString() {
        return "Folder 1: " + getFileFromFolder1() + "\nFolder 2: " +getFileFromFolder2() +
                "\n Assertion:\n  File Names Match: " + resolveBooleans(fileNamesMatch) +
                "\n  Last Modified Dates Match: " + resolveBooleans(fileLastModifiedDatesMatch) +
                "\n  File Sizes Match: " + resolveBooleans(fileSizesMatch);
    }
}
