package com.manerfan.leetcode.question_1111_1120;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * 我们提供了一个类：
 *
 * public class Foo {
 *   public void one() { print("one"); }
 *   public void two() { print("two"); }
 *   public void three() { print("three"); }
 * }
 * 三个不同的线程将会共用一个 Foo 实例。
 *
 * 线程 A 将会调用 one() 方法
 * 线程 B 将会调用 two() 方法
 * 线程 C 将会调用 three() 方法
 * 请设计修改程序，以确保 two() 方法在 one() 方法之后被执行，three() 方法在 two() 方法之后被执行。
 *
 *  
 *
 * 示例 1:
 *
 * 输入: [1,2,3]
 * 输出: "onetwothree"
 * 解释:
 * 有三个线程会被异步启动。
 * 输入 [1,2,3] 表示线程 A 将会调用 one() 方法，线程 B 将会调用 two() 方法，线程 C 将会调用 three() 方法。
 * 正确的输出是 "onetwothree"。
 * 示例 2:
 *
 * 输入: [1,3,2]
 * 输出: "onetwothree"
 * 解释:
 * 输入 [1,3,2] 表示线程 A 将会调用 one() 方法，线程 B 将会调用 three() 方法，线程 C 将会调用 two() 方法。
 * 正确的输出是 "onetwothree"。
 *  
 *
 * 注意:
 *
 * 尽管输入中的数字似乎暗示了顺序，但是我们并不保证线程在操作系统中的调度顺序。
 *
 * 你看到的输入格式主要是为了确保测试的全面性。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-in-order
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author Maner.Fan
 * @date 2019/11/4
 */
public class Q1114_Print_In_Order {
    // 使用 信号量Semaphore 或者 计数器CountDownLatch

    private Semaphore cdFirst = new Semaphore(0);
    private Semaphore cdSecond = new Semaphore(0);

    public Q1114_Print_In_Order() {}

    public void first(Runnable printFirst) throws InterruptedException {
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
        cdFirst.release();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        cdFirst.acquire();
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();
        cdSecond.release();
    }

    public void third(Runnable printThird) throws InterruptedException {
        cdSecond.acquire();
        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
    }

    //////////////////////////////////////////////
    ////////////// ↓↓↓ for test ↓↓↓ //////////////
    //////////////////////////////////////////////

    public static void main(String[] args) {
        List<Runnable> runnables = Arrays.asList(
            () -> System.out.println("first"),
            () -> System.out.println("second"),
            () -> System.out.println("third")
        );

        for (int i = 0; i < 10; i++) {
            Q1114_Print_In_Order pio = new Q1114_Print_In_Order();

            List<ConsumerWithException<Runnable, InterruptedException>> prints = Arrays.asList(
                pio::first, pio::second, pio::third
            );

            for (int j = 0; j < 3; j++) {
                int index = (i + j) % 3;
                new Thread(
                    () -> {
                        try {
                            prints.get(index).accept(runnables.get(index));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    },
                    String.format("Thread-%d-%d", i + 1, index + 1)
                ).start();
            }
        }
    }

    @FunctionalInterface
    public interface ConsumerWithException<T, E extends Exception> {
        void accept(T t) throws E;
    }
}
