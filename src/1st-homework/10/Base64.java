import java.util.ArrayList;

public class Base64 {
	private final static String Hex = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	private final static int ENCODE_MASK = 0b11111100_00000000_00000000_00000000;
	private final static int DECODE_MASK = 0b11111111_00000000_00000000_00000000;
	private final static  int UNSIGNED_MASK	 = 0b00000000_00000000_00000000_11111111;



	public static String encode(byte[] binaryData) {
		final int BYTES_LEFT = binaryData.length % 3;
		int SuffixNum = 0;/* = 数 */
		if(BYTES_LEFT == 1){
			SuffixNum = 2;
		}
		else if(BYTES_LEFT == 2){
			SuffixNum = 1;
		}
		int GroupNum = BYTES_LEFT == 0 ? binaryData.length / 3 : binaryData.length / 3 + 1;

		//构建64字字符串
		StringBuilder EncoderStirng = new StringBuilder();
		int Buffer24Bits = 0;
		int Buffer6Bites = 0;

		//逐个24处理
		for(int i = 0 ; i < GroupNum; i++){
			// 从字节数组中取出3个字节放入 buffer24Bits
			for(int j = 0; j < 3;j++){
				// 只有数组中还有数据的时候去取。如果取完了没凑够一组，就直接移位（相当于补0）
				if( i * 3 + j < binaryData.length){
					Buffer24Bits += (int)binaryData[i*3+j] & UNSIGNED_MASK;
				}
				Buffer24Bits <<= 8;
			}

			for (int j = 0; j < 4; j++)
			{
				// 取出高位6位
				Buffer6Bites = Buffer24Bits & ENCODE_MASK;
//				System.out.println(Buffer6Bites);
				// 将高6位删除
				Buffer24Bits <<= 6;
//				System.out.println(Buffer24Bits);
				// 把这6位移动到低位，查表找对应字符
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
		// 每个字符事实上代表6位。看有多少组24位
		final int GROUP_SIZE = s.length() / 4;
		// 放置每个字符
		char charBuffer;
		// 字符代表的数字
		int decodeNum = 0;
		int buffer24Bits = 0;
		// 成24位的取出并解码
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
			// 需要把24位移动到int的高位，因此需要补移2位
			buffer24Bits <<= 2;

			// 每个字节取出查看数据。如果出现了8位全是0，那么必然是补上去的，忽略
			for (int j = 0; j < 3 && buffer24Bits != 0; j++)
			{
				// 取出高8位数据，移动到低位，并截断成byte。
				arrayList.add((byte) ((buffer24Bits & DECODE_MASK) >>> 24));
				buffer24Bits <<= 8;

			}
		}
		// 把arrayList中的数据移动到byte数组中
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
