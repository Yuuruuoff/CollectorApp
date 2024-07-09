package ru.zmo00;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Getter
public class RateRecordSource {

    private static final String FILE = "rate-source.csv";

    private static RateRecordSource instance;

    private final List<RateRecord> source;

    private RateRecordSource(List<RateRecord> source) {
        this.source = source;
    }

    public static synchronized RateRecordSource getInstance() {
        if (Objects.isNull(instance)) {
            instance = RateRecordSource.of();
        }
        return instance;
    }

    private static RateRecordSource of() {
        ClassLoader loader = RateRecordSource.class.getClassLoader();
        try (InputStream inputStream = loader.getResourceAsStream(FILE)) {
            Objects.requireNonNull(inputStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            List<RateRecord> records = reader.lines()
                    .skip(1)
                    .map(lineToRateRecordMapper())
                    .toList();
            return new RateRecordSource(records);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Function<String, RateRecord> lineToRateRecordMapper() {
        return line -> {
            String[] data = line.split(",");
            return RateRecord.builder()
                    .id(Long.parseLong(data[0]))
                    .date(LocalDate.parse(data[1], DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                    .from(Currency.valueOf(data[2]))
                    .to(Currency.valueOf(data[3]))
                    .rate(new BigDecimal(data[4]))
                    .build();
        };
    }
}
