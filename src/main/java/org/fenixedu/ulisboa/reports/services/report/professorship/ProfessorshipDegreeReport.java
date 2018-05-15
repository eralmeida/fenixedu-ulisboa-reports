package org.fenixedu.ulisboa.reports.services.report.professorship;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ShiftProfessorship;

public class ProfessorshipDegreeReport extends ProfessorshipReport {

    final Degree degree;

    public ProfessorshipDegreeReport(final ShiftProfessorship shiftProfessorship, final Degree degree) {
        super(shiftProfessorship);
        this.degree = degree;
    }

    public String getDegreeName() {
        return this.degree.getNameI18N().getContent();
    }

    public String getDegreeCode() {
        return this.degree.getCode();
    }

}
