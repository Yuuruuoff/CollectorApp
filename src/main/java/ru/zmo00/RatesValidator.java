package ru.zmo00;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RatesValidator {

    public static List<RateRecord> validate(Map<LocalDate, Map<CurrencyPair, BigDecimal>> rates) {
        List<RateRecord> errors = new ArrayList<>();

        List<RateRecord> records = RateRecordSource.getInstance().getSource();
        for (RateRecord rateRecord : records) {
            if (rates.get(rateRecord.getDate())
                    .get(new CurrencyPair(rateRecord.getFrom(), rateRecord.getTo()))
                    .compareTo(rateRecord.getRate()) != 0) {
                errors.add(rateRecord);
            }
        }

        return errors;
    }
}
