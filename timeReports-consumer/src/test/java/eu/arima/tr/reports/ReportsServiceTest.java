package eu.arima.tr.reports;

import static eu.arima.tr.reports.DayStatus.MISSING_HOURS;
import static eu.arima.tr.reports.DayStatus.RIGHT_HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = { "server.url=http://localhost:8083" })
@AutoConfigureStubRunner(ids = { "eu.arima.tr:timeReports-producer:+:stubs:8083" })
public class ReportsServiceTest {

	@Autowired
	private ReportsService reportsService;

	@Test
	@DisplayName("Given a worklog with 8 hours duration the status is RIGHT_HOURS")
	void when_the_worklog_for_the_resquested_day_is_8_hours_the_status_is_RIGHT_HOURS() throws InterruptedException {
		LocalDate day = LocalDate.of(2020, 05, 01);
		String username = "JESSI";

		DayStatusSummary result = reportsService.getDayStatusSummaryForWorkerAndDay(username, day);

		assertStatusEquals(RIGHT_HOURS, result);

	}

	@Test
	@DisplayName("Given a list of worklogs with more than 8 hours duration the status is MISSING_HOURS")
	void when_the_worklogs_for_resquested_day_are_more_than_8_hours_the_status_is_MISSING_HOURS()
			throws InterruptedException {
		LocalDate day = LocalDate.of(2020, 05, 05);
		String username = "JESSI";

		DayStatusSummary result = reportsService.getDayStatusSummaryForWorkerAndDay(username, day);

		assertStatusEquals(DayStatus.EXTRA_HOURS, result);

	}

	@Test
	@DisplayName("Given a list of worklogs with less than 8 hours duration the status is MISSING_HOURS")
	void when_the_worklogs_for_resquested_day_are_less_than_8_hours_the_status_is_MISSING_HOURS()
			throws InterruptedException {
		LocalDate day = LocalDate.of(2020, 05, 10);
		String username = "JESSI";

		DayStatusSummary result = reportsService.getDayStatusSummaryForWorkerAndDay(username, day);

		assertStatusEquals(MISSING_HOURS, result);

	}

	@Test
	@DisplayName("Given the username of a worker the status result has that username")
	void the_status_result_belongs_to_the_requested_worker() throws InterruptedException {
		LocalDate day = LocalDate.of(2020, 05, 01);
		String username = "JESSI";

		DayStatusSummary result = reportsService.getDayStatusSummaryForWorkerAndDay(username, day);

		assertEquals(username, result.getWorkerUserName());

	}

	@Test
	@DisplayName("Given a date the status result has that date")
	void the_status_result_belongs_to_the_requested_day() throws InterruptedException {
		LocalDate day = LocalDate.of(2020, 05, 01);
		String username = "JESSI";

		DayStatusSummary result = reportsService.getDayStatusSummaryForWorkerAndDay(username, day);

		assertEquals(day, result.getDate());

	}

	private void assertStatusEquals(DayStatus dayStatus, DayStatusSummary result) {
		assertEquals(1, result.getStatusList().size());
		assertEquals(dayStatus, result.getStatusList().get(0));
	}

}
