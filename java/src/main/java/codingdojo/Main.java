package codingdojo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    static SimpleDateFormat reportFileDateFormat =
            new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");

    public static void main(String[] argv) {
        Database.getItems()
                .map(Main::getItems)
                .map(WithDate.lift(GildedRose::updateItems))
                .map(WithDate.lift(Report::createReport))
                .subscribe(Main::writeReport);
    }

    private static WithDate<List<Item>> getItems(DatabaseResult result) {
        List<Item> itemList = result.items.stream()
                .map(i -> new Item(
                        i.name,
                        Integer.parseInt(i.sellIn),
                        Integer.parseInt(i.quality)
                )).collect(Collectors.toList());
        return new WithDate<>(result.timestamp, itemList);
    }

    private static void writeReport(WithDate<Report> report) {
        try {
            String filename = "report" + reportFileDateFormat.format(report.timestamp) + ".txt";
            Path path = Paths.get(filename);
            String reportString = report.value.toString();
            Files.write(path, reportString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
