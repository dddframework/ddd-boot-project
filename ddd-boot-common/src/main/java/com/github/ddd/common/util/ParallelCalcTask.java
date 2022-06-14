package com.github.ddd.common.util;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

/**
 * 并行任务
 *
 * @author ranger
 */
@RequiredArgsConstructor
public class ParallelCalcTask<T, R> {

    /**
     * Future池
     */
    private final List<Future<R>> todoTask = new ArrayList<>();

    /**
     * 使用的线程池
     */
    private final ThreadPoolExecutor threadPool;

    /**
     * 需要处理的方法
     */
    private final Function<T, R> function;

    /**
     * 需要处理的数据
     */
    private final List<T> params;

    /**
     * 投递任务
     */
    private void deliveryTask() {
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
                throw new RuntimeException("获取结果失败", e);
            }
        }
        todoTask.clear();
        return result;
    }

}
