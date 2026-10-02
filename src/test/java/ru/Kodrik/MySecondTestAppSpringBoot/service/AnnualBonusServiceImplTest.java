package ru.Kodrik.MySecondTestAppSpringBoot.service;

import org.junit.jupiter.api.Test;
import ru.Kodrik.MySecondTestAppSpringBoot.model.Positions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AnnualBonusServiceImplTest {

    @Test
    void calculate() {

        //given
        Positions position = Positions.HR;
        double bonus = 2.0;
        int workDays = 243;
        double salary = 100000.00;

        //when
        double result = new AnnualBonusServiceImpl().calculate(position, salary, bonus, workDays);

        //then
        double expected = 159780.82191780822;
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void calculateQuarterlyBonus() {
        Positions position = Positions.PO;
        double bonus = 3.0;
        int workDays = 243;
        double salary = 300000.00;
        int year = 2026;
        int quarter = 1;

        double result = new AnnualBonusServiceImpl().calculateQuarterlyBonus
                (position, salary, bonus, workDays, year, quarter);

        double expected = 7290000.0;
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void calculateQuarterlyBonus_forNonManager_shouldThrow() {
        Positions position = Positions.DEV;   // isManager = false
        double salary = 100000.00;
        double bonus = 2.0;
        int workDays = 60;
        int year = 2026;
        int quarter = 1;

        assertThatThrownBy(() -> new AnnualBonusServiceImpl()
                .calculateQuarterlyBonus(position, salary, bonus, workDays, year, quarter))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("только для менеджеров");
    }

}