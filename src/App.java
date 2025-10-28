import java.util.Locale;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        //Scanner scanner = new Scanner(System.in);
        System.out.println("Тестирование анализатора");
        System.out.println("Введите выражения вида: PROCEDURE <индификатор>[(формальные параметры)]");
        System.out.println("Для выхода введите 'exit'\n");
        //while (true){
            String input = "PROCEDURE SUBSTR(VAR S : CHAR);";
            System.out.println("Введите выражение:\n" + input);
            //String input = scanner.nextLine();
            //if(input.toLowerCase().equals("exit"))
            //    break;
            AnalyzerCode analyzerCode = new AnalyzerCode(input);
            Result result = analyzerCode.AnalyzerString();
            if(result.number == -1){
                System.out.println("Успех");
            }else {
                System.out.println("Ошибка: " + result.str + " в позиции: " + result.number);
            }
        //}
        //scanner.close();
    }

}