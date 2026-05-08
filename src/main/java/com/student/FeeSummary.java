package com.student;

import java.math.BigDecimal;

// This class holds the main fee data for one semester
public class FeeSummary {

    private int summaryId;
    private int semester;
    private BigDecimal totalDue;
    private BigDecimal totalPaid;
    private String status;

    // Getters and Setters
    public int getSummaryId() { return summaryId; }
    public void setSummaryId(int summaryId) { this.summaryId = summaryId; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public BigDecimal getTotalDue() { return totalDue; }
    public void setTotalDue(BigDecimal totalDue) { this.totalDue = totalDue; }

    public BigDecimal getTotalPaid() { return totalPaid; }
    public void setTotalPaid(BigDecimal totalPaid) { this.totalPaid = totalPaid; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}