package ru.zmo00;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateRecord {

    private Long id;
    private LocalDate date;
    private Currency from;
    private Currency to;
    private BigDecimal rate;

}
