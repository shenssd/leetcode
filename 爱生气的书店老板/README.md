# 爱生气的书店老板（Grumpy Bookstore Owner）

力扣 1052：https://leetcode.cn/problems/grumpy-bookstore-owner/

## 题目描述

书店老板营业 `n` 分钟，每分钟进入的顾客数由数组 `customers` 给出。老板有时会生气，`grumpy[i]` 表示第 `i` 分钟是否生气（`1` = 生气，`0` = 不生气）。

- 老板不生气时，这分钟的顾客满意；
- 老板生气时，这分钟的顾客不满意。

老板有一个「憋气」技能，可以让自己**连续 `minutes` 分钟不生气**（只能用一次）。问：最多能让多少顾客满意？

## 解题思路

滑动窗口。满意顾客 = 本来就不生气的顾客 + 靠技能挽回的顾客：

1. **基础满意数**：所有 `grumpy[i] == 0` 的分钟，顾客本来就满意，累加记为 `s[0]`。
2. **额外挽回数**：技能只能覆盖连续 `minutes` 分钟，且只在「生气分钟」上有收益。用长度为 `minutes` 的滑动窗口，动态维护窗口内「生气分钟的顾客之和」`s[1]`，并记录其最大值 `ans`。
3. 答案 = `s[0] + ans`。

## 复杂度分析

- 时间复杂度：O(n)
- 空间复杂度：O(1)

## 代码（Python）

```python
from typing import List

class Solution:
    def maxSatisfied(self, customers: List[int], grumpy: List[int], minutes: int) -> int:
        s = [0] * 2
        ans = 0
        for i, (c, j) in enumerate(zip(customers, grumpy)):
            s[j] += c
            left = i - minutes + 1
            if left < 0:
                continue
            ans = max(s[1], ans)
            if grumpy[left]:
                s[1] -= customers[left]
        return s[0] + ans
```
