public class AnalyzerCode {

    public AnalyzerCode(String text){
        str = text.toLowerCase();
        strLen = str.length();
        nowPos = 0;
        state = State.S;
    }
    String str;
    int nowPos;
    int strLen;
    State state;
    String errorMessage = "Ошибка";
    public Result AnalyzerString() {
        while (state != State.F && nowPos < strLen && state != State.E) {
            char sym = str.charAt(nowPos);
            switch (state) {
                case S:
                    state = switch (sym) {
                        case ' ' -> State.S;
                        case 'p' -> State.Sp1;
                        default -> {
                            errorMessage = "Ожидался символ 'P' для ключевого слова PROCEDURE";
                            yield State.E;
                        }
                    };
                    break;
                case Sp1:
                    state = switch (sym) {
                        case 'r' -> State.Sp2;
                        default -> {
                            errorMessage = "Ожидался символ 'R' для ключевого слова PROCEDURE";
                            yield State.E;
                        }                    };
                    break;
                case Sp2:
                    state = switch (sym) {
                        case 'o' -> State.Sp3;
                        default -> {
                            errorMessage = "Ожидался символ 'O' для ключевого слова PROCEDURE";
                            yield State.E;
                        }                    };
                    break;
                case Sp3:
                    state = switch (sym) {
                        case 'c' -> State.Sp4;
                        default -> {
                            errorMessage = "Ожидался символ 'C' для ключевого слова PROCEDURE";
                            yield State.E;
                        }                    };
                    break;
                case Sp4:
                    state = switch (sym) {
                        case 'e' -> State.Sp5;
                        default -> {
                            errorMessage = "Ожидался символ 'E' для ключевого слова PROCEDURE";
                            yield State.E;
                        }                    };
                    break;
                case Sp5:
                    state = switch (sym) {
                        case 'd' -> State.Sp6;
                        default -> {
                            errorMessage = "Ожидался символ 'D' для ключевого слова PROCEDURE";
                            yield State.E;
                        }                    };
                    break;
                case Sp6:
                    state = switch (sym) {
                        case 'u' -> State.Sp7;
                        default -> {
                            errorMessage = "Ожидался символ 'U' для ключевого слова PROCEDURE";
                            yield State.E;
                        }                    };
                    break;
                case Sp7:
                    state = switch (sym) {
                        case 'r' -> State.Sp8;
                        default -> {
                            errorMessage = "Ожидался символ 'R' для ключевого слова PROCEDURE";
                            yield State.E;
                        }                    };
                    break;
                case Sp8:
                    state = switch (sym) {
                        case 'e' -> State.S1;
                        default -> {
                            errorMessage = "Ожидался символ 'E' для ключевого слова PROCEDURE";
                            yield State.E;
                        }                    };
                    break;
                case S1:
                    state = switch (sym) {
                        case ' ' -> State.S2;
                        default -> {
                            errorMessage = "Ожидался пробел";
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
                            errorMessage = "Ожидалось ';' или '('";
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
                            errorMessage = "Ожидалось ')'";
                            yield State.E;
                        }
                    };
                    break;
                case S6:
                    state = switch (sym) {
                        case ' ' -> State.S6;
                        case ';' -> State.F;
                        default -> {
                            errorMessage = "Ожидалось ';'";
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
                        errorMessage = "Ожидался индетификатор";
                        nowState = State.E;
                    }
                    break;
                case Si:
                    if ((sym >= 'a' && sym <= 'z') || (sym >= '0' && sym <= '9')) {
                        nowState = State.Si;
                    } else {
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
                            errorMessage = "Ожидалось один из следующих символов: 'R', 'I', 'C', 'B', 'D', 'S'";
                            yield State.E;
                        }
                    };
                    break;
                case St11:
                    nowState = switch (sym){
                        case 'e' -> State.St12;
                        default -> {
                            errorMessage = "Ожидалось 'E' для REAL";
                            yield State.E;
                        }
                    };
                    break;
                case St12:
                    nowState = switch (sym){
                        case 'a' -> State.St13;
                        default -> {
                            errorMessage = "Ожидалось 'A' для REAL";
                            yield State.E;
                        }
                    };
                    break;
                case St13:
                    nowState = switch (sym){
                        case 'l' -> State.Fo;
                        default -> {
                            errorMessage = "Ожидалось 'L' для REAL";
                            yield State.E;
                        }
                    };
                    break;
                case St21:
                    nowState = switch (sym){
                        case 'n' -> State.St22;
                        default -> {
                            errorMessage = "Ожидалось 'N' для INTEGER";
                            yield State.E;
                        }
                    };
                    break;
                case St22:
                    nowState = switch (sym){
                        case 't' -> State.St23;
                        default -> {
                            errorMessage = "Ожидалось 'T' для INTEGER";
                            yield State.E;
                        }
                    };
                    break;
                case St23:
                    nowState = switch (sym){
                        case 'e' -> State.St24;
                        default -> {
                            errorMessage = "Ожидалось 'E' для INTEGER";
                            yield State.E;
                        }
                    };
                    break;
                case St24:
                    nowState = switch (sym){
                        case 'g' -> State.St25;
                        default -> {
                            errorMessage = "Ожидалось 'G' для INTEGER";
                            yield State.E;
                        }
                    };
                    break;
                case St25:
                    nowState = switch (sym){
                        case 'e' -> State.St26;
                        default -> {
                            errorMessage = "Ожидалось 'E' для INTEGER";
                            yield State.E;
                        }
                    };
                    break;
                case St26:
                    nowState = switch (sym){
                        case 'r' -> State.Fo;
                        default -> {
                            errorMessage = "Ожидалось 'R' для INTEGER";
                            yield State.E;
                        }
                    };
                    break;
                case St31:
                    nowState = switch (sym){
                        case 'h' -> State.St32;
                        default -> {
                            errorMessage = "Ожидалось 'H' для CHAR";
                            yield State.E;
                        }
                    };
                    break;
                case St32:
                    nowState = switch (sym){
                        case 'a' -> State.St33;
                        default -> {
                            errorMessage = "Ожидалось 'A' для CHAR";
                            yield State.E;
                        }
                    };
                    break;
                case St33:
                    nowState = switch (sym){
                        case 'r' -> State.Fo;
                        default -> {
                            errorMessage = "Ожидалось 'R' для CHAR";
                            yield State.E;
                        }
                    };
                    break;
                case St41:
                    nowState = switch (sym){
                        case 't' -> State.St42;
                        default -> {
                            errorMessage = "Ожидалось 'T' для BYTE";
                            yield State.E;
                        }
                    };
                    break;
                case St42:
                    nowState = switch (sym){
                        case 'e' -> State.Fo;
                        default -> {
                            errorMessage = "Ожидалось 'E' для BYTE";
                            yield State.E;
                        }
                    };
                    break;
                case St4477:
                    nowState = switch (sym){
                        case 'y' -> State.St41;
                        case 'o' -> State.St71;
                        default -> {
                            errorMessage = "Ожидалось 'O' для BOOLEAN или 'Y' для BYTE";
                            yield State.E;
                        }
                    };
                    break;
                case St51:
                    nowState = switch (sym){
                        case 'o' -> State.St52;
                        default -> {
                            errorMessage = "Ожидалось 'O' для DOUBLE";
                            yield State.E;
                        }
                    };
                    break;
                case St52:
                    nowState = switch (sym){
                        case 'u' -> State.St53;
                        default -> {
                            errorMessage = "Ожидалось 'U' для DOUBLE";
                            yield State.E;
                        }
                    };
                    break;
                case St53:
                    nowState = switch (sym){
                        case 'b' -> State.St54;
                        default -> {
                            errorMessage = "Ожидалось 'B' для DOUBLE";
                            yield State.E;
                        }
                    };
                    break;
                case St54:
                    nowState = switch (sym){
                        case 'l' -> State.St55;
                        default -> {
                            errorMessage = "Ожидалось 'L' для DOUBLE";
                            yield State.E;
                        }
                    };
                    break;
                case St55:
                    nowState = switch (sym){
                        case 'e' -> State.Fo;
                        default -> {
                            errorMessage = "Ожидалось 'E' для DOUBLE";
                            yield State.E;
                        }
                    };
                    break;
                case St61:
                    nowState = switch (sym){
                        case 't' -> State.St62;
                        default -> {
                            errorMessage = "Ожидалось 'T' для STRING";
                            yield State.E;
                        }
                    };
                    break;
                case St62:
                    nowState = switch (sym){
                        case 'r' -> State.St63;
                        default -> {
                            errorMessage = "Ожидалось 'R' для STRING";
                            yield State.E;
                        }
                    };
                    break;
                case St63:
                    nowState = switch (sym){
                        case 'i' -> State.St64;
                        default -> {
                            errorMessage = "Ожидалось 'I' для STRING";
                            yield State.E;
                        }
                    };
                    break;
                case St64:
                    nowState = switch (sym){
                        case 'n' -> State.St65;
                        default -> {
                            errorMessage = "Ожидалось 'N' для STRING";
                            yield State.E;
                        }
                    };
                    break;
                case St65:
                    nowState = switch (sym){
                        case 'g' -> State.Fo;
                        default -> {
                            errorMessage = "Ожидалось 'G' для STRING";
                            yield State.E;
                        }
                    };
                    break;
                case St71:
                    nowState = switch (sym){
                        case 'o' -> State.St72;
                        default -> {
                            errorMessage = "Ожидалось 'O' для BOOLEAN";
                            yield State.E;
                        }
                    };
                    break;
                case St72:
                    nowState = switch (sym){
                        case 'l' -> State.St73;
                        default -> {
                            errorMessage = "Ожидалось 'L' для BOOLEAN";
                            yield State.E;
                        }
                    };
                    break;
                case St73:
                    nowState = switch (sym){
                        case 'e' -> State.St74;
                        default -> {
                            errorMessage = "Ожидалось 'E' для BOOLEAN";
                            yield State.E;
                        }
                    };
                    break;
                case St74:
                    nowState = switch (sym){
                        case 'a' -> State.St75;
                        default -> {
                            errorMessage = "Ожидалось 'A' для BOOLEAN";
                            yield State.E;
                        }
                    };
                    break;
                case St75:
                    nowState = switch (sym){
                        case 'n' -> State.Fo;
                        default -> {
                            errorMessage = "Ожидалось 'N' для BOOLEAN";
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
                    errorMessage = "Ожидался список параметров";
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
                            errorMessage = "Ожидалось ключевое слово var";
                            yield State.E;
                        }
                    };
                    break;
                case Sd2:
                    nowState = switch (sym){
                        case 'r' -> State.Sd3;
                        default -> {
                            errorMessage = "Ожидалось ключевое слово var";
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
                            errorMessage = "Ожидался символ :";
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
}
