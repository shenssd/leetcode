import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

class Solution {
    public boolean isValid(String s) {
        if (s.length()%2!=0){
return false;
        }
        Map<Character,Character> st=new HashMap<>(){{
             put('(', ')');
            put('[', ']');
           put('{', '}');
        }};
        Deque <Character> sa=new ArrayDeque<>();
        for(char i:s.toCharArray()){
            if(st.containsKey(i)){
                sa.push(st.get(i));

            }
            else if (sa.isEmpty()||sa.pop()!=i){
                return false;
            }
        }
        return sa.isEmpty();
    }
}
