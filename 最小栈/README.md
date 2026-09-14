# 最小栈（Min Stack）

力扣 155：https://leetcode.cn/problems/min-stack/

## 题目描述

设计一个支持 `push`、`pop`、`top` 操作，并能在常数时间内检索到最小元素的栈。

- `MinStack()` 初始化栈对象
- `void push(int val)` 将元素 `val` 推入栈中
- `void pop()` 删除栈顶元素
- `int top()` 获取栈顶元素
- `int getMin()` 获取栈中的最小元素

要求所有操作时间复杂度均为 O(1)。

## 解题思路

使用**辅助栈（双栈）**：

- `s`：普通栈，正常存取元素；
- `mins`：同步维护当前栈中的最小值。入栈时，若新值不大于当前最小值则同时入辅助栈；出栈时，若弹出的恰好是当前最小值，辅助栈也同步弹出。

这样 `getMin()` 直接取辅助栈栈顶即可，O(1) 完成。

## 复杂度分析

- 时间复杂度：`push` / `pop` / `top` / `getMin` 均为 O(1)
- 空间复杂度：O(n)

## 代码

```java
import java.util.Deque;
import java.util.LinkedList;

class MinStack {
    Deque<Integer> mins = new LinkedList<>();
    Deque<Integer> s = new LinkedList<>();

    public MinStack() {
    }

    public void push(int value) {
        s.push(value);
        if (mins.isEmpty() || value <= mins.peek()) {
            mins.push(value);
        }
    }

    public void pop() {
        int data = s.peek();
        if (data == mins.peek()) {
            mins.pop();
        }
        s.pop();
    }

    public int top() {
        return s.peek();
    }

    public int getMin() {
        return mins.peek();
    }
}
```
