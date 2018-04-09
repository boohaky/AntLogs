package boohaky.github.com.antlogs.log;

import boohaky.github.com.antlogs.BuildConfig;
import boohaky.github.com.antlogs.logwriter.LogWriter;
import boohaky.github.com.antlogs.utils.LogUtil;


public class LogInstance {

    private final LogConfiguration mLogConfiguration;
    private final LogWriter logWriter;

    LogInstance() {
        this(new LogConfiguration.Builder()
                .setLogDirectory(LogConfiguration.LOG_DEFAULT_DIRECTORY)
                .build());
    }

    public LogInstance(LogConfiguration logConfiguration) {
        mLogConfiguration = logConfiguration;
        logWriter = new LogWriter(mLogConfiguration);
    }

    public void v(Object object, String format, Object ...messageFormat) {
        checkConfiguration();
        final String tag = LogUtil.getTagFromObject(object);
        final String message = LogUtil.getLogMessage(format, messageFormat);
        v(tag, message);
    }

    private void v(String tag, String message) {
        checkConfiguration();
        android.util.Log.v(tag, message);
        printSaveLog(LogConfiguration.LogLevel.VERBOSE, tag, message);
    }

    public void d(Object object, String format, Object ...messageFormat) {
        checkConfiguration();
        final String tag = LogUtil.getTagFromObject(object);
        final String message = LogUtil.getLogMessage(format, messageFormat);
        d(tag, message);

    }

    public void d(String tag, String message) {
        checkConfiguration();
        android.util.Log.d(tag, message);
        printSaveLog(LogConfiguration.LogLevel.DEBUG, tag, message);
    }

    public void i(Object object,  String format, Object ...messageFormat) {
        checkConfiguration();
        final String tag = LogUtil.getTagFromObject(object);
        final String message = LogUtil.getLogMessage(format, messageFormat);
        i(tag, message);
    }

    public void i(String tag, String message) {
        checkConfiguration();
        android.util.Log.i(tag, message);
        printSaveLog(LogConfiguration.LogLevel.INFO, tag, message);
    }

    public void w(Object object, String format, Object ...messageFormat) {
        checkConfiguration();
        final String tag = LogUtil.getTagFromObject(object);
        final String message = LogUtil.getLogMessage(format, messageFormat);
        w(tag, message);
    }

    public void w(String tag, String message) {
        checkConfiguration();
        android.util.Log.w(tag, message);
        printSaveLog(LogConfiguration.LogLevel.WARNING, tag, message);
    }

    public void e(Object object, String format, Object ...messageFormat) {
        checkConfiguration();
        final String tag = LogUtil.getTagFromObject(object);
        final String message = LogUtil.getLogMessage(format, messageFormat);
        e(tag, message);
    }

    public void e(String tag, String message) {
        checkConfiguration();
        android.util.Log.e(tag, message);
        printSaveLog(LogConfiguration.LogLevel.ERROR, tag, message);
    }

    public void e(Object object, Throwable e, String format, Object ...messageFormat) {
        checkConfiguration();
        final String tag = LogUtil.getTagFromObject(object);
        final String message = LogUtil.getLogMessage(format, messageFormat);
        e(tag, e, message);
    }

    public void e(String tag, Throwable e, String message) {
        checkConfiguration();
        android.util.Log.e(tag, message);
        printSaveLog(LogConfiguration.LogLevel.ERROR, tag, message);
    }

    public void printSaveLog(@LogConfiguration.LogLevel int logLevel, String tag, String message) {
        if (isSaveLogToFile(logLevel)) {
            final String text = LogUtil.getLogText(logLevel, tag, message,
                    mLogConfiguration.getLogDateFormat(), System.currentTimeMillis());
            logWriter.process1Log(text);
        }
    }

    private boolean isSaveLogToFile(int logLevel) {
        boolean isWriteToFIle = mLogConfiguration.isWriteToFile();
        if (!isWriteToFIle) {
            return false;
        }
        int printBuild = mLogConfiguration.getPrintInReleaseBuild();
        return logLevel >= mLogConfiguration.getLevelToSaveToFile()
                && (printBuild == LogConfiguration.SaveLogBuild.DEFAULT_BUILD
                || (printBuild == LogConfiguration.SaveLogBuild.DEBUG_BUILD && BuildConfig.DEBUG)
                || printBuild == LogConfiguration.SaveLogBuild.RELEASE_BUILD && !BuildConfig.DEBUG);
    }

    private void checkConfiguration() {
        if (mLogConfiguration == null){
            throw new IllegalStateException("Log Configuration must be set " +
                    "before saving custom logs");
        }
    }
}
