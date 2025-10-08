package utility;

import commands.CommandManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CheckRecursion {
    private Console console;
    private final CommandManager commandManager;
    private final List<String> scriptStack = new ArrayList<>();
    private int lengthRecursion = -1;
    public CheckRecursion(Console console, CommandManager commandManager) {
        this.console = console;
        this.commandManager = commandManager;
    }
    /**
     * Проверяет рекурсивность выполнения скриптов.
     * @param argument Название запускаемого скрипта
     * @return можно ли выполнять скрипт.
     */
    public  boolean checkRecursion(String argument, Scanner scriptScanner) {
        var recStart = -1;
        var i = 0;
        for (String script : scriptStack) {
            i++;
            if (argument.equals(script)) {
                if (recStart < 0) recStart = i;
                if (lengthRecursion < 0) {
                    console.selectConsoleScanner();
                    console.println("Была замечена рекурсия! Введите максимальную глубину рекурсии (0..500)");
                    while (lengthRecursion < 0 || lengthRecursion > 500) {
                        try { console.print("> "); lengthRecursion = Integer.parseInt(console.readln().trim()); } catch (NumberFormatException e) { console.println("длина не распознана"); }
                    }
                    console.selectFileScanner(scriptScanner);
                }
                if (i > recStart + lengthRecursion || i > 500)
                    return false;
            }
        }
        return true;
    }
}
