package ru.Kodrik.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.Kodrik.MySecondTestAppSpringBoot.model.Positions;

@Service
public interface AnnualBonusService {

    double calculate (Positions positions, double salary, double bonus, int workDays);

    double calculateQuarterlyBonus(Positions positions, double salary, double bonus,
                                   int workDays, int year, int quarter);
}
