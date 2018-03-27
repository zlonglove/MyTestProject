/**
 *
 */
package com.ISHello.Module;

import com.in.zlonglove.commonutil.minify.Gsonable;

import java.io.Serializable;

public class BarInfoModel extends Gsonable implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ArticleId;// 置顶帖子Id
    private String ArticleTitle;// 置顶帖子标题
    private String IsComment;// 0-允许评论 1-禁止评论
    private String FansCount;// 粉丝数
    private String TopicCount;// 话题数
    private String GroupCount;// 本吧公开群数量
    private String FilesServerUrl;// 文件服务器前缀地址
    private String BarId;// 吧id
    private String IsAttention;// 我是否关注

    public BarInfoModel() {

    }

    public BarInfoModel(String sArticleId, String sArticleTitle, String sIsComment, String sFansCount, String sTopicCount, String sGroupCount,
                        String sFilesServerUrl, String sBarId, String sBarIcon, String sIsAttention) {
        this.ArticleId = sArticleId;
        this.ArticleTitle = sArticleTitle;
        this.IsComment = sIsComment;
        this.FansCount = sFansCount;
        this.TopicCount = sTopicCount;
        this.GroupCount = sGroupCount;
        this.FilesServerUrl = sFilesServerUrl;
        this.BarId = sBarId;
        this.IsAttention = sIsAttention;
    }

    public String getArticleId() {
        return ArticleId;
    }

    public void setArticleId(String sArticleId) {
        ArticleId = sArticleId;
    }

    public String getArticleTitle() {
        return ArticleTitle;
    }

    public void setArticleTitle(String sArticleTitle) {
        ArticleTitle = sArticleTitle;
    }

    public String getIsComment() {
        return IsComment;
    }

    public void setIsComment(String sIsComment) {
        IsComment = sIsComment;
    }

    public String getFansCount() {
        return FansCount;
    }

    public void setFansCount(String sFansCount) {
        FansCount = sFansCount;
    }

    public String getTopicCount() {
        return TopicCount;
    }

    public void setTopicCount(String sTopicCount) {
        TopicCount = sTopicCount;
    }

    public String getGroupCount() {
        return GroupCount;
    }

    public void setGroupCount(String sGroupCount) {
        GroupCount = sGroupCount;
    }

    public String getFilesServerUrl() {
        return FilesServerUrl;
    }

    public void setFilesServerUrl(String sFilesServerUrl) {
        FilesServerUrl = sFilesServerUrl;
    }

    public String getBarId() {
        return BarId;
    }

    public void setBarId(String sBarId) {
        BarId = sBarId;
    }

    public String getIsAttention() {
        return IsAttention;
    }

    public void setIsAttention(String sIsAttention) {
        IsAttention = sIsAttention;
    }

    public String toString() {
        return "BarInfoModel [ArticleId=" + ArticleId + ", ArticleTitle=" + ArticleTitle + ", IsComment=" + IsComment + ", FansCount=" + FansCount
                + ", TopicCount=" + TopicCount + ", GroupCount=" + GroupCount + ", FilesServerUrl=" + FilesServerUrl + ", BarId=" + BarId
                + ", IsAttention=" + IsAttention + "]";
    }
}
