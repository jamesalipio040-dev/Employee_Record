package com.eprs.model;

public record Payroll(
        int p_id,
        String emp_name,
        String month,
        int h_rate,
        int total_hours,
        int basic_pay,
        int late,
        int absent,
        double sss,
        double pag_ibig,
        double philhealth,
        double tax,
        double gross_salary,
        double total_deductions,
        double net_pay
) {
    @Override
    public String toString() {
        return String.format(
                "[%d] %s (%s)%n" +
                        "  Hourly Rate: %d | Total Hours: %d | Basic Pay: %d%n" +
                        "  Late: %d | Absent: %d%n" +
                        "  SSS: %.2f | Pag-IBIG: %.2f | PhilHealth: %.2f | Tax: %.2f%n" +
                        "  Gross: %.2f | Total Deductions: %.2f | NET PAY: %.2f",
                p_id, emp_name, month,
                h_rate, total_hours, basic_pay,
                late, absent,
                sss, pag_ibig, philhealth, tax,
                gross_salary, total_deductions, net_pay);
    }
}