import java.util.HashMap;
import java.util.List;

public class Result {
    private final int numberOfPosError;
    private final String nameError;
    HashMap<String,String> names = new HashMap<>();


    public Result(int number, String srt, int lenght) {
        this.numberOfPosError = number;
        this.nameError = srt;
    }



    public int getNumberOfPosError() {
        return numberOfPosError;
    }

    public String getNameError() {
        return nameError;
    }
}