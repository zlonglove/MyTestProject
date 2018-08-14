package im.icbc.com.downloadfile.downloadutils.bean;

/**
 * Created by on 2017/7/20.
 * @author zhanglong
 */

public class DownloadInfo {
    private int threadId;// 下载器id
    private long startPos;// 开始点
    private long endPos;// 结束点
    private long compeleteSize;// 完成度
    private String url;// 下载器网络标识

    public DownloadInfo(int threadId, long startPos, long endPos,
                        long compeleteSize, String url) {
        this.threadId = threadId;
        this.startPos = startPos;
        this.endPos = endPos;
        this.compeleteSize = compeleteSize;
        this.url = url;
    }

    public DownloadInfo() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public long getEndPos() {
        return endPos;
    }

    public void setEndPos(long endPos) {
        this.endPos = endPos;
    }

    public long getCompeleteSize() {
        return compeleteSize;
    }

    public void setCompeleteSize(long compeleteSize) {
        this.compeleteSize = compeleteSize;
    }

    @Override
    public String toString() {
        return "DownloadInfo [threadId=" + threadId + ", startPos=" + startPos
                + ", endPos=" + endPos + ", compeleteSize=" + compeleteSize
                + "]";
    }
}
