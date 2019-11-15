package com.manerfan.leetcode.question_1111_1120;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * 现在有两种线程，氢 oxygen 和氧 hydrogen，你的目标是组织这两种线程来产生水分子。
 *
 * 存在一个屏障（barrier）使得每个线程必须等候直到一个完整水分子能够被产生出来。
 *
 * 氢和氧线程会被分别给予 releaseHydrogen 和 releaseOxygen 方法来允许它们突破屏障。
 *
 * 这些线程应该三三成组突破屏障并能立即组合产生一个水分子。
 *
 * 你必须保证产生一个水分子所需线程的结合必须发生在下一个水分子产生之前。
 *
 * 换句话说:
 *
 * 如果一个氧线程到达屏障时没有氢线程到达，它必须等候直到两个氢线程到达。
 * 如果一个氢线程到达屏障时没有其它线程到达，它必须等候直到一个氧线程和另一个氢线程到达。
 * 书写满足这些限制条件的氢、氧线程同步代码。
 *
 *  
 *
 * 示例 1:
 *
 * 输入: "HOH"
 * 输出: "HHO"
 * 解释: "HOH" 和 "OHH" 依然都是有效解。
 * 示例 2:
 *
 * 输入: "OOHHHH"
 * 输出: "HHOHHO"
 * 解释: "HOHHHO", "OHHHHO", "HHOHOH", "HOHHOH", "OHHHOH", "HHOOHH", "HOHOHH" 和 "OHHOHH" 依然都是有效解。
 *  
 *
 * 限制条件:
 *
 * 输入字符串的总长将会是 3n, 1 ≤ n ≤ 50；
 * 输入字符串中的 “H” 总数将会是 2n；
 * 输入字符串中的 “O” 总数将会是 n。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/building-h2o
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author Maner.Fan
 * @date 2019/11/15
 */
public class Q1117_Building_H2O {
    Q1117_Building_H2O() {}

    private Semaphore hydrogen = new Semaphore(2);
    private Semaphore oxygen = new Semaphore(0);

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        // 消耗一个H，每来一个O会贡献两个H
        hydrogen.acquire(1);

        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
        releaseHydrogen.run();

        // 贡献一个O
        oxygen.release(1);
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        // 消耗两个O，每来一个H会贡献一个O
        oxygen.acquire(2);

        // releaseOxygen.run() outputs "O". Do not change or remove this line.
        releaseOxygen.run();

        // 贡献两个个H
        hydrogen.release(2);
    }

    //////////////////////////////////////////////
    ////////////// ↓↓↓ for test ↓↓↓ //////////////
    //////////////////////////////////////////////

    public static void main(String[] args) {
        Runnable releaseHydrogen = () -> System.out.print("H");
        Runnable releaseOxygen = () -> System.out.print("O");

        Q1117_Building_H2O building_h2O = new Q1117_Building_H2O();

        String inputs = "OOOOHHHHHHHH";
        inputs.chars().forEach(c -> {
            switch (c) {
                case 'H': {
                    new Thread(() -> {
                        try {
                            building_h2O.hydrogen(releaseHydrogen);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }, "Thread-Hydrogen").start();
                    break;
                }
                case 'O': {
                    new Thread(() -> {
                        try {
                            building_h2O.oxygen(releaseOxygen);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }, "Thread-Oxygen").start();
                    break;
                }
                default:
                    break;
            }
        });

    }
}
