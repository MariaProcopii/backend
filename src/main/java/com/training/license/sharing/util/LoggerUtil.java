package com.training.license.sharing.util;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class LoggerUtil {

    private LoggerUtil() {
    }

    private static final Logger logger = getLogger(LoggerUtil.class);

    public static void logInfo(String message) {
        logger.info(message);
    }

}
