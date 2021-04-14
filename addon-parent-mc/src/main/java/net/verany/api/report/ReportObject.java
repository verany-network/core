package net.verany.api.report;

import lombok.Getter;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;

import java.util.ArrayList;
import java.util.List;

public class ReportObject extends DatabaseLoader implements IReportObject {

    public ReportObject(VeranyProject project) {
        super(project, "reports", "network");
    }

    @Override
    public void load() {
        load(new LoadInfo<>("reports", ReportLoadObject.class, new ReportLoadObject()));
    }

    @Override
    public void update() {
        save("reports");
    }

    @Override
    public List<ReportEntry> getOpenReports() {
        return getData(ReportLoadObject.class).getOpenReports();
    }

    @Getter
    public static class ReportLoadObject extends DatabaseLoadObject {

        private final List<ReportEntry> openReports = new ArrayList<>();
        private final List<ReportEntry> closedReports = new ArrayList<>();

        public ReportLoadObject() {
            super("reports");
        }
    }
}
