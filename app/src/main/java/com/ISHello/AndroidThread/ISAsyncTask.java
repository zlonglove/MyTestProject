package com.ISHello.AndroidThread;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by kfzx-zhanglong on 2016/4/25.
 * Company ICBC
 * <p>
 * onPreExecute()在主线程中执行，在异步任务执行前，此方法会被调用，一般用于做一些准备工作
 * <p>
 * doInBackground(Params...params)在线程池中执行，用于执行异步任务
 * 在此方法中可以通过publishProgress()方法来更新任务进度
 * <p>
 * onProgressUpdate(Progress...values)主线程中执行,values[0]表示进度值,由publishProgress()设置
 * <p>
 * onPostExcute(Result result)主线程中执行，异步任务执行后执行，result参数是后台任务的返回值，即doBackground的返回值
 */

/**
 * 1)更加方便的执行后台任务和在主线程中访问UI
 * 2)不合适进行特别耗时的操作，对于特别耗时的操作任务建议使用线程池
 */
public class ISAsyncTask {
    private final String TAG = "ISAsyncTask";
    private DownLoadFileTask downLoadFileTask;

    public void downLoadFile(String... urls) {
        if (downLoadFileTask == null) {
            downLoadFileTask = new DownLoadFileTask();
        }
        downLoadFileTask.execute(urls);
    }

    /**
     * 参数类型
     * Params-表示参数的类型
     * Progress-表示后台任务的执行进度的类型
     * Result-表示后台任务的返回值类型
     */
    private class DownLoadFileTask extends AsyncTask<String, Integer, Long> {

        /**
         * 主线程中执行，在异步任务被调用前执行，一般用于做一些准备工作
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "--->onPreExecute() call");
        }

        /**
         * 在线程池中执行，用于执行异任务
         *
         * @param urls--传入的参数列表Params
         * @return onPostExecute()的参数输入
         */
        @Override
        protected Long doInBackground(String... urls) {
            Log.i(TAG, "--->doInbackground() call +Thread" + Thread.currentThread().getName());
            int count = urls.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {
                Log.i(TAG, "--->current url==" + urls[i]);
                totalSize += 10;
                publishProgress(count);//更新进度
                if (isCancelled()) {
                    break;
                }
            }
            return totalSize;
        }

        /**
         * 主线程中执行，后台任务的进度执行度发生变化时此方法被调用
         * publishProgress();
         *
         * @param values--更新进度值
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.i(TAG, "--->onProgressUpdate() call progress==" + values[0]);
        }

        /**
         * 主线程中执行，异步任务执行完成后调用，
         *
         * @param aLong--doInBackground()方法的返回值
         */
        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            Log.i(TAG, "--->onPostExecute() call");
        }

        /**
         * 主线程中执行，用于异步任务被取消的时候调用
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i(TAG, "--->onCancelled() call");
        }
    }
}
