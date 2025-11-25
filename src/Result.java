import java.util.LinkedHashMap;

public class Result {
    private final int numberOfPosError;
    private final String nameError;
    LinkedHashMap<String,String> names = new LinkedHashMap<>();


    public Result(int number, String srt, LinkedHashMap<String,String> names) {
        this.numberOfPosError = number;
        this.nameError = srt;
        this.names = names;
    }



    public int getNumberOfPosError() {
        return numberOfPosError;
    }

    public String getNameError() {
        return nameError;
    }

    public LinkedHashMap<String, String> getNames() {
        return names;
    }
}