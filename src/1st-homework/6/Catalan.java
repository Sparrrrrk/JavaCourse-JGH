
public class Catalan {
	
	public static int answers = 0;
	
	//��ʵ��go����
	public static void go(Deque from, Deque to, Stack s) {
		if(from.size() == 0 && s.empty()){//������Ѿ�����to������
			for(int i = 0; i < to.size(); i++){
				System.out.println(to.get(i));
			}
			System.out.println();
			answers++;
		}
		else{
			// ��from��һ��Ԫ�ط���ջ
			if(from.size() > 0  /* && s.size() < 4 */){
				Deque fromCopy = from.clone();
				Deque toCopy  = to.clone();
				Stack sCopy = s.clone();
				int temp = fromCopy.getFirst();
				fromCopy.removeFirst();
				sCopy.push(temp);
				go(fromCopy,toCopy,sCopy);
			}
			if(!s.empty()){
				// ��ջ��һ��Ԫ�ص��������
				Deque fromCopy = from.clone();
				Deque toCopy = to.clone();
				Stack sCopy = s.clone();
				int temp = sCopy.pop();
				toCopy.addLast(temp);
				go(fromCopy,toCopy,sCopy);
			}




		}
	}

	public static void main(String[] args) {
		Deque from = new Deque();
		Deque to = new Deque();
		Stack s = new Stack();
		
		for(int i=1;i<=7;i++) {
			from.addLast(i);
		}
		
		go(from, to, s);
		
		System.out.println(answers);
		

	}

}
