package com.eventbridge.booking.config;

import com.eventbridge.booking.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Releases expired provisional holds so inventory / vendor slots become available again.
 * Runs every 15 minutes.
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {

    private static final Logger log = LoggerFactory.getLogger(SchedulingConfig.class);

    private final BookingService bookingService;

    public SchedulingConfig(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Scheduled(fixedRate = 900_000) // 15 minutes
    public void releaseExpiredHolds() {
        int released = bookingService.releaseExpiredHolds();
        if (released > 0) {
            log.info("Released {} expired provisional hold(s)", released);
        }
    }
}
