package backend.academy.console;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Console implements AutoCloseable {
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public Console(BufferedReader reader, BufferedWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public void print(String message) throws IOException {
        writer.write(message);
        writer.flush();
    }

    public void println(String message) throws IOException {
        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    public void flush() throws IOException {
        if (System.getProperty("os.name").contains("Windows")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (InterruptedException e) {
                throw new IOException("error.flush", e);
            }
        } else {
            print("\033[H\033[2J");
        }

    }

    /**
     * Прочитать строку
     *
     * @return строку или завершает работу приложения при CTRL+D/CMD+D
     */
    @SuppressFBWarnings(value = "DM_EXIT")
    public String readLine() throws IOException {
        var input = reader.readLine();
        if (input == null) {
            writer.close();
            reader.close();
            System.exit(0);
            return null;
        } else {
            return input;
        }
    }

    @Override
    public void close() throws Exception {
        writer.close();
        reader.close();
    }
}
