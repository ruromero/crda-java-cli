package com.redhat.ecosystemappeng.client;

import com.redhat.ecosystemappeng.crda.api.AnalysisReport;

public record DepAnalysisDTO(AnalysisReport report, String html) {
}
