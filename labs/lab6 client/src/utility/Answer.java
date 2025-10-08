package utility;

import models.*;

import java.util.NoSuchElementException;

public class Answer {
    public static class AnswerBreak extends Exception{
    }
    public static StudyGroup ansStudyGroup(Console console, Integer id) throws AnswerBreak {
        try {
            String name;
            while (true) {
                if (console.getFileScanner() == null) {
                    console.print("Введите название группы: ");
                }
                name = console.readln().trim();
                if (name.equals("exit")) throw new AnswerBreak();
                if (!name.isEmpty()) break;
            }
            var groupAdmin = ansPerson(console);
            var coordinates = ansCoordinates(console);
            var formOfEducation = ansFormOfEducation(console);
            var semesterEnum = ansSemestr(console);
            var studentsCount = ansStudentscount(console);

            return new StudyGroup(id, name, coordinates, studentsCount, formOfEducation, semesterEnum, groupAdmin);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static Long ansStudentscount(Console console) throws AnswerBreak {
        try {
            long studentscount;
            while (true) {
                if (console.getFileScanner() == null) {
                    console.print("Ведите количество человек в группе: ");
                }
                var line = console.readln().trim();
                if (line.equals("exit"))
                    throw new AnswerBreak();
                if (!line.equals("")) {
                    try {
                        studentscount = Long.parseLong(line);
                        if (studentscount >= 0){
                            break;
                        }else{
                            System.out.println("Число не должно быть отрицательным");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Тут должно быть число");
                    }
                }
            }
            return studentscount;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");

            return null;
        }
    }

    public static Coordinates ansCoordinates(Console console) throws AnswerBreak {
        try {
            int x;
            while (true) {
                if (console.getFileScanner() == null) {
                    console.print("Ведите координаты x: ");
                }
                var line = console.readln().trim();
                if (line.equals("exit"))
                    throw new AnswerBreak();
                if (!line.equals("")) {
                    try {
                        x = Integer.parseInt(line);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Тут должно быть число");
                    }
                }
            }
            long y;
            while (true) {
                if (console.getFileScanner() == null) {
                    console.print("Ведите координаты y: ");
                }
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AnswerBreak();
                if (!line.equals("")) {
                    try {
                        y = Long.parseLong(line);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Тут должно быть число");
                    }
                }
            }
            return new Coordinates(x, y);
        } catch (NoSuchElementException | IllegalStateException e) {
//            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static Person ansPerson(Console console) throws AnswerBreak {
        try {
            HairColor hairColor;
            while (true) {
                if (console.getFileScanner() == null) {
                    console.print("Выберите один из предложенных цветов волос: " + HairColor.names() + ": ");
                }
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AnswerBreak();
                if (!line.equals("")) {
                    try {
                        hairColor = HairColor.valueOf(line);
                        break;
                    } catch (NullPointerException | IllegalArgumentException e) {
                        System.out.println("Такого выбора нет");
                    }
                } else {
                    hairColor = null;
                    break;
                }
            }
            EyeColor eyeColor;
            while (true) {
                if (console.getFileScanner() == null) {
                    console.print("Выберите один из предложенных цветов глаз: " + EyeColor.names() + ": ");
                }
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AnswerBreak();
                if (!line.equals("")) {
                    try {
                        eyeColor = EyeColor.valueOf(line);
                        break;
                    } catch (NullPointerException | IllegalArgumentException e) {
                        System.out.println("Такого выбора нет");
                    }
                }

            }
            Country country;
            while (true) {
                if (console.getFileScanner() == null) {
                    console.print("Выберите одну из предложенных стран: " + Country.names() + ": ");
                }
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AnswerBreak();
                if (!line.equals("")) {
                    try {
                        country = Country.valueOf(line);
                        break;
                    } catch (NullPointerException | IllegalArgumentException e) {
                        System.out.println("Такого выбора нет");
                    }
                } else {
                    country = null;
                    break;
                }
            }
            int x;
            while (true) {
                if (console.getFileScanner() == null) {
                    console.print("Введите координаты x: ");
                }
                var line = console.readln().trim();
                if (line.equals("exit"))
                    throw new AnswerBreak();
                if (!line.equals("")) {
                    try {
                        x = Integer.parseInt(line);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Тут должно быть число");
                    }
                }
            }
            int y;
            while (true) {
                if (console.getFileScanner() == null) {
                    console.print("Введите координаты y: ");
                }
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AnswerBreak();
                if (!line.equals("")) {
                    try {
                        y = Integer.parseInt(line);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Тут должно быть число");
                    }
                }
            }
            String namePlace;
            while (true) {
                if (console.getFileScanner() == null) {
                    console.print("Введите место: ");
                }
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AnswerBreak();
                if (!line.equals("")) {
                    namePlace = line;
                    break;
                }
            }
            String nameAdmin;
            while (true) {
                if (console.getFileScanner() == null) {
                    console.print("Введите имя старосты: ");
                }
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AnswerBreak();
                if (!line.equals("")) {
                    nameAdmin = line;
                    break;
                }
            }
            String passportId;
            while (true) {
                if (console.getFileScanner() == null) {
                    console.print("Введите паспортное id: ");
                }
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AnswerBreak();
                if (!line.equals("")) {
                    passportId = line;
                    break;
                }
            }

            return new Person(nameAdmin, passportId, eyeColor, hairColor, country, new Location(namePlace, x, y));
        } catch (NoSuchElementException | IllegalStateException e) {
//            console.printError("Чет не читается");
            return null;
        }
    }


    public static Semester ansSemestr(Console console) throws AnswerBreak {
        try {
            Semester semester;
            while (true) {
                if (console.getFileScanner() == null) {
                    console.print("Введите номер семестра: " + Semester.names() + ": ");
                }
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AnswerBreak();
                if (!line.equals("")) {
                    try {
                        semester = Semester.valueOf(line);
                        break;
                    } catch (NullPointerException | IllegalArgumentException e) {
                        System.out.println("Такого выбора нет");
                    }
                }
            }
            return semester;
        } catch (NoSuchElementException | IllegalStateException e) {
//            console.printError("Чет не читается");
            return null;
        }
    }

    public static FormOfEducation ansFormOfEducation(Console console) throws AnswerBreak {
        try {
            FormOfEducation formOfEducation;
            while (true) {
                if (console.getFileScanner() == null) {
                    console.print("Выберите один из предложенных вариантов обучения: " + FormOfEducation.names() + ":  ");
                }
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AnswerBreak();
                if (!line.equals("")) {
                    try {
                        formOfEducation = FormOfEducation.valueOf(line);
                        break;
                    } catch (NullPointerException | IllegalArgumentException e) {
                        System.out.println("Такого выбора нет");
                    }
                } else {
                    return null;
                }
            }
            return formOfEducation;
        } catch (NoSuchElementException | IllegalStateException e) {
//            console.printError("Чет не читается");
            return null;
        }
    }
}

