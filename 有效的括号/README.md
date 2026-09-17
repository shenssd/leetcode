# 有效的括号（Valid Parentheses）

力扣 20：https://leetcode.cn/problems/valid-parentheses/

## 题目描述

给定一个只包括 `'('`、`')'`、`'{'`、`'}'`、`'['`、`']'` 的字符串 `s`，判断字符串是否有效。

有效字符串需满足：

1. 左括号必须用**相同类型**的右括号闭合；
2. 左括号必须以**正确的顺序**闭合；
3. 每个右括号都有一个对应的相同类型的左括号。

示例：

- `s = "()[]{}"` → `true`
- `s = "(]"` → `false`
- `s = "([)]"` → `false`
- `s = "{[]}"` → `true`

## 解题思路

用**栈** + **哈希表**。

关键技巧：**入栈的是「期望遇到的右括号」，而不是当前遇到的左括号**。

- 遇到左括号 `(`，就把它的配对右括号 `)` 压入栈；
- 遇到右括号，就看它是不是栈顶期望的那个：相同则弹出（配对成功），不同或栈已空则说明不合法；
- 遍历结束后栈为空，说明全部配对完成。

用哈希表存 `左括号 → 右括号` 的映射，`containsKey` 一次判断就能区分「当前字符是左括号还是右括号」，省掉一长串 `if-else`。

两个小优化：

- 奇数长度的字符串一定不合法，提前返回，省一次遍历；
- 最后直接 `return sa.isEmpty()`，栈空即为合法，不必写 `if-else`。

以 `s = "([])"` 走一遍：

| 字符 | 判断 | 栈内容（栈顶在右） |
| --- | --- | --- |
| `(` | 是左括号 → 压入它的期望右括号 | `)` |
| `[` | 是左括号 → 压入它的期望右括号 | `) ]` |
| `]` | 不是左括号，栈顶 `]` 与它相同 → 弹出 | `)` |
| `)` | 不是左括号，栈顶 `)` 与它相同 → 弹出 | 空 |

循环结束栈为空 → `true`。

## 心得 · 本题用到的关键字 / 方法逐个说明

### 1. `s.length()` —— 取字符串长度

`String` 取长度是**方法** `length()`，必须带括号。三个容易混的：

| 类型 | 写法 | 是否带括号 |
| --- | --- | --- |
| 数组 `int[] a` | `a.length` | 属性，**不带**括号 |
| 字符串 `String s` | `s.length()` | 方法，**带**括号 |
| 集合 `List/Map/Deque` | `c.size()` | 方法，**带**括号 |

`if (s.length() % 2 != 0)` 这里的 `%` 是取模，余数不为 0 即长度为奇数。

### 2. `Map` / `HashMap` —— 接口与实现

```java
Map<Character, Character> st = new HashMap<>() { ... };
```

- `Map` 是**接口**，`HashMap` 是它的一个实现（哈希表）。
- 声明类型写接口、`new` 的时候写实现类，是 Java 的常规写法（面向接口编程），以后想换成 `LinkedHashMap`、`TreeMap` 只改 `new` 那一处。
- `HashMap` 的 `put` / `get` / `containsKey` 期望时间复杂度都是 O(1)。

### 3. `put(K, V)` —— 存键值对

```java
put('(', ')');   // key = 左括号，value = 对应的右括号
```

- 键不存在时新增，**键已存在时覆盖旧值**，返回被覆盖的旧值（不需要就忽略返回值）。
- `HashMap` 允许一个 `null` 键和多个 `null` 值。

### 4. `new HashMap<>() {{ ... }}` —— 双大括号初始化（本题写法）

```java
Map<Character, Character> st = new HashMap<>() {{
    put('(', ')');
    put('[', ']');
    put('{', '}');
}};
```

这是两个嵌套的花括号，含义完全不同：

- **外层 `{}`**：创建了一个**匿名内部类**（继承 `HashMap`），所以 `new HashMap<>() {}` 实际上 new 的是它的子类；
- **内层 `{}`**：**实例初始化块**，在构造对象时执行，于是三个 `put` 就被自动调用了。

它只是为了少写几行，代价是：

- 编译后多产生一个 `Solution$1.class`；
- 匿名内部类隐式持有外部类 `Solution` 的引用（本题 `Solution` 无状态，没影响；在需要序列化的场景会告警）；
- 看起来像「Map 字面量」，其实不是。

本项目其他写法可以参考，正式代码里更推荐：

```java
// Java 9+：Map.of 一步到位（返回不可变 Map，不能改）
Map<Character, Character> st = Map.of('(', ')', '[', ']', '{', '}');

// 传统写法：先 new 再 put，最稳妥
Map<Character, Character> st = new HashMap<>();
st.put('(', ')');
st.put('[', ']');
st.put('{', '}');
```

### 5. `containsKey(K)` —— 判断键是否存在

```java
if (st.containsKey(i))   // i 是左括号吗？
```

- 返回 `boolean`，期望 O(1)。
- 判断「键存在」要用 `containsKey`，**不要**用 `get(i) != null`：`HashMap` 的值本身允许为 `null`，那样写会误判。
- 本题里它的语义就是「`i` 是不是左括号」。

### 6. `get(K)` —— 按键取值

```java
sa.push(st.get(i));   // 取出 i 对应的右括号，压入栈
```

- 键不存在时返回 `null`，**不会抛异常**（这点和数组越界不同）。
- 因为前面刚 `containsKey` 判过，这里的 `get` 一定非 null，可以放心用。

### 7. 泛型里只能写 `Character`，不能写 `char`

```java
Map<Character, Character> st = ...   // ✔
Map<char, char> st = ...             // ✘ 编译错误
Deque<Character> sa = ...            // ✔ 同理
```

Java 泛型的类型参数必须是**引用类型**，基本类型要换成对应的包装类：`char → Character`、`int → Integer`、`boolean → Boolean`，以此类推。

### 8. `Deque` / `ArrayDeque` —— 用双端队列当栈

```java
Deque<Character> sa = new ArrayDeque<>();
```

- `Deque`（double ended queue）是**接口**，`ArrayDeque` 是基于**循环数组**的实现。
- 它同时能当栈和队列用；本题只用一端 `push` / `pop`，就是当**栈**用。
- **为什么不用 `Stack`**：`Stack` 是遗留类（继承自 `Vector`，方法都带 `synchronized`，又慢又已不推荐），官方建议用 `Deque` 替代。
- 注意 `ArrayDeque` **不允许 `null` 元素**，`push(null)` 会直接抛 `NullPointerException`。

### 9. `push` / `pop` / `isEmpty` / `peek` —— 栈的四个基本操作

| 方法 | 作用 | 栈为空时 |
| --- | --- | --- |
| `push(e)` | 把元素压入栈顶 | — |
| `pop()` | 弹出并**返回**栈顶元素 | 抛 `NoSuchElementException` |
| `peek()` | **只看不弹**，返回栈顶元素 | 抛 `NoSuchElementException` |
| `isEmpty()` | 栈是否为空 | 返回 `true` |

- 本题 `sa.pop()` 前先 `sa.isEmpty()` 短路判断，所以永远不会走到抛异常的分支——`||` 是**短路或**，左边为 `true` 时右边不求值。
- `peek` 和 `pop` 的区别：`peek` 不改变栈，`pop` 会移除。
- `Deque` 的 `push` / `pop` 操作的是**队头**，等价于 `addFirst` / `removeFirst`；而 `add` / `remove` 默认操作**队尾**。混用容易出错，当栈用就一直用 `push` / `pop`。

### 10. `for (char i : s.toCharArray())` —— 增强 for + `toCharArray()`

```java
for (char i : s.toCharArray()) { ... }
```

拆成两件事：

- **`s.toCharArray()`**：把字符串**复制**成一个新的 `char[]`，之后可以按下标随机访问；代价是额外 O(n) 空间。
- **增强 for（for-each）**：语法是 `for (元素类型 变量 : 数组或 Iterable) {...}`，逐个取出元素，没有下标。

对照写法（不复制数组，直接按下标取）：

```java
for (int i = 0; i < s.length(); i++) {
    char c = s.charAt(i);   // charAt(i) 取第 i 个字符
}
```

两种都可以。`toCharArray` 写起来更短，缺点是多一份数组拷贝。

**注意**：`String` 本身没有实现 `Iterable`，所以不能写 `for (char c : s)`（这点和 Python 可以直接遍历字符串不同），必须先 `toCharArray()` 或改用 `charAt`。

### 11. `char` 与 `Character` 的装箱 / 拆箱

```java
} else if (sa.isEmpty() || sa.pop() != i) {
```

- `sa.pop()` 返回的是 `Character`（包装类对象），而 `i` 是 `char`（基本类型）。
- 两边类型不一致时，Java 会先把 `Character` **自动拆箱**成 `char`，再按**字符值**比较。所以这里 `!=` 是安全的。
- **坑**：如果两边**都是** `Character` 对象，`!=` 比较的是**引用地址**而不是值，必须用 `equals`：

```java
Character a = 'a', b = 'a';
a != b        // 有坑：比的是地址，不要这么写
a.equals(b)   // 正确：比值

char x = 'a';
x != b        // 安全：一边是基本类型，会自动拆箱
```

（补充：`Character.valueOf` 对 0~127 有缓存，`'a'` 这种小字符恰好能相等，但这属于实现细节，不要依赖。）

### 12. 单引号 `'('` 与双引号 `"("`

- `'('` 是 `char`（一个字符），`"("` 是 `String`（字符串对象）。
- 本题的 `Map<Character, Character>` 键值都是字符，必须用**单引号**，写成双引号会编译报错。

### 13. `return sa.isEmpty();`

直接返回一个布尔表达式，不用写成 `if (sa.isEmpty()) return true; else return false;` —— 后者啰嗦且容易被误写。空栈 = 所有括号都配对了 = 合法。

### 14. `import`

本机（IDEA / javac）编译需要手动导入：

```java
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
```

在力扣的编辑器里提交时平台会自带这些 import，所以在力扣上直接粘贴不带 import 的代码也能过；但在本地跑就必须补上。

## 复杂度分析

- 时间复杂度：O(n)，`n` 为字符串长度，每个字符进出栈各至多一次，哈希表操作是 O(1)。
- 空间复杂度：O(n)，栈最多存 n/2 个字符；`toCharArray()` 还会额外复制一份长度 n 的 `char[]`（若在意这点可改用 `charAt`）。

## 代码

```java
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

class Solution {
    public boolean isValid(String s) {
        // 长度为奇数，一定无法两两配对
        if (s.length() % 2 != 0) {
            return false;
        }
        // 左括号 → 对应的右括号
        Map<Character, Character> st = new HashMap<>() {{
            put('(', ')');
            put('[', ']');
            put('{', '}');
        }};
        // 存放「期望遇到的右括号」
        Deque<Character> sa = new ArrayDeque<>();
        for (char i : s.toCharArray()) {
            if (st.containsKey(i)) {
                // 遇到左括号：把期望的右括号压栈
                sa.push(st.get(i));
            } else if (sa.isEmpty() || sa.pop() != i) {
                // 遇到右括号：栈空，或栈顶不是它 → 不合法
                return false;
            }
        }
        // 栈空说明全部配对完成
        return sa.isEmpty();
    }
}
```
