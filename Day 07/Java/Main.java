import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    private static List<IPAddress> data = new ArrayList<>();

    public static void main(String[] args) {

        parseInput("./input");

        List<IPAddress> tls = new ArrayList<>();
        List<IPAddress> ssl = new ArrayList<>();
        for (IPAddress ip : data) {
            if (ip.tls) {
                tls.add(ip);
            }
            if (ip.ssl) {
                ssl.add(ip);
            }
        }

        System.out.printf("TLS addresses: %d.\n", tls.size());
        System.out.printf("SSL addresses: %d.\n", ssl.size());
    }

    private static void parseInput(String fileName) {
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach((line) -> {
                IPAddress address = new IPAddress(line);
                data.add(address);
            });
        } catch (IOException e) {
            System.out.printf("Could not open file '%s'\n", fileName);
        }
    }
}

