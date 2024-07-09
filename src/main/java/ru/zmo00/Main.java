package ru.zmo00;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Main {
    public static void main(String[] args) {
        List<RateRecord> records = RateRecordSource.getInstance().getSource();

        Map<LocalDate, Map<CurrencyPair, BigDecimal>> rates = records.stream()
                .collect(ratesCollector());

        List<RateRecord> errors = RatesValidator.validate(rates);
        if (errors.isEmpty()) {
            System.out.println("Задача выполнена успешно");
        } else {
            System.out.println("Содержаться ошибки для записей: " + errors);
        }
    }

    private static Collector<
            RateRecord,
            Map<LocalDate, Map<CurrencyPair, BigDecimal>>,
            Map<LocalDate, Map<CurrencyPair, BigDecimal>>>
    ratesCollector() {
        Supplier<Map<LocalDate, Map<CurrencyPair, BigDecimal>>> supplier = HashMap::new;
        BiConsumer<Map<LocalDate, Map<CurrencyPair, BigDecimal>>, RateRecord> accumulator = (map, record) -> {
            Map<CurrencyPair, BigDecimal> rates = new HashMap<>();
            rates.put(new CurrencyPair(record.getFrom(), record.getTo()), record.getRate());
            if (map.get(record.getDate()) == null) {
                map.put(record.getDate(), rates);
            } else {
                map.get(record.getDate()).putAll(rates);
            }
        };

        BinaryOperator<Map<LocalDate, Map<CurrencyPair, BigDecimal>>> combiner = (map1, map2) -> {
            map1.putAll(map2);
            return map1;
        };

        return Collector.of(
                supplier, accumulator, combiner
        );
    }
}