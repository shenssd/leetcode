import java.util.Deque;
import java.util.LinkedList;

/**
 * 力扣 225. 用队列实现栈（Implement Stack using Queues）
 *
 * 【思路】
 * 栈是「后进先出」（LIFO：最后进去的最先出来），
 * 队列是「先进先出」（FIFO：先进去的最先出来），两者刚好相反。
 *
 * 这里用「一个队列」模拟栈，核心在 push：
 *  1. 先把新元素 x 入队（放到队尾）；
 *  2. 再把队首的 (size - 1) 个元素依次出队、再重新入队（转一圈）；
 *  3. 转完之后，新元素 x 就排到了队首 —— 也就是「栈顶」。
 *
 * 这样 pop() 直接取队首就是栈顶，成功模拟出了栈的 LIFO。
 *
 * 【复杂度】
 *  时间：push O(n)，pop/top/empty O(1)
 *  空间：O(n)
 *
 * 注：LeetCode 上这道题的类名固定叫 MyStack，所以这里不写 public class，
 *     否则文件名必须改成 MyStack.java 才能编译。
 */
class MyStack {
    // Deque：双端队列接口（两端都能插入/删除），继承自 Queue
    // Integer：包装类，集合里只能存对象，所以用 Integer 而不是基本类型 int
    // 这里只声明、不赋值，所以初值是 null（还没真正创建对象）
    Deque<Integer> s;

    // 构造方法：名字和类名相同，没有返回值（连 void 都不写）
    // 用 new MyStack() 创建对象时会自动调用它，用来做初始化
    public MyStack() {
        // new 关键字：在内存中真正创建一个 LinkedList 对象
        // LinkedList：实现了 Deque 接口的具体类，底层是链表
        // 把「接口类型变量 s」指向「实现类对象」，这叫面向接口编程
        s = new LinkedList<>();
    }

    // void：这个方法不返回任何值
    // int x：参数是基本类型 int，调用时要传一个整数
    public void push(int x) {
        s.offer(x);              // offer：入队，把 x 放到队尾
        int si = s.size();       // size：返回当前元素个数（此时 x 已入队，所以是入队后的个数）
        // for：循环；i 从 0 开始，每次 +1，直到 i < si - 1 为止
        // 把队首的 (si - 1) 个元素依次搬到队尾，让 x 变成队首（栈顶）
        for (int i = 0; i < si - 1; i++) {
            s.offer(s.poll());   // poll：出队（取出并删除队首），再 offer 回队尾
        }
    }

    // 出栈：弹出并返回栈顶元素
    public int pop() {
        int t = s.peek();  // peek：查看队首（栈顶），但不删除
        s.poll();          // poll：真正把队首删除
        return t;          // return：把结果返回给调用者（这里等价于 return s.poll()）
    }

    // 查看栈顶：只看不删
    public int top() {
        return s.peek();
    }

    // 判断栈是否为空：返回 true 或 false
    public boolean empty() {
        return s.isEmpty(); // isEmpty：判断队列里有没有元素
    }
}
