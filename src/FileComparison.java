/**
 * FileComparison Model
 */
public class FileComparison {
    /**
     * RetrievedFile from folder 1
     */
    public final RetrievedFile fileFromFolder1;

    /**
     * RetrievedFile from folder 2
     */
    public final RetrievedFile fileFromFolder2;

    /**
     * Comparison results on File Names
     */
    public boolean fileNamesMatch;

    /**
     * Comparison results on File Last Modified Dates
     */
    public boolean fileLastModifiedDatesMatch;

    /**
     * Comparison results on File Sizes
     */
    public boolean fileSizesMatch;

    /**
     * Comparison of the file names, file last modified dates and file sizes comparison results.
     */
    public boolean finalMatch;

    /**
     * Class Constructor
     * @param fileFromFolder1 Retrived file from folder 1.
     * @param fileFromFolder2 Retrieved file from folder 2.
     * @param fileNamesMatch Results of the comparison of the file names.
     * @param fileLastModifiedDatesMatch  Results of the comparison of the last modified dates.
     * @param fileSizesMatch Results of the comparison of the file sizes.
     */
    public FileComparison(RetrievedFile fileFromFolder1, RetrievedFile fileFromFolder2, boolean fileNamesMatch, boolean fileLastModifiedDatesMatch, boolean fileSizesMatch) {
        this.fileFromFolder1 = fileFromFolder1;
        this.fileFromFolder2 = fileFromFolder2;
        this.fileNamesMatch = fileNamesMatch;
        this.fileLastModifiedDatesMatch = fileLastModifiedDatesMatch;
        this.fileSizesMatch = fileSizesMatch;
        this.finalMatch = fillFinalMacth();
    }

    /**
     * Compares the results of the comparisons of the file names, last modified dates and file sizes and fills the finalMatch field of the comparison.
     * @return True if all the results are True. False otherwise.
     */
    private boolean fillFinalMacth(){
        return this.fileNamesMatch && this.fileLastModifiedDatesMatch && this.fileSizesMatch;
    }

    /**
     * Get the File from folder 1 as a String.
     * @return File from folder 1 as a string.
     */
    public String getFileFromFolder1() {
        return fileFromFolder1.toString();
    }

    /**
     * Get the File from folder 2 as a String.
     * @return File from folder 2 as a String.
     */
    public String getFileFromFolder2() {
        return fileFromFolder2.toString();
    }

    /**
     * Simplifies the reading of the results of the comparison results.
     * @param unresolvedBoolean Boolean value.
     * @return Yes if the received boolean is true. No otherwise
     */
    private String resolveBooleans(boolean unresolvedBoolean){
        if (unresolvedBoolean){
            return "Yes";
        }
        return "No";
    }

    /**
     * Prints the class as a String.
     * @return Class as a String.
     */
    @Override
    public String toString() {
        return "Folder 1: " + getFileFromFolder1() + "\nFolder 2: " +getFileFromFolder2() +
                "\n Assertion:\n  File Names Match: " + resolveBooleans(fileNamesMatch) +
                "\n  Last Modified Dates Match: " + resolveBooleans(fileLastModifiedDatesMatch) +
                "\n  File Sizes Match: " + resolveBooleans(fileSizesMatch);
    }
}
