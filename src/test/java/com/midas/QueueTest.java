package com.midas;

import java.util.LinkedList;
import java.util.Queue;

public class QueueTest {

    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<String>();
        queue.offer("Hello");
        queue.offer("World!");
        queue.offer("你好！");
        System.out.println(queue.size());
        String str;
        System.out.println(queue.size());
        while ((str = queue.poll()) != null) {
            System.out.println(str);
        }
        System.out.println();
        System.out.println("队列："+queue.size());
    }

}
