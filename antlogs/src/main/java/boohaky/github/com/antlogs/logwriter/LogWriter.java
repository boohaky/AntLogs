package boohaky.github.com.antlogs.logwriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import boohaky.github.com.antlogs.log.LogConfiguration;


public class LogWriter implements LogCollector.CollectorCallBack {

    private static final ExecutorService SERVICE = Executors.newSingleThreadExecutor();
    private final LogCollector logCollector;
    private final LogConfiguration configuration;

    public LogWriter(LogConfiguration configuration) {
        logCollector = new LogCollector(this);
        this.configuration = configuration;
    }

    public void process1Log(String text) {
        logCollector.onNextLog(text);
    }

    @Override
    public void onSaveLogsToFiles(List<String> logs) {
        final File file = LogWriterUtils.getLogFile(configuration);
        if (file == null){
            return;
        }
        SERVICE.submit(new SaveLogFileRunnable(file, logs));
    }

    private static class SaveLogFileRunnable implements Runnable {

        final List<String> mLogs;
        final File mFile;

        SaveLogFileRunnable(File file, List<String> logs) {
            mLogs = logs;
            mFile = file;

        }

        @Override
        public void run() {
            saveMessagesToFile();
        }

        private void saveMessagesToFile() {
            try {
                FileWriter fw = new FileWriter(mFile, true);
                for (String message : mLogs) {
                    fw.write(message);
                }
                fw.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
