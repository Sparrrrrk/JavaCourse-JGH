public class Array {

	// ��ҵҪ��
	// �ж�����data�Ƿ���array�����У�����������У��������ص�һ�γ����������е�λ��
	// ������������У��򷵻�-1, ���ÿ���array���ñ���Ϊnull�����
	// ��ʵ�ָú�������ֹ�޸����������������������Ĵ���
	public static int indexOf(int data, int[] array) {
		int pos = -1;
		for(int i = 0; i < array.length; i++){
			if(array[i] == data){
				pos = i;
				break;
			}
		}
		return pos;
	}

	// �ó������н��Ӧ�������1��-1
	public static void main(String[] args) {
		int[] array = { 1, 3, -1, 9, 7, 3 };
		System.out.println(indexOf(3, array));
		System.out.println(indexOf(5, array));

	}

}
