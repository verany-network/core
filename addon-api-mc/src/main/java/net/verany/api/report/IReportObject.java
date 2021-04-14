package net.verany.api.report;

import java.util.List;

public interface IReportObject {

    void load();

    void update();

    List<ReportEntry> getOpenReports();

}
