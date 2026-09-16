# 数组的最大美丽值（Maximum Beauty of an Array After Applying Operation）

力扣 2779：https://leetcode.cn/problems/maximum-beauty-of-an-array-after-applying-operation/

## 题目描述

给你一个下标从 0 开始的整数数组 `nums` 和一个非负整数 `k`。

在一步操作中，你可以选择 `nums` 中任一元素，将其**加上或减去**一个不超过 `k` 的值（每个元素最多操作一次）。

数组的**美丽值**定义为：数组中相等元素的最大数目。请返回执行操作后能得到的最大美丽值。

**示例 1**

```
输入：nums = [4,6,1,2], k = 2
输出：3
解释：nums 变为 [4,6,1,2] -> [4,4,1,2]，有 3 个 4。
```

**示例 2**

```
输入：nums = [1,1,1,1], k = 10
输出：4
```

**提示**

- `1 <= nums.length <= 10^5`
- `0 <= nums[i], k <= 10^5`

## 解题思路

**排序 + 滑动窗口**。

每个元素 `x` 经过一次操作后，可以变成区间 `[x-k, x+k]` 内的任意一个数。要让若干个元素变成同一个数，就要它们的可变成区间有公共交点 —— 等价于：这些元素中 **最大值 - 最小值 <= 2k**。

于是问题变成：**排序后，找最长的一段连续子数组，使 `nums[right] - nums[left] <= 2k`**。因为子数组排序后最大值/最小值恰好在两端，长度就是可行元素的个数。

用滑动窗口维护这个条件：

- `right` 向右扩张，把 `nums[right]` 纳入窗口；
- 若 `nums[right] - nums[left] > 2k`，说明窗口不合法，`left` 一直右移直到重新合法；
- 每次用窗口长度 `right - left + 1` 更新答案。

**为什么是排序**：排序后「选哪些元素」的问题变成「选哪一段连续区间」，单调性和双指针才成立。

**为什么 left 不用回退**：窗口右端向右移动时，合法的左端只会跟着右移（单调不减），所以 `left` 一直递增即可，总体 O(n)。

## 心得 · 滑动窗口标准模板

滑动窗口的通用骨架（**不定长窗口**，本题属于这一类）：

```java
int left = 0;
int ans = 0;                       // 按题目需要，也可能是 min / 计数 / 和
for (int right = 0; right < n; right++) {
    // 1. 把 nums[right] 加入窗口：更新统计量
    //    （sum += nums[right]; count[x]++; ...）

    // 2. 窗口不满足条件时，收缩左边界
    while (/* 窗口不合法 */) {
        //    把 nums[left] 移出窗口：还原统计量
        //    （sum -= nums[left]; count[x]--; ...）
        left++;
    }

    // 3. 此时窗口一定合法，用窗口更新答案
    ans = Math.max(ans, right - left + 1);
}
return ans;
```

套用到本题：

| 模板位置 | 本题对应 |
| --- | --- |
| 加入右端 | 无需额外统计，直接扩张 |
| 合法性判断 | `nums[right] - nums[left] > 2 * k` |
| 收缩左端 | `left++`（不需要还原统计量） |
| 更新答案 | `ans = max(ans, right - left + 1)` |

几点通用提醒：

- **先想清楚「窗口里维护什么」**：是和 / 计数 / 最大值，还是本题这种两端差值。
- **合法性的单调性是前提**：只有当「窗口变大 → 违法，窗口变小 → 合法」这种单调关系成立时，`left` 才不用回退，复杂度才是 O(n)。否则要考虑其他做法。
- **定长窗口**是另一类写法：先铺满长度 `k` 的初始窗口，然后 `right` 每进一步、`left` 就跟进一步，窗口大小恒定不变（如力扣 643、1052）。
- **收缩用 `while` 而不是 `if`**：把「缩小到合法为止」表达清楚，定长窗口时用 `if` 即可。
- 数组含负数时，和型窗口需要额外的单调性前提，别硬套模板。

## 复杂度分析

- 时间复杂度：O(n log n)，瓶颈在排序；滑动窗口部分 `left`、`right` 各最多移动 n 次，为 O(n)。
- 空间复杂度：O(log n)，排序的递归栈开销。

## 代码

```java
import java.util.Arrays;

class Solution {
    public int maximumBeauty(int[] nums, int k) {
        // 先排序，问题转化为：找最长的一段子数组，使最大值 - 最小值 <= 2k
        Arrays.sort(nums);
        int ans = 0;
        int left = 0;
        for (int right = 0; right < nums.length; right++) {
            // 窗口不合法就收缩左边界
            while (nums[right] - nums[left] > 2 * k) {
                left++;
            }
            ans = Math.max(ans, right - left + 1);
        }
        return ans;
    }
}
```
