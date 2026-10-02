package ru.Kodrik.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.Kodrik.MySecondTestAppSpringBoot.model.Positions;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService{
    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays) {
        int daysInYear = Year.now().length();
        return salary * bonus * workDays * positions.getPositionCoefficient() /  daysInYear;
    }

    @Override
    public double calculateQuarterlyBonus(Positions positions, double salary, double bonus, int workDays, int year, int quarter) {
         if(!(positions.isManager())) {
             throw new IllegalArgumentException
                     ("Квартальная премия рассчитывается только для менеджеров. Позиция: " + positions
             );
         } if (quarter < 1 || quarter > 4) {
            throw new IllegalArgumentException("Квартал должен быть от 1 до 4, получено: " + quarter);
        }
        int daysInQuarter = getDaysInQuarter(year, quarter);

        return salary * bonus * workDays * positions.getPositionCoefficient() / daysInQuarter;
    }

    private int getDaysInQuarter(int year, int quarter) {
        int startMonth = (quarter - 1) * 3 + 1;   // 1, 4, 7, 10
        LocalDate start = LocalDate.of(year, startMonth, 1);
        LocalDate end = start.plusMonths(3);
        return (int) ChronoUnit.DAYS.between(start, end);
    }
}
