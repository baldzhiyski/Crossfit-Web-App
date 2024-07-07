package com.softuni.crossfitapp.service.schedulers;

import com.softuni.crossfitapp.service.EventService;
import com.softuni.crossfitapp.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventsCacheEvictScheduler {
    private static final Logger logger = LoggerFactory.getLogger(EventsCacheEvictScheduler.class);

    @CacheEvict(value = "events", allEntries = true)
    @Scheduled(cron = "0 0 0 */5 * ?") // Every 5 days at midnight
    public void evictAllCacheValues() {
        // This method will be executed every 5 days to clear the 'events' cache
        logger.info(Constants.EVENT_CACHE);
    }
}
