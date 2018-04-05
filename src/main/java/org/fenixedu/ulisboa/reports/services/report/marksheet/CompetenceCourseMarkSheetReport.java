package org.fenixedu.ulisboa.reports.services.report.marksheet;

import org.fenixedu.academic.domain.evaluation.markSheet.CompetenceCourseMarkSheet;
import org.fenixedu.academic.domain.evaluation.markSheet.CompetenceCourseMarkSheetStateChange;
import org.fenixedu.academic.domain.evaluation.season.EvaluationSeasonServices;
import org.fenixedu.ulisboa.specifications.dto.evaluation.markSheet.CompetenceCourseMarkSheetBean;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class CompetenceCourseMarkSheetReport implements Comparable<CompetenceCourseMarkSheetReport> {

    private final CompetenceCourseMarkSheet competenceCourseMarkSheet;

    public CompetenceCourseMarkSheetReport(final CompetenceCourseMarkSheet competenceCourseMarkSheet) {
        this.competenceCourseMarkSheet = competenceCourseMarkSheet;
    }

    public int compareTo(final CompetenceCourseMarkSheetReport o) {

    }

    public String getCompetenceCourseCode() {
        return competenceCourseMarkSheet.getCompetenceCourse().getCode();
    }

    public String getComptenceCourseName() {
        return competenceCourseMarkSheet.getCompetenceCourse().getNameI18N().getContent();
    }

    public String getExecutionPresentation() {
        CompetenceCourseMarkSheetBean.getExecutionCoursePresentation(competenceCourseMarkSheet.getExecutionCourse());
    }

    public String getEvaluationSeason() {
        EvaluationSeasonServices.getDescriptionI18N(competenceCourseMarkSheet.getEvaluationSeason()).getContent());
    }

    public String getCreationDate() {
        return competenceCourseMarkSheet.getCreationDate().toString();
    }

    public String getExecutionSemester() {
        return competenceCourseMarkSheet.getExecutionSemester().getQualifiedName();
    }

    public String getCheckSum() {
        return competenceCourseMarkSheet.getFormattedCheckSum();
    }

    public String getEvaluationDate() {
        return competenceCourseMarkSheet.getEvaluationDatePresentation();
    }

    public String getLastSubmissionDate() {
        final CompetenceCourseMarkSheetStateChange stateChange = competenceCourseMarkSheet.getStateChangeSet().stream().filter(sc -> sc.isSubmitted()).sorted().findFirst().orElse(null);
        if (stateChange != null) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            return formatter.print(stateChange.getDate());
        } else {
            return "Pauta não finalizada";
        }
    }

    public String getCertifier(){

    }
    public String getLastState() {

    }

}
