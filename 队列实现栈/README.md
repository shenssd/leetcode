# 队列实现栈（Implement Stack using Queues）

力扣 225：https://leetcode.cn/problems/implement-stack-using-queues/

## 题目描述

请你仅使用队列的基本操作，实现一个后入先出（LIFO）的栈，并支持普通栈的全部操作：

- `MyStack()` 初始化栈对象
- `void push(int x)` 将元素 `x` 压入栈顶
- `int pop()` 移除并返回栈顶元素
- `int top()` 返回栈顶元素（不移除）
- `boolean empty()` 如果栈为空返回 `true`，否则返回 `false`

注意：只能使用队列的入队、出队、查看队首、判空这几个基本操作。

## 解题思路

栈是「后进先出」（LIFO），队列是「先进先出」（FIFO），两者方向相反。这里用**单个队列**模拟栈，关键在 `push`：

1. 先把新元素 `x` 入队（放到队尾）；
2. 再把队首的 `size - 1` 个元素依次出队、重新入队（相当于「转一圈」）；
3. 转完后，新元素 `x` 排到了队首，也就是「栈顶」。

于是 `pop()` / `top()` 直接操作队首即可，成功模拟出 LIFO。

## 复杂度分析

- 时间复杂度：`push` O(n)，`pop` / `top` / `empty` O(1)
- 空间复杂度：O(n)

## 代码

```java
import java.util.Deque;
import java.util.LinkedList;

class MyStack {
    Deque<Integer> s;

    public MyStack() {
        s = new LinkedList<>();
    }

    public void push(int x) {
        s.offer(x);
        int si = s.size();
        for (int i = 0; i < si - 1; i++) {
            s.offer(s.poll());
        }
    }

    public int pop() {
        int t = s.peek();
        s.poll();
        return t;
    }

    public int top() {
        return s.peek();
    }

    public boolean empty() {
        return s.isEmpty();
    }
}
```
