package zlonglove.cn.network.bean;


import com.google.gson.Gson;

import java.util.List;

/**
 * Created by zhanglong on 2018/3/9.
 */

public class StoryNew {
    /**
     * date : 20180309
     * stories : [{"images":["https://pic2.zhimg.com/v2-d91272229b8d6f77040f2ef1066d2c7d.jpg"],"type":0,"id":9672703,"ga_prefix":"030915","title":"「马冬什么？什么冬梅？马什么梅？」，别笑，你也会这样"},{"title":"企鹅们臭臭的屎，把白白的南极大陆，染成了美美的粉红色","ga_prefix":"030914","images":["https://pic3.zhimg.com/v2-ad09c56151f62fc190c1fe5b1854346e.jpg"],"multipic":true,"type":0,"id":9672976},{"images":["https://pic4.zhimg.com/v2-c9742d5aafdb95145551b83b17cc5503.jpg"],"type":0,"id":9672282,"ga_prefix":"030912","title":"大误 · 这么发朋友圈，你将再也没朋友"},{"images":["https://pic1.zhimg.com/v2-ea53a31dd0be680ba9deb902fd2d7c2c.jpg"],"type":0,"id":9672625,"ga_prefix":"030910","title":"想辞职去卖早餐，同事都说不靠谱，有好的建议吗？"},{"images":["https://pic2.zhimg.com/v2-2318fb5f34dc3ff6dd190b4b20fac32d.jpg"],"type":0,"id":9672472,"ga_prefix":"030909","title":"生物学家能修好电视机吗？"},{"title":"星巴克店员的围裙有 7 种颜色，分别代表什么意思？","ga_prefix":"030908","images":["https://pic4.zhimg.com/v2-39dca8e89025f57e661f172a150cd23b.jpg"],"multipic":true,"type":0,"id":9672270},{"images":["https://pic4.zhimg.com/v2-4928833e59086e4b8dced8b56d327f13.jpg"],"type":0,"id":9672247,"ga_prefix":"030907","title":"马化腾说「发币」很有风险，很多人没搞清，区块链和 ICO 是两码事"},{"images":["https://pic4.zhimg.com/v2-bd9218e996f8ac1f99bdae8bc2620ddb.jpg"],"type":0,"id":9672930,"ga_prefix":"030907","title":"为什么 SpaceX 的发射总是延期？"},{"images":["https://pic1.zhimg.com/v2-2904fe04a5c048934fb4c0246d0fa134.jpg"],"type":0,"id":9672405,"ga_prefix":"030907","title":"和父母无法沟通，你有多绝望？"},{"images":["https://pic4.zhimg.com/v2-4004733b92ef47e8e003aafb11d99bb7.jpg"],"type":0,"id":9672678,"ga_prefix":"030906","title":"瞎扯 · 如何正确地吐槽"}]
     */

    private String date;
    private List<StoriesBean> stories;

    public static StoryNew objectFromData(String str) {
        return new Gson().fromJson(str, StoryNew.class);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public static class StoriesBean {
        /**
         * images : ["https://pic2.zhimg.com/v2-d91272229b8d6f77040f2ef1066d2c7d.jpg"]
         * type : 0
         * id : 9672703
         * ga_prefix : 030915
         * title : 「马冬什么？什么冬梅？马什么梅？」，别笑，你也会这样
         * multipic : true
         */

        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private boolean multipic;
        private List<String> images;

        public static StoriesBean objectFromData(String str) {

            return new Gson().fromJson(str, StoriesBean.class);
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isMultipic() {
            return multipic;
        }

        public void setMultipic(boolean multipic) {
            this.multipic = multipic;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        @Override
        public String toString() {
            return "StoriesBean{" +
                    "type=" + type +
                    ", id=" + id +
                    ", ga_prefix='" + ga_prefix + '\'' +
                    ", title='" + title + '\'' +
                    ", multipic=" + multipic +
                    ", images=" + images +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "StoryNew{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                '}';
    }
}
