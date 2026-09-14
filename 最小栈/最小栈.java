import java.util.Deque;
import java.util.LinkedList;
class MinStack {
Deque<Integer> mins = new LinkedList<>();
Deque<Integer> s = new LinkedList<>();
    public MinStack() {
        
    }
    
    public void push(int value) {
        s.push(value);
        if(mins.isEmpty()||value<=mins.peek()){
            mins.push(value);
        }
    }
    
    public void pop() {
        int data=s.peek();
        if(data==mins.peek()){
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
