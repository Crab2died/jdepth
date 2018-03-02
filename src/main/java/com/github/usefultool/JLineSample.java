package com.github.usefultool;

import jline.console.ConsoleReader;
import jline.console.completer.Completer;

import java.io.IOException;
import java.util.List;

public class JLineSample {
    public static void main(String... args) throws IOException {

        // IDEA、Eclipse调试时使用
//        System.setProperty("jline.WindowsTerminal.directConsole", "false");
//        System.setProperty("jline.internal.Log.debug", "true");
        ConsoleReader reader = new ConsoleReader();
        reader.addCompleter(new Completer() {
            @Override
            public int complete(String buffer, int cursor, List<CharSequence> candidates) {
                if (buffer.length() > 0 && cursor > 0) {
                    String prefix = buffer.substring(cursor);
                    if (prefix.endsWith("m")) {
                        candidates.add("mdir");
                        candidates.add("mfile");
                        candidates.add("make");
                    }
                }
                return cursor - 2;
            }
        });
        String cmd;
        do {
            cmd = reader.readLine("JLine >");
            System.out.println(cmd);
        } while (cmd != null && !"exit".equals(cmd));

        reader.close();
    }
}
