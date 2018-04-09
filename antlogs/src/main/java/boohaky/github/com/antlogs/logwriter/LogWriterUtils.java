package boohaky.github.com.antlogs.logwriter;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import boohaky.github.com.antlogs.log.LogConfiguration;


class LogWriterUtils {

    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("dd_mm_yyyy_hhmm",
            Locale.US);
    private static final String LOG_FILE_EXTENSION = ".txt";


    @Nullable
    static File getLogFile(LogConfiguration mLogConfiguration) {
        final File directory = getLogDirectory(mLogConfiguration.getLogDirectoryName());
        if (directory == null || !directory.exists()) {
            return null;
        }
        final File latestFile = getLatestLogFile(directory);
        if (latestFile == null || shouldCreateNewFile(latestFile, mLogConfiguration)) {
            return createNewLogFile(directory);
        } else {
            return latestFile;
        }
    }

    private static boolean shouldCreateNewFile(File latestFile, LogConfiguration mLogConfiguration) {
        final long scheduleDelta = getScheduleDelta(mLogConfiguration.getCreationSchedule());
        final long fileLimitSize = mLogConfiguration.getFileLimitSize();
        if (latestFile.length() >= fileLimitSize) {
            return true;
        }
        final long fileCreateDate;
        try {
            final String fileName = latestFile.getName();
            final String fileDate = fileName.substring(0, fileName.lastIndexOf("."));
            fileCreateDate = DEFAULT_FORMAT.parse(fileDate).getTime();
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            return true;
        }
        return (new Date().getTime() - fileCreateDate) > scheduleDelta;
    }

    private static long getScheduleDelta(@LogConfiguration.FileScheduleCreation
                                                 int creationSchedule) {
        switch (creationSchedule) {
            case LogConfiguration.FileScheduleCreation.EVERY_HOUR:
                return TimeUnit.HOURS.toMillis(1);
            case LogConfiguration.FileScheduleCreation.DAILY:
                return TimeUnit.DAYS.toMillis(1);
            case LogConfiguration.FileScheduleCreation.WEEKLY:
                return TimeUnit.DAYS.toMillis(7);
            case LogConfiguration.FileScheduleCreation.MONTHLY:
                return TimeUnit.DAYS.toMillis(30);
            default:
                throw new IllegalArgumentException("Schedule was not found");
        }
    }

    @Nullable
    private static File getLogDirectory(String logDirectory) {
        if (TextUtils.isEmpty(logDirectory)) {
            throw new IllegalStateException("Log directory is not set, set before invoke any operations");
        }
        final File file = new File(Environment.getExternalStorageDirectory(), logDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    @Nullable
    private static File getLatestLogFile(File logDirecory) {
        final File[] files = logDirecory.listFiles();
        if (files == null) {
            return null;
        }
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Long.valueOf(o1.lastModified()).compareTo(o2.lastModified());
            }
        });
        if (files.length == 0) {
            return null;
        } else {
            return files[0];
        }
    }

    private static File createNewLogFile(File directory) {
        final String fileName = DEFAULT_FORMAT.format(new Date(System.currentTimeMillis()))
                + LOG_FILE_EXTENSION;
        return new File(directory, fileName);
    }

}
