package boohaky.github.com.antlogs.logwriter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class LogCollector {

    interface CollectorCallBack {
        void onSaveLogsToFiles(List<String> objects);
    }

    private static final int DELAY = 2;

    private final ScheduledExecutorService delayedService = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService filterService = Executors.newSingleThreadExecutor();
    private List<String> mLogList = new LinkedList<>();
    private Future mCollectorTask;
    private CollectorCallBack mCallBack;
    private Lock collectorLock = new ReentrantLock();

    LogCollector(CollectorCallBack callBack) {
        mCallBack = callBack;
    }

    void onNextLog(final String message) {
        cancelTask();
        filterService.submit(new Runnable() {
            @Override
            public void run() {
                filterObjects(message);
            }
        });
        mCollectorTask = delayedService.schedule(new Runnable() {
            @Override
            public void run() {
                collectorLock.lock();
                final List<String> list2 = new LinkedList<>(mLogList);
                mLogList.clear();
                collectorLock.unlock();
                mCallBack.onSaveLogsToFiles(list2);
            }
        }, DELAY, TimeUnit.SECONDS);
    }

    private void cancelTask() {
        if (mCollectorTask != null && (!mCollectorTask.isCancelled() || !mCollectorTask.isDone())) {
            mCollectorTask.cancel(false);
        }
    }

    private void filterObjects(String message) {
        collectorLock.lock();
        mLogList.add(message);
        collectorLock.unlock();
    }

}
