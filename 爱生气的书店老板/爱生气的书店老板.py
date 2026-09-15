from typing import List

# 力扣 1052. 爱生气的书店老板（Grumpy Bookstore Owner）
# https://leetcode.cn/problems/grumpy-bookstore-owner/
#
# 【思路】滑动窗口
# grumpy[i]=0：老板不生气，这分钟的顾客本来就满意；
# grumpy[i]=1：老板生气，这分钟的顾客会流失，只有靠「憋气」技能（连续 minutes 分钟不生气）才能挽回。
#
# 答案拆成两部分：
#   1) 基础满意数：所有 grumpy==0 的分钟，顾客都满意，与技能无关，记为 s[0]；
#   2) 额外挽回数：选一段长度为 minutes 的窗口，让窗口内「生气分钟的顾客」也变满意，
#      窗口内生气分钟的顾客之和记为 s[1]，答案取所有窗口里 s[1] 的最大值。
#
# 于是最终答案 = s[0] + max(s[1])。
# 用 s[1] 动态维护「当前窗口内生气顾客之和」，窗口每右移一格：加进新进入的、减去滑出的。
#
# 【复杂度】
#   时间：O(n)（每个元素最多进出窗口一次）
#   空间：O(1)

class Solution:
    def maxSatisfied(self, customers: List[int], grumpy: List[int], minutes: int) -> int:
        s = [0] * 2   # s[0]=全程不生气顾客总数；s[1]=当前窗口内生气顾客数
        ans = 0       # 记录所有窗口里 s[1] 的最大值
        for i, (c, j) in enumerate(zip(customers, grumpy)):
            s[j] += c                      # j==0 累进基础满意；j==1 累进当前窗口的生气顾客
            left = i - minutes + 1         # 窗口左边界：[left, i] 长度正好 minutes
            if left < 0:
                continue                   # 窗口还没满 minutes 分钟，先跳过
            ans = max(s[1], ans)           # 窗口已满，用当前 s[1] 更新最大值
            if grumpy[left]:
                s[1] -= customers[left]    # 左边界滑出窗口，若它生气就减去
        return s[0] + ans
