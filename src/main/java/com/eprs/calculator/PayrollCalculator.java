package com.eprs.calculator;

import com.eprs.model.Payroll;

public class PayrollCalculator {

    private static final int HOURS_PER_DAY = 8;
    private static final double OVERTIME_MULTIPLIER = 1.25;

    public static Payroll compute(int pId, String empName, String month,
                                  int hRate, int totalHours, int late, int absent, int overtimeHours) {

        int basicPay = hRate * totalHours;
        double lateDeduction   = (hRate / 60.0) * late;
        double absentDeduction = (double) hRate * HOURS_PER_DAY * absent;

        // Whole month overtime pay calculation using the 1.25 multiplier as default
        double overtime = overtimeHours * hRate * OVERTIME_MULTIPLIER;

        double gross = Math.max(0, basicPay + overtime - lateDeduction - absentDeduction);

        double sss        = Math.min(Math.max(gross, 5_000), 35_000) * 0.05;
        double philhealth = Math.min(Math.max(gross, 10_000), 100_000) * 0.025;
        double pagIbig    = Math.min(gross, 10_000) * 0.02;
        double tax        = computeTax(gross - sss - philhealth - pagIbig);

        double totalDeductions = sss + philhealth + pagIbig + tax;
        double netPay = gross - totalDeductions;

        return new Payroll(pId, empName, month, hRate, totalHours, overtime, basicPay,
                late, absent, round(sss), round(pagIbig), round(philhealth), round(tax),
                round(gross), round(totalDeductions), round(netPay), "PENDING");
    }

    private static double computeTax(double taxable) {
        if (taxable <= 20_833)  return 0;
        if (taxable <= 33_333)  return (taxable - 20_833) * 0.15;
        if (taxable <= 66_667)  return 1_875.00  + (taxable - 33_333)  * 0.20;
        if (taxable <= 166_667) return 8_541.80  + (taxable - 66_667)  * 0.25;
        if (taxable <= 666_667) return 33_541.80 + (taxable - 166_667) * 0.30;
        return 183_541.80 + (taxable - 666_667) * 0.35;
    }

    private static double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}