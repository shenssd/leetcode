class Solution:
    def levelOrderBottom(self, root: Optional[TreeNode]) -> List[List[int]]:
        if not root:
            return []

        res = []
        quene = collections.deque([root])

        while quene:
            level = []
            for _ in range(len(quene)):
                cur = quene.popleft()
                level.append(cur.val)
                if cur.left:
                    quene.append(cur.left)
                if cur.right:
                    quene.append(cur.right)
            res.append(level)  
        return res[::-1]  