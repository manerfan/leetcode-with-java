package com.manerfan.leetcode.question_1111_1120;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

/**
 * 假设有这么一个类：
 *
 * class ZeroEvenOdd {
 *   public ZeroEvenOdd(int n) { ... }      // 构造函数
 * public void zero(printNumber) { ... }  // 仅打印出 0
 * public void even(printNumber) { ... }  // 仅打印出 偶数
 * public void odd(printNumber) { ... }   // 仅打印出 奇数
 * }
 * 相同的一个 ZeroEvenOdd 类实例将会传递给三个不同的线程：
 *
 * 线程 A 将调用 zero()，它只输出 0 。
 * 线程 B 将调用 even()，它只输出偶数。
 * 线程 C 将调用 odd()，它只输出奇数。
 * 每个线程都有一个 printNumber 方法来输出一个整数。请修改给出的代码以输出整数序列 010203040506... ，其中序列的长度必须为 2n。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：n = 2
 * 输出："0102"
 * 说明：三条线程异步执行，其中一个调用 zero()，另一个线程调用 even()，最后一个线程调用odd()。正确的输出为 "0102"。
 *
 * 示例 2：
 *
 * 输入：n = 5
 * 输出："0102030405"
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-zero-even-odd
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author Maner.Fan
 * @date 2019/11/5
 */
public class Q1116_Print_Zero_Even_Odd {
    private int n;

    private Semaphore zeroSemaphore = new Semaphore(1);
    private Semaphore evenSemaphore = new Semaphore(0);
    private Semaphore oddSemaphore = new Semaphore(0);

    public Q1116_Print_Zero_Even_Odd(int n) {
        this.n = n;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            zeroSemaphore.acquire();
            printNumber.accept(0);
            if ((i & 1) == 0) {
                // 唤醒偶数
                evenSemaphore.release();
            } else {
                // 唤醒奇数
                oddSemaphore.release();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            evenSemaphore.acquire();
            printNumber.accept(i);
            // 重新唤醒zero
            zeroSemaphore.release();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            oddSemaphore.acquire();
            printNumber.accept(i);
            // 重新唤醒zero
            zeroSemaphore.release();
        }
    }

    //////////////////////////////////////////////
    ////////////// ↓↓↓ for test ↓↓↓ //////////////
    //////////////////////////////////////////////

    public static void main(String[] args) {
        IntConsumer printNumber = System.out::print;
        Q1116_Print_Zero_Even_Odd print_zero_even_odd = new Q1116_Print_Zero_Even_Odd(11);

        new Thread(() -> {
            try {
                print_zero_even_odd.zero(printNumber);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Thread-zero").start();

        new Thread(() -> {
            try {
                print_zero_even_odd.even(printNumber);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Thread-even").start();

        new Thread(() -> {
            try {
                print_zero_even_odd.odd(printNumber);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Thread-odd").start();
    }

}
