package boohaky.github.com.antlogs.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import boohaky.github.com.antlogs.log.LogConfiguration;


public class LogUtil {

    private static final int UTILS_NAME_LENGTH = 5;
    private static final String ANONYMOUS = "AnonymousClass";

    public static String getTagFromObject(@NonNull Object object) {
        final Class tagClass = object.getClass();
        final String className = tagClass.isAnonymousClass() ? ANONYMOUS : tagClass.getSimpleName();
        final String methodName = getCallingMethodName();
        return String.format(Locale.US, "%s : %s ", className, methodName);
    }

    private static String getCallingMethodName() {
        final String thisPackageName = getMainLogPackageName();
        final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        boolean isTraceFound = false;
        for (StackTraceElement trace : traces) {
            if (isTraceFound) {
                if (!trace.getClassName().startsWith(thisPackageName)) {
                    return trace.getMethodName();
                }
            } else if (trace.getClassName().startsWith(thisPackageName)) {
                isTraceFound = true;
            }
        }
        return "_";
    }

    @NonNull
    private static String getMainLogPackageName() {
        final String packageName = LogUtil.class.getPackage().getName();
        return packageName.substring(0, packageName.length() - UTILS_NAME_LENGTH - 1);
    }

    public static String getLogMessage(String message, @Nullable Object... messageFormat) {
        if (messageFormat == null) {
            return message;
        }
        return String.format(Locale.US, message, messageFormat);
    }

    public static String getLogText(@LogConfiguration.LogLevel int logLevel, String tag,
                                    String message, DateFormat logDateFormat, long time) {
        final String threadName = Thread.currentThread().getName();
        final String logLevelString = LogUtil.getTagStringByLevel(logLevel);
        final String logTime = logDateFormat.format(new Date(time));
        return String.format(Locale.US, "%s %s  %s %s %s \n", logTime, threadName, logLevelString,
                tag, message);
    }

    private static String getTagStringByLevel(@LogConfiguration.LogLevel int logLevel) {
        switch (logLevel) {
            case LogConfiguration.LogLevel.VERBOSE:
                return "V";
            case LogConfiguration.LogLevel.DEBUG:
                return "D";
            case LogConfiguration.LogLevel.INFO:
                return "I";
            case LogConfiguration.LogLevel.WARNING:
                return "W";
            case LogConfiguration.LogLevel.ERROR:
                return "E";
            default:
                throw new IllegalArgumentException("log level was not found");
        }
    }

    public static LogConfiguration getConfigurationFromBundle(Bundle bundle) {
        LogConfiguration.Builder builder = new LogConfiguration.Builder();
        final boolean writeToFile = bundle.getBoolean(Configuration.LOG_WRITE_FILE_EXTRA, false);
        builder.setWriteToFile(writeToFile);
        if (!writeToFile) {
            return builder.build();
        }
        final String path = bundle.getString(Configuration.LOG_DIRECTION_EXTRA);
        if (path != null) {
            builder.setLogDirectory(path);
        }
        final int scheduleExtra = bundle.getInt(Configuration.LOG_SCHEDULE_EXTRA, -1);
        if (scheduleExtra != -1) {
            builder.setCreationSchedule(scheduleExtra);
        }
        final long limitSizeExtra = bundle.getLong(Configuration.LOG_LIMIT_SIZE_EXTRA, -1);
        if (limitSizeExtra != -1) {
            builder.setFileLimitSize(scheduleExtra);
        }
        final int minLogLevel = bundle.getInt(Configuration.LOG_MIN_LEVEL_EXTRA, -1);
        if (minLogLevel != -1) {
            builder.setLevelToSaveToFile(minLogLevel);
        }
        final int printBuild = bundle.getInt(Configuration.LOG_PRINT_BUILD_EXTRA, -1);
        if (printBuild != -1) {
            builder.setPrintInReleaseBuild(printBuild);
        }
        return builder.build();
    }
}
