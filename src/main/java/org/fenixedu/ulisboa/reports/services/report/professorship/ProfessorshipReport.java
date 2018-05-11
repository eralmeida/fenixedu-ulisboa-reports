package org.fenixedu.ulisboa.reports.services.report.professorship;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftProfessorship;
import org.fenixedu.ulisboa.reports.util.ULisboaReportsUtil;

public class ProfessorshipReport implements Comparable<ProfessorshipReport> {

    private ShiftProfessorship shiftProfessorship;
    private List<Degree> shiftDegrees;

    public ProfessorshipReport(final ShiftProfessorship shiftProfessorship) {
        this.shiftProfessorship = shiftProfessorship;
        this.shiftDegrees = getShiftDegrees();
    }

    private ArrayList<Degree> getShiftDegrees() {
        return getShift().getExecutionCourse().getExecutionDegrees().stream().map(o -> o.getDegree())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    protected Shift getShift() {
        return shiftProfessorship.getShift();
    }

    @Override
    public int compareTo(final ProfessorshipReport o) {

        final Comparator<ProfessorshipReport> byTeacher =
                (x, y) -> Collator.getInstance().compare(x.getTeacherName(), y.getTeacherName());

        final Comparator<ProfessorshipReport> bySemesterAndYear = (x, y) -> ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR
                .compare(x.getExecutionPeriod(), y.getExecutionPeriod());

        return byTeacher.thenComparing(bySemesterAndYear).compare(this, o);
    }

    public String getTeacherName() {
        final Person person = getTeacherPerson();
        return person == null ? null : person.getName();
    }

    protected Person getTeacherPerson() {
        Professorship professorship = getProfessorship();
        return professorship == null ? null : professorship.getPerson();
    }

    protected Professorship getProfessorship() {
        return shiftProfessorship.getProfessorship();
    }

    public ExecutionSemester getExecutionPeriod() {
        final ExecutionCourse executionCourse = getExecutionCourse();
        return executionCourse == null ? null : executionCourse.getExecutionPeriod();
    }

    protected ExecutionCourse getExecutionCourse() {
        Shift shift = getShift();
        return shift == null ? null : shift.getExecutionCourse();
    }

    public String getTeacherUsername() {
        final Person person = getTeacherPerson();
        return person == null ? null : person.getUsername();
    }

    public String getTeacherDepartment() {
        return Optional.ofNullable(getProfessorship()).map(o -> o.getTeacher())
                .map(o -> o.getTeacherAuthorization(getExecutionPeriod().getAcademicInterval()).get()).map(o -> o.getDepartment())
                .map(o -> o.getNameI18n().getContent()).orElse(null);
    }

    public String getIsResponsible() {
        return getProfessorship().isResponsibleFor() ? ULisboaReportsUtil.bundle("yes") : ULisboaReportsUtil.bundle("no");
    }

    public String getExecutionYearName() {
        final ExecutionYear executionYear = getExecutionYear();
        return executionYear == null ? null : executionYear.getQualifiedName();
    }

    public ExecutionYear getExecutionYear() {
        final ExecutionCourse executionCourse = getExecutionCourse();
        return executionCourse == null ? null : executionCourse.getExecutionYear();
    }

    public String getExecutionSemesterName() {
        final ExecutionSemester executionSemester = getExecutionPeriod();
        return executionSemester == null ? null : executionSemester.getName();
    }

    public String getExecutionCourseName() {
        return getExecutionCourse().getNameI18N().getContent();
    }

    public String getShiftDegreesNames() {
        return this.shiftDegrees.stream().map(degree -> degree.getNameI18N().getContent()).collect(Collectors.joining(";"));
    }

    public String getShiftDegreesCodes() {
        return this.shiftDegrees.stream().map(degree -> degree.getCode()).collect(Collectors.joining(";"));
    }

    public String getClassesName() {
        final Shift shift = getShift();
        return shift == null ? null : shift.getClassesPrettyPrint();
    }

    public String getShiftName() {
        final Shift shift = getShift();
        return shift == null ? null : shift.getNome();
    }

    public String getShiftTypeName() {
        final Shift shift = getShift();
        return shift == null ? null : shift.getShiftTypesPrettyPrint();
    }

    public String getShiftOcupation() {
        return Integer.toString(getShift().getStudentsSet().size());
    }

    public String getShiftCapacity() {
        return Integer.toString(getShift().getLotacao());
    }

    public String getTotalHours() {
        return minutesToHours(getTotalMinutes());
    }

    private String minutesToHours(long min) {
        int t = (int) min;
        int hours = t / 60;
        int minutes = t % 60;
        return String.format("%d:%02d", hours, minutes);
    }

    private long getTotalMinutes() {
        return getShift().getAssociatedLessonsSet().stream()
                .map(l -> (l.getAllLessonDatesWithoutInstanceDates().size() + l.getLessonInstancesSet().size())
                        * l.getTotalDuration().getStandardMinutes())
                .collect(Collectors.summingLong(i -> i));
    }

    public String getTeacherHours() {
        return getAllocationPercentage() == null ? null : minutesToHours(
                (long) (getAllocationPercentage() / 100 * getTotalMinutes()));
    }

    public Double getAllocationPercentage() {
        return getShiftProfessorship().getPercentage();
    }

    public ShiftProfessorship getShiftProfessorship() {
        return shiftProfessorship;
    }

}