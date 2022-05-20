package codingdojo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    static SimpleDateFormat simpleDateFormatter =
        new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
    public static void main(String[] argv) {
        Database.getItems()
            .map(Main::handleDatabaseResult)
            .map(WithDate.lift(GildedRose::updateItems))
            .map(WithDate.lift(Report::createReport))
            .subscribe(Main::writeReport);
    }

    private static WithDate<List<Item>> handleDatabaseResult(DatabaseResult r) {
        List<Item> itemList = r.result.stream() //r besitzt einen timestamp --> dieser geht verloren
                .map(i -> new Item(
                        i.name,
                        Integer.valueOf(i.sellIn),
                        Integer.valueOf(i.quality)
                )).collect(Collectors.toList());
        return new WithDate<>(r.timestamp, itemList);
    }

    private static void writeReport(WithDate<Report> report) {
        try {
            String filename = "report" + report.timestamp.getSeconds() + ".txt"; //hier soll filename landen
            Path path = Paths.get(filename);
            String reportString = report.value.toString();
            Files.write(path, reportString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
