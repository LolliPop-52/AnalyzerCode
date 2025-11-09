import java.util.Locale;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=======================Тестирование анализатора=======================");
        System.out.println("Выполнил студент группы 6202-020302D Иваненко Никита.");
        System.out.println("Вариант №12. Анализатор языка Turbo Pascal.");
        System.out.println("Введите выражения вида: PROCEDURE <индификатор>[(формальные параметры)];");
        System.out.println("Для выхода введите 'exit'.");
        System.out.println("======================================================================");
        while (true){
            System.out.println("Введите выражение:");
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("exit"))
                break;
            AnalyzerCode analyzerCode = new AnalyzerCode(input);
            Result result = analyzerCode.AnalyzerString();
            if(result.number == -1){
                System.out.println("Успех \n");
            }else {
                System.out.println(" ".repeat(result.number++) + "^");
                System.out.println("Ошибка: \"" + result.str + "\" в позиции: " + result.number + "\n");
            }
        }
        scanner.close();
    }
}