public class Result {
    private final int numberOfPosError;
    private final String nameError;


    public Result(int number, String srt) {
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