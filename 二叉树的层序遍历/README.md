# 题目：【填写题目名称】
力扣107二叉树的层序遍历2

## 题目描述
给你二叉树的根节点 root ，返回其节点值 自底向上的层序遍历 。 （即按从叶子节点所在层到根节点所在的层，逐层从左向右遍历

## 解题思路
queue取头结点,创建一个队列.从头结点开始最后反转就行

## 代码
```java
//粘贴AC完整Java代码

class Solution:
    def levelOrderBottom(self, root: Optional[TreeNode]) -> List[List[int]]:
        if not root:
            return []

        res = []
        quene = collections.deque([root])

        while quene:
            level = []
            for _ in range(len(quene)):#循环当前层结点
                cur = quene.popleft()#取当前结点值
                level.append(cur.val)
                if cur.left:#有子结点入列
                    quene.append(cur.left)
                if cur.right:
                    quene.append(cur.right)
            res.append(level)  
        return res[::-1]  