# 合并区间（Merge Intervals）

力扣 56：https://leetcode.cn/problems/merge-intervals/

## 题目描述

以数组 `intervals` 表示若干区间的集合，其中单个区间为 `intervals[i] = [starti, endi]`。请合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。

**示例 1**

```
输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
输出：[[1,6],[8,10],[15,18]]
解释：区间 [1,3] 和 [2,6] 重叠，合并为 [1,6]。
```

**示例 2**

```
输入：intervals = [[1,4],[4,5]]
输出：[[1,5]]
解释：区间 [1,4] 和 [4,5] 可被视为重叠区间（端点相接也算重叠）。
```

**提示**

- `1 <= intervals.length <= 10^4`
- `intervals[i].length == 2`
- `0 <= starti <= endi <= 10^4`

## 解题思路

**排序 + 一次遍历**。

1. 先按区间的**左端点升序排序**。排序后，所有能与当前区间合并的区间一定是相邻的，不需要再两两比较。
2. 用结果列表 `res` 存放已合并的区间，初始放入第一个区间。
3. 依次遍历剩下的区间 `cur`，与 `res` 中最后一个区间 `last` 比较：
   - 若 `cur[0] <= last[1]`，说明两者有重叠，合并成 `[last[0], max(last[1], cur[1])]`（右端点取较大者，防止被更短的区间缩小）；
   - 否则没有重叠，把 `cur` 直接追加到 `res`。
4. 遍历结束后 `res` 即为答案。

关键点是排序：排序之后每个区间只需要和「当前正在合并的区间」比较一次，把 O(n²) 的两两比较降为 O(n log n)。

## 复杂度分析

- 时间复杂度：O(n log n)，瓶颈在排序；遍历合并为 O(n)。
- 空间复杂度：O(log n)，排序的递归栈开销（不计返回结果占用的空间）。

## 代码

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public int[][] merge(int[][] intervals) {
        if (intervals.length == 0) {
            return new int[0][];
        }
        // 按区间左端点升序排序
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> res = new ArrayList<>();
        res.add(intervals[0]);
        for (int i = 1; i < intervals.length; i++) {
            int[] cur = intervals[i];
            int[] last = res.get(res.size() - 1);
            if (cur[0] <= last[1]) {
                // 有重叠，合并：右端点取两者较大值
                last[1] = Math.max(last[1], cur[1]);
            } else {
                res.add(cur);
            }
        }
        return res.toArray(new int[res.size()][]);
    }
}
```
