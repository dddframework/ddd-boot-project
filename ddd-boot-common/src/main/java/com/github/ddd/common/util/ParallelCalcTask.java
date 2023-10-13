package com.github.ddd.common.util;

import com.github.ddd.common.exception.SystemException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * 并行任务
 *
 * @author ranger
 */
@Slf4j
@Data
public class ParallelCalcTask<T, R> {

    /**
     * Future池
     */
    protected List<Future<R>> todoTask = new ArrayList<>();

    /**
     * 使用的线程池 不指定使用默认线程池
     */
    private ThreadPoolExecutor threadPool;

    /**
     * 需要处理的方法
     */
    private Function<T, R> function;

    /**
     * 需要处理的数据
     */
    private List<T> params;

    /**
     * 投递任务
     */
    private synchronized void deliveryTask() {
        if (threadPool == null) {
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            threadPool = new ThreadPoolExecutor(availableProcessors, availableProcessors, 60L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(1000), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        }
        for (T param : params) {
            Future<R> future = threadPool.submit(() -> function.apply(param));
            todoTask.add(future);
        }
    }


    /**
     * 获取汇总结果
     *
     * @return List<R>
     */
    public List<R> getResult() {
        this.deliveryTask();
        List<R> result = new ArrayList<>();
        for (Future<R> task : todoTask) {
            try {
                result.add(task.get());
            } catch (Exception e) {
                log.error("执行失败", e);
                throw new SystemException("获取结果失败", e);
            }
        }
        todoTask.clear();
        return result;
    }

}
