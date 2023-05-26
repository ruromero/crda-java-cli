package com.redhat.ecosystemappeng.app;

import com.redhat.ecosystemappeng.crda.api.AnalysisReport;
import com.redhat.ecosystemappeng.crda.api.CvssVector;
import com.redhat.ecosystemappeng.crda.api.DependenciesSummary;
import com.redhat.ecosystemappeng.crda.api.DependencyReport;
import com.redhat.ecosystemappeng.crda.api.Issue;
import com.redhat.ecosystemappeng.crda.api.PackageRef;
import com.redhat.ecosystemappeng.crda.api.ProviderStatus;
import com.redhat.ecosystemappeng.crda.api.Remediation;
import com.redhat.ecosystemappeng.crda.api.Summary;
import com.redhat.ecosystemappeng.crda.api.TransitiveDependencyReport;
import com.redhat.ecosystemappeng.crda.api.VulnerabilitiesSummary;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = {
    AnalysisReport.class,
    Summary.class,
    DependenciesSummary.class,
    VulnerabilitiesSummary.class,
    DependencyReport.class,
    Issue.class,
    TransitiveDependencyReport.class,
    CvssVector.class,
    PackageRef.class,
    ProviderStatus.class,
    Remediation.class
})
public class ReflectionConfig {
    
}
