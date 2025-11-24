import java.awt.*;
import java.util.HashMap;

public class AnalyzerCode {

    public AnalyzerCode(String text){
        str = text.toLowerCase();
        strLen = str.length();
        nowPos = 0;
        state = State.S;
    }
    boolean indFlag;
    String str;
    int nowPos;
    int strLen;
    State state;
    String errorMessage = error("ошибка", 0);
    public Result AnalyzerString() {
        while (state != State.F && nowPos < strLen && state != State.E) {
            char sym = str.charAt(nowPos);
            switch (state) {
                case S:
                    state = switch (sym) {
                        case ' ' -> State.S;
                        case 'p' -> State.Sp1;
                        default -> {
                            errorMessage = error("Ожидался символ 'P' для ключевого слова PROCEDURE", 0);
                            yield State.E;
                        }
                    };
                    break;
                case Sp1:
                    state = switch (sym) {
                        case 'r' -> State.Sp2;
                        default -> {
                            errorMessage = error("Ожидался символ 'R' для ключевого слова PROCEDURE", 0);
                            yield State.E;
                        }                    };
                    break;
                case Sp2:
                    state = switch (sym) {
                        case 'o' -> State.Sp3;
                        default -> {
                            errorMessage = error("Ожидался символ 'O' для ключевого слова PROCEDURE", 0);
                            yield State.E;
                        }                    };
                    break;
                case Sp3:
                    state = switch (sym) {
                        case 'c' -> State.Sp4;
                        default -> {
                            errorMessage = error("Ожидался символ 'C' для ключевого слова PROCEDURE", 0);
                            yield State.E;
                        }                    };
                    break;
                case Sp4:
                    state = switch (sym) {
                        case 'e' -> State.Sp5;
                        default -> {
                            errorMessage = error("Ожидался символ 'E' для ключевого слова PROCEDURE", 0);
                            yield State.E;
                        }                    };
                    break;
                case Sp5:
                    state = switch (sym) {
                        case 'd' -> State.Sp6;
                        default -> {
                            errorMessage = error("Ожидался символ 'D' для ключевого слова PROCEDURE", 0);
                            yield State.E;
                        }                    };
                    break;
                case Sp6:
                    state = switch (sym) {
                        case 'u' -> State.Sp7;
                        default -> {
                            errorMessage = error("Ожидался символ 'U' для ключевого слова PROCEDURE", 0);
                            yield State.E;
                        }                    };
                    break;
                case Sp7:
                    state = switch (sym) {
                        case 'r' -> State.Sp8;
                        default -> {
                            errorMessage = error("Ожидался символ 'R' для ключевого слова PROCEDURE",0);
                            yield State.E;
                        }                    };
                    break;
                case Sp8:
                    state = switch (sym) {
                        case 'e' -> State.S1;
                        default -> {
                            errorMessage = error("Ожидался символ 'E' для ключевого слова PROCEDURE", 0);
                            yield State.E;
                        }                    };
                    break;
                case S1:
                    state = switch (sym) {
                        case ' ' -> State.S2;
                        default -> {
                            errorMessage = error("Ожидался пробел",0);
                            yield State.E;
                        }
                    };
                    break;
                case S2:
                    state = switch (sym) {
                        case ' ' -> State.S2;
                        default -> identifier(State.S3);
                    };
                    break;
                case S3:
                    state = switch (sym) {
                        case ' ' -> State.S3;
                        case ';' -> State.F;
                        case '(' -> State.S4;
                        default -> {
                            if(indFlag){
                                errorMessage = error("Недопустимый символ для индетификатора", 0);
                            } else {
                                errorMessage = error("Ожидалось ';' или '(', или пробел",0);
                            }
                            yield State.E;
                        }
                    };
                    break;
                case S4:
                    state = switch (sym) {
                        case ' ' -> State.S4;
                        default -> listOfForlamParameters(State.S5);
                    };
                    break;
                case S5:
                    state = switch (sym) {
                        case ' ' -> State.S5;
                        case ')' -> State.S6;
                        default -> {
                            errorMessage = error("Ожидалось ')' или пробел", 0);
                            yield State.E;
                        }
                    };
                    break;
                case S6:
                    state = switch (sym) {
                        case ' ' -> State.S6;
                        case ';' -> State.F;
                        default -> {
                            errorMessage = error("Ожидалось ';' или пробел", 0);
                            yield State.E;
                        }
                    };
                    break;
                default:
                    state = State.E;
            }
            nowPos++;
        }
        if(state == State.E) {
            nowPos--;
            return new Result(nowPos, errorMessage);
        }
        else if(state == State.F && nowPos == strLen){
            return new Result(-1, "Анализ завершен успешно");
        }
        else
            return new Result(nowPos, "Неожиданный конец строки");
    }

    public State identifier(State futureState) {
        State nowState = State.S;
        while(nowPos < strLen && nowState != State.E && nowState != State.F) {
            char sym = str.charAt(nowPos);
            switch (nowState) {
                case S:
                    if (sym == ' ') {
                        nowState = State.S;
                    } else if (sym >= 'a' && sym <= 'z') {
                        nowState = State.Si;
                    } else {
                        errorMessage = error("Недопустимвый символ для индетификатора", 1);
                        nowState = State.E;
                    }
                    break;
                case Si:
                    if ((sym >= 'a' && sym <= 'z') || (sym >= '0' && sym <= '9')) {
                        nowState = State.Si;
                    } else {
                        indFlag = true;
                        nowState = State.F;
                        nowPos-=2;
                    }
                    break;
                default:
                    nowState = State.E;
            }
            nowPos++;
        };
        if(nowState == State.E){
            futureState = State.E;
            nowPos--;
        }
        return futureState;
    }

    public State listOfIdentifiers(State futureState){
        State nowState = State.S;
        while(nowPos < strLen && nowState != State.E && nowState != State.F){
            char sym = str.charAt(nowPos);
            switch (nowState) {
                case S:
                    nowState = switch (sym) {
                        case ' ' -> State.S;
                        default -> identifier(State.Sli1);
                    };
                    break;
                case Sli1:
                    nowState = switch (sym) {
                        case ' ' -> State.Sli1;
                        case ',' -> State.S;
                        default -> {
                            nowPos-=2;
                            yield State.F;
                        }
                    };
                    break;
                default:
                    nowState = State.E;
            }
            nowPos++;
        }
        if(nowState == State.E){
            futureState = State.E;
            nowPos--;
        }
        return futureState;
    }

    public State type(State futureState) {
        State nowState = State.S;
        while (nowPos < strLen && nowState != State.E && nowState != State.F) {
            char sym = str.charAt(nowPos);
            switch (nowState){
                case S:
                    nowState = switch (sym){
                        case 'r' -> State.St11;
                        case 'i' -> State.St21;
                        case 'c' -> State.St31;
                        case 'b' -> State.St4477;
                        case 'd' -> State.St51;
                        case 's' -> State.St61;
                        default -> {
                            errorMessage = error("Ожидался один из следующих символов: 'R', 'I', 'C', 'B', 'D', 'S' для типа данных",0);
                            yield State.E;
                        }
                    };
                    break;
                case St11:
                    nowState = switch (sym){
                        case 'e' -> State.St12;
                        default -> {
                            errorMessage = error("Ожидалось 'E' для REAL",0);
                            yield State.E;
                        }
                    };
                    break;
                case St12:
                    nowState = switch (sym){
                        case 'a' -> State.St13;
                        default -> {
                            errorMessage = error("Ожидалось 'A' для REAL", 0);
                            yield State.E;
                        }
                    };
                    break;
                case St13:
                    nowState = switch (sym){
                        case 'l' -> State.Fo;
                        default -> {
                            errorMessage = error("Ожидалось 'L' для REAL", 0);
                            yield State.E;
                        }
                    };
                    break;
                case St21:
                    nowState = switch (sym){
                        case 'n' -> State.St22;
                        default -> {
                            errorMessage = error("Ожидалось 'N' для INTEGER",0);
                            yield State.E;
                        }
                    };
                    break;
                case St22:
                    nowState = switch (sym){
                        case 't' -> State.St23;
                        default -> {
                            errorMessage = error("Ожидалось 'T' для INTEGER",0);
                            yield State.E;
                        }
                    };
                    break;
                case St23:
                    nowState = switch (sym){
                        case 'e' -> State.St24;
                        default -> {
                            errorMessage = error("Ожидалось 'E' для INTEGER",0);
                            yield State.E;
                        }
                    };
                    break;
                case St24:
                    nowState = switch (sym){
                        case 'g' -> State.St25;
                        default -> {
                            errorMessage = error("Ожидалось 'G' для INTEGER",0);
                            yield State.E;
                        }
                    };
                    break;
                case St25:
                    nowState = switch (sym){
                        case 'e' -> State.St26;
                        default -> {
                            errorMessage = error("Ожидалось 'E' для INTEGER",0);
                            yield State.E;
                        }
                    };
                    break;
                case St26:
                    nowState = switch (sym){
                        case 'r' -> State.Fo;
                        default -> {
                            errorMessage = error("Ожидалось 'R' для INTEGER",0);
                            yield State.E;
                        }
                    };
                    break;
                case St31:
                    nowState = switch (sym){
                        case 'h' -> State.St32;
                        default -> {
                            errorMessage = error("Ожидалось 'H' для CHAR",0);
                            yield State.E;
                        }
                    };
                    break;
                case St32:
                    nowState = switch (sym){
                        case 'a' -> State.St33;
                        default -> {
                            errorMessage = error("Ожидалось 'A' для CHAR",0);
                            yield State.E;
                        }
                    };
                    break;
                case St33:
                    nowState = switch (sym){
                        case 'r' -> State.Fo;
                        default -> {
                            errorMessage = error("Ожидалось 'R' для CHAR", 0);
                            yield State.E;
                        }
                    };
                    break;
                case St41:
                    nowState = switch (sym){
                        case 't' -> State.St42;
                        default -> {
                            errorMessage = error("Ожидалось 'T' для BYTE", 0);
                            yield State.E;
                        }
                    };
                    break;
                case St42:
                    nowState = switch (sym){
                        case 'e' -> State.Fo;
                        default -> {
                            errorMessage = error("Ожидалось 'E' для BYTE", 0);
                            yield State.E;
                        }
                    };
                    break;
                case St4477:
                    nowState = switch (sym){
                        case 'y' -> State.St41;
                        case 'o' -> State.St71;
                        default -> {
                            errorMessage = error("Ожидалось 'O' для BOOLEAN или 'Y' для BYTE",0);
                            yield State.E;
                        }
                    };
                    break;
                case St51:
                    nowState = switch (sym){
                        case 'o' -> State.St52;
                        default -> {
                            errorMessage = error("Ожидалось 'O' для DOUBLE",0);
                            yield State.E;
                        }
                    };
                    break;
                case St52:
                    nowState = switch (sym){
                        case 'u' -> State.St53;
                        default -> {
                            errorMessage = error("Ожидалось 'U' для DOUBLE",0);
                            yield State.E;
                        }
                    };
                    break;
                case St53:
                    nowState = switch (sym){
                        case 'b' -> State.St54;
                        default -> {
                            errorMessage = error("Ожидалось 'B' для DOUBLE",0);
                            yield State.E;
                        }
                    };
                    break;
                case St54:
                    nowState = switch (sym){
                        case 'l' -> State.St55;
                        default -> {
                            errorMessage = error("Ожидалось 'L' для DOUBLE",0);
                            yield State.E;
                        }
                    };
                    break;
                case St55:
                    nowState = switch (sym){
                        case 'e' -> State.Fo;
                        default -> {
                            errorMessage = error("Ожидалось 'E' для DOUBLE",0);
                            yield State.E;
                        }
                    };
                    break;
                case St61:
                    nowState = switch (sym){
                        case 't' -> State.St62;
                        default -> {
                            errorMessage = error("Ожидалось 'T' для STRING",0);
                            yield State.E;
                        }
                    };
                    break;
                case St62:
                    nowState = switch (sym){
                        case 'r' -> State.St63;
                        default -> {
                            errorMessage = error("Ожидалось 'R' для STRING",0);
                            yield State.E;
                        }
                    };
                    break;
                case St63:
                    nowState = switch (sym){
                        case 'i' -> State.St64;
                        default -> {
                            errorMessage = error("Ожидалось 'I' для STRING",0);
                            yield State.E;
                        }
                    };
                    break;
                case St64:
                    nowState = switch (sym){
                        case 'n' -> State.St65;
                        default -> {
                            errorMessage = error("Ожидалось 'N' для STRING",0);
                            yield State.E;
                        }
                    };
                    break;
                case St65:
                    nowState = switch (sym){
                        case 'g' -> State.Fo;
                        default -> {
                            errorMessage = error("Ожидалось 'G' для STRING",0);
                            yield State.E;
                        }
                    };
                    break;
                case St71:
                    nowState = switch (sym){
                        case 'o' -> State.St72;
                        default -> {
                            errorMessage = error("Ожидалось 'O' для BOOLEAN",0);
                            yield State.E;
                        }
                    };
                    break;
                case St72:
                    nowState = switch (sym){
                        case 'l' -> State.St73;
                        default -> {
                            errorMessage = error("Ожидалось 'L' для BOOLEAN",0);
                            yield State.E;
                        }
                    };
                    break;
                case St73:
                    nowState = switch (sym){
                        case 'e' -> State.St74;
                        default -> {
                            errorMessage = error("Ожидалось 'E' для BOOLEAN",0);
                            yield State.E;
                        }
                    };
                    break;
                case St74:
                    nowState = switch (sym){
                        case 'a' -> State.St75;
                        default -> {
                            errorMessage = error("Ожидалось 'A' для BOOLEAN",0);
                            yield State.E;
                        }
                    };
                    break;
                case St75:
                    nowState = switch (sym){
                        case 'n' -> State.Fo;
                        default -> {
                            errorMessage = error("Ожидалось 'N' для BOOLEAN",0);
                            yield State.E;
                        }
                    };
                    break;
                case Fo:
                    nowState = State.F;
                    nowPos-=2;
                    break;
                default:
                    nowState = State.E;
            }
            nowPos++;
        }
        if(nowState == State.E){
            futureState = State.E;
            nowPos--;
        }
        return futureState;
    }

    public State listOfForlamParameters(State futureState) {
        State nowState = State.S;
        while (nowPos < strLen && nowState != State.E && nowState != State.F) {
            char sym = str.charAt(nowPos);
            switch (nowState){
                case S:
                    nowState = switch (sym){
                        case ' ' -> State.S;
                        default -> description(State.Slfp1);
                    };
                    break;
                case Slfp1:
                    nowState = switch (sym){
                        case ' ' -> State.Slfp1;
                        case ';' -> State.S;
                        default -> {
                            nowPos-=2;
                            yield State.F;
                        }
                    };
                    break;
                default:
                    nowState = State.E;
                    errorMessage = error("Ожидался список параметров",0);
            }
            nowPos++;
        }
        if(nowState == State.E){
            futureState = State.E;
            nowPos--;
        }
        return futureState;
    }

    public State description(State futureState){
        State nowState = State.S;
        while(nowPos < strLen && nowState != State.E && nowState != State.F) {
            char sym = str.charAt(nowPos);
            switch (nowState){
                case S:
                    nowState = switch (sym){
                        case ' ' -> State.S;
                        case 'v' -> State.Sd1;
                        default -> listOfIdentifiers(State.Sd4);
                    };
                    break;
                case Sd1:
                    nowState = switch (sym){
                        case 'a' -> State.Sd2;
                        default -> {
                            errorMessage = error("Ожидалось ключевое слово var",0);
                            yield State.E;
                        }
                    };
                    break;
                case Sd2:
                    nowState = switch (sym){
                        case 'r' -> State.Sd3;
                        default -> {
                            errorMessage = error("Ожидалось ключевое слово var",0);
                            yield State.E;
                        }
                    };
                    break;
                case Sd3:
                    nowState = switch (sym){
                        case ' ' -> State.Sd3;
                        default -> listOfIdentifiers(State.Sd4);
                    };
                    break;
                case Sd4:
                    nowState = switch (sym){
                        case ' ' -> State.Sd4;
                        case ':' -> State.Sd5;
                        default -> {
                            errorMessage = error("Ожидался символ ':'",0);
                            yield State.E;
                        }
                    };
                    break;
                case Sd5:
                    nowState = switch (sym){
                        case ' ' -> State.Sd5;
                        default -> type(State.Sd6);
                    };
                    break;
                case Sd6:
                    nowState = switch (sym){
                        case ' ' -> State.Sd6;
                        default -> {
                            nowPos-=2;
                            yield State.F;
                        }
                    };
                    break;
                default:
                    nowState = State.E;
            }
            nowPos++;
        }
        if(nowState == State.E){
            futureState = State.E;
            nowPos--;
        }
        return futureState;
    }
    public String error(String nameError, int numError){
        String startColor = "\u001B[31m";
        String endColor = "\u001B[0m";
        String semantic = " Сeмантическая";
        String syntax = "Синтаксическая";
        String nameTypeError = numError == 0 ? syntax : semantic;
        return startColor + nameTypeError + " ошибка: " + endColor + nameError;
    }
}

