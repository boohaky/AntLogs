package boohaky.github.com.antlogs.log;

import android.os.Bundle;
import android.support.annotation.MainThread;

import boohaky.github.com.antlogs.utils.LogUtil;


public class Log {

    private static LogInstance LOG;

    @MainThread
    public synchronized static void initialize(LogConfiguration logConfiguration) {
        if (LOG != null) {
            return;
        }
        LOG = new LogInstance(logConfiguration);
    }

    /**
     * //TODO add later
     *
     * @param bundle
     */
    @MainThread
    public synchronized static void initialize(Bundle bundle) {
        initialize(LogUtil.getConfigurationFromBundle(bundle));
    }

    public static void v(Object object, String format, Object... messageFormat) {
        LOG.v(object, format, messageFormat);
    }

    public static void v(String tag, String message) {
        LOG.v(tag, message);
    }

    public static void d(Object object, String format, Object... messageFormat) {
        LOG.d(object, format, messageFormat);
    }

    public static void d(String tag, String message) {
        LOG.d(tag, message);

    }

    public static void i(Object object, String format, Object... messageFormat) {
        LOG.i(object, format, messageFormat);
    }

    public static void i(String tag, String message) {
        LOG.i(tag, message);
    }

    public static void w(Object object, String format, Object... messageFormat) {
        LOG.w(object, format, messageFormat);

    }

    public static void w(String tag, String message) {
        LOG.w(tag, message);
    }

    public static void e(Object object, String format, Object... messageFormat) {
        LOG.e(object, format, messageFormat);
    }

    public static void e(String tag, String message) {
        LOG.e(tag, message);
    }

    public static void e(Object object, Throwable e, String format, Object... messageFormat) {
        LOG.e(object, e, format, messageFormat);
    }

    public static void e(String tag, Throwable e, String message) {
        LOG.e(tag, e, message);
    }

}
