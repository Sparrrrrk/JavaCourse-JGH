// 13/17С������100λ�������Ǽ���
public class FractionalDigit {

	public static void main(String[] args) {
		int d = 13;
		int q = 17;
		int a = 0;
		/*����˼·��
		 * 1. ���� d * 10 / q���õ�һ������ a
		 * 2. �� d = d * 10 - a * q���ظ� 1
		 * */
		for(int i = 0; i < 100; i++){
			a = d * 10 / q;
			d = d * 10 - a * q;
		}
		System.out.println(a);

	}

}
