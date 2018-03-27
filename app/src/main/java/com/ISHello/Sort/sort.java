package com.ISHello.Sort;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhanglong on 2018/1/25.
 */

public class sort {
    private int number = 0;

    public class VoteObject {
        String name;
        int age;
        int voteNum;

        public VoteObject(String name, int age, int voteNum) {
            this.name = name;
            this.age = age;
            this.voteNum = voteNum;
        }
    }

    public void test() {
        List<VoteObject> listTemp = new ArrayList<>();
        listTemp.add(new VoteObject("密瓜", 20, 666));
        listTemp.add(new VoteObject("苹果", 25, 999));
        listTemp.add(new VoteObject("橙子", 25, 888));
        listTemp.add(new VoteObject("蜜桃", 22, 668));
        listTemp.add(new VoteObject("芒果", 21, 668));
        listTemp.add(new VoteObject("橘子", 20, 668));
        listTemp.add(new VoteObject("香蕉", 21, 898));
        // 这里排序规则：按票数从高到低排，票数相同按年龄从低到高排
        Comparator<VoteObject> comparator = new Comparator<VoteObject>() {
            public int compare(VoteObject o1, VoteObject o2) {
                number++;
                Log.e("排序次数","--->"+number+o1.name+"/"+o2.name);
                int result = o2.voteNum - o1.voteNum; // 投票按[降序]
                if (result == 0) { // 票数相等
                    return o1.age - o2.age; // 按年龄排序按[升序]
                } else {
                    return result;
                }
            }
        };
        Collections.sort(listTemp, comparator);
        for (VoteObject obj : listTemp) {
            Log.e("排序后", obj.name + "  " + obj.age + "  " + obj.voteNum);
        }
    }

}
