package boohaky.github.com.antlogs.log;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class LogConfiguration {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LogLevel.VERBOSE, LogLevel.DEBUG, LogLevel.INFO, LogLevel.WARNING, LogLevel.ERROR})
    public @interface LogLevel {
        int VERBOSE = 0;
        int DEBUG = 1;
        int INFO = 2;
        int WARNING = 3;
        int ERROR = 4;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FileScheduleCreation.EVERY_HOUR, FileScheduleCreation.DAILY, FileScheduleCreation.WEEKLY, FileScheduleCreation.MONTHLY})
    public @interface FileScheduleCreation {
        int EVERY_HOUR = 10;
        int DAILY = 20;
        int WEEKLY = 30;
        int MONTHLY = 40;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SaveLogBuild.DEFAULT_BUILD, SaveLogBuild.DEBUG_BUILD, SaveLogBuild.RELEASE_BUILD})
    public @interface SaveLogBuild {
        int DEFAULT_BUILD = 100;
        int DEBUG_BUILD = 200;
        int RELEASE_BUILD = 300;
    }


    static final String LOG_DEFAULT_DIRECTORY = "LogSaver";
    private static final int DEFAULT_FILE_SIZE = 1024 * 1024;
    private static final DateFormat DEFAULT_DATE_FORMAT = SimpleDateFormat.getDateInstance();

    private String mLogDirectoryName;
    private int mCreationSchedule = FileScheduleCreation.DAILY;
    private long mFileLimitSize = DEFAULT_FILE_SIZE;
    private int mLevelToSaveToFile = LogLevel.VERBOSE;
    private DateFormat mLogDateFormat = DEFAULT_DATE_FORMAT;
    private int mPrintInReleaseBuild = SaveLogBuild.DEFAULT_BUILD;
    private boolean mWriteToFile = true;

    public String getLogDirectoryName() {
        return mLogDirectoryName;
    }

    @FileScheduleCreation
    public int getCreationSchedule() {
        return mCreationSchedule;
    }

    public long getFileLimitSize() {
        return mFileLimitSize;
    }

    @LogLevel
    public int getLevelToSaveToFile() {
        return mLevelToSaveToFile;
    }

    public DateFormat getLogDateFormat() {
        return mLogDateFormat;
    }

    @SaveLogBuild
    public int getPrintInReleaseBuild() {
        return mPrintInReleaseBuild;
    }

    public boolean isWriteToFile() {
        return mWriteToFile;
    }

    public static class Builder {

        private LogConfiguration configuration = new LogConfiguration();

        /**
         * Set directory(with additional path) for store logs, do not include root android folder.
         *
         * @param directoryName name of the directory where logs will be stored.
         * @return Builder instance.
         */
        public Builder setLogDirectory(String directoryName) {
            configuration.mLogDirectoryName = directoryName;
            return this;
        }

        /**
         * Set schedule when new log file should be created.
         *
         * @param creationSchedule constant with interval.
         * @return Builder instance.
         */
        public Builder setCreationSchedule(@FileScheduleCreation int creationSchedule) {
            configuration.mCreationSchedule = creationSchedule;
            return this;
        }

        public Builder setFileLimitSize(long fileLimitSize) {
            configuration.mFileLimitSize = fileLimitSize;
            return this;
        }

        public Builder setLevelToSaveToFile(@LogLevel int levelToSaveToFile) {
            configuration.mLevelToSaveToFile = levelToSaveToFile;
            return this;
        }

        public Builder setLogDateFormat(DateFormat logDateFormat) {
            configuration.mLogDateFormat = logDateFormat;
            return this;
        }

        public Builder setPrintInReleaseBuild(@SaveLogBuild int printInReleaseBuild) {
            configuration.mPrintInReleaseBuild = printInReleaseBuild;
            return this;
        }

        public Builder setWriteToFile(boolean writeToFile) {
            configuration.mWriteToFile = writeToFile;
            return this;
        }

        public LogConfiguration build() {
            return configuration;
        }
    }
}
