import java.util.ArrayList;

public class Base64 {
	private final static String Hex = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	private final static int ENCODE_MASK = 0b11111100_00000000_00000000_00000000;
	private final static int DECODE_MASK = 0b11111111_00000000_00000000_00000000;
	private final static  int UNSIGNED_MASK	 = 0b00000000_00000000_00000000_11111111;



	public static String encode(byte[] binaryData) {
		final int BYTES_LEFT = binaryData.length % 3;
		int SuffixNum = 0;/* = �� */
		if(BYTES_LEFT == 1){
			SuffixNum = 2;
		}
		else if(BYTES_LEFT == 2){
			SuffixNum = 1;
		}
		int GroupNum = BYTES_LEFT == 0 ? binaryData.length / 3 : binaryData.length / 3 + 1;

		//����64���ַ���
		StringBuilder EncoderStirng = new StringBuilder();
		int Buffer24Bits = 0;
		int Buffer6Bites = 0;

		//���24����
		for(int i = 0 ; i < GroupNum; i++){
			// ���ֽ�������ȡ��3���ֽڷ��� buffer24Bits
			for(int j = 0; j < 3;j++){
				// ֻ�������л������ݵ�ʱ��ȥȡ�����ȡ����û�չ�һ�飬��ֱ����λ���൱�ڲ�0��
				if( i * 3 + j < binaryData.length){
					Buffer24Bits += (int)binaryData[i*3+j] & UNSIGNED_MASK;
				}
				Buffer24Bits <<= 8;
			}

			for (int j = 0; j < 4; j++)
			{
				// ȡ����λ6λ
				Buffer6Bites = Buffer24Bits & ENCODE_MASK;
//				System.out.println(Buffer6Bites);
				// ����6λɾ��
				Buffer24Bits <<= 6;
//				System.out.println(Buffer24Bits);
				// ����6λ�ƶ�����λ������Ҷ�Ӧ�ַ�
				EncoderStirng.append(Hex.charAt(Buffer6Bites >>> 26));
				System.out.println(EncoderStirng);
			}
		}
//		EncoderStirng.delete(EncoderStirng.length() - SuffixNum, EncoderStirng.length());
		for (int i = 0; i < SuffixNum; i++)
		{
			EncoderStirng.append('=');
		}
		return EncoderStirng.toString();
	}
	
	public static byte[] decode(String s) {
		ArrayList<Byte> arrayList = new ArrayList<>();
		// ÿ���ַ���ʵ�ϴ���6λ�����ж�����24λ
		final int GROUP_SIZE = s.length() / 4;
		// ����ÿ���ַ�
		char charBuffer;
		// �ַ����������
		int decodeNum = 0;
		int buffer24Bits = 0;
		// ��24λ��ȡ��������
		for (int i = 0; i < GROUP_SIZE; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				charBuffer = s.charAt(i * 4 + j);
				if (charBuffer == '=')
				{
					decodeNum = 0;
				}
				else
				{
					decodeNum = Hex.indexOf(charBuffer);
				}
				buffer24Bits += decodeNum;
				buffer24Bits <<= 6;
			}
			// ��Ҫ��24λ�ƶ���int�ĸ�λ�������Ҫ����2λ
			buffer24Bits <<= 2;

			// ÿ���ֽ�ȡ���鿴���ݡ����������8λȫ��0����ô��Ȼ�ǲ���ȥ�ģ�����
			for (int j = 0; j < 3 && buffer24Bits != 0; j++)
			{
				// ȡ����8λ���ݣ��ƶ�����λ�����ضϳ�byte��
				arrayList.add((byte) ((buffer24Bits & DECODE_MASK) >>> 24));
				buffer24Bits <<= 8;

			}
		}
		// ��arrayList�е������ƶ���byte������
		byte[] arr = new byte[arrayList.size()];
		for (int i = 0; i < arrayList.size(); i++)
		{
			arr[i] = arrayList.get(i);
		}
		return arr;
	}
	
	public static void main(String[] args) {
		byte[] a = { 97};
		String s = encode(a);
		System.out.println(s);
		byte[] b = decode(s);
		for(int i=0;i<b.length;i++) {
			System.out.print(b[i] + " ");
		}
		System.out.println();
	}
}

//List,Set
//Map
//java.util
