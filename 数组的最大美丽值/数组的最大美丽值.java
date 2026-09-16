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
