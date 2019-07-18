import java.io.*;
import java.util.*;

/**
 * ����Ϊ�������Ӷ���ķ��������ݳ�Ա.
 * ID����ָѧ��, ���������һ��Ҫд��������ֺ�ѧ��
 * ��ҵ�г��ֵ�ʾ������abdc001��Ҫ�ĳ�ѧ����ѧ������
 *
 * @author Dutianmiao and 201792137
 * @version 2018-07-11
 **/

public class TextZip
{

	//ID, ��ѧ�ŵ�ֵ��Ҫ�޸�!
	private static final String ID = "201792137";

	/*Ƶ���ļ�ÿһ����¼�Ŀ�ͷ����β��ָ�������Ҫ��Ϊ�˷�ֹ��ƽ̨���з�����
	 * �ļ���ʽ��
	 * #|F:~72|@
	 * #|G:~18|@
	 * #|H:~76|@
	 * #|I:~51|@
	 * */
	private static final String FREQ_START = "#|";
	private static final String FREQ_END = "|@";
	private static final String FREQ_MID = ":~";


	/**
	 * This method generates the huffman tree for the text: "abracadabra!"
	 *
	 * @return the root of the huffman tree
	 */

	public static TreeNode abracadbraTree()
	{
		TreeNode n0 = new TreeNode(new CharFreq('!', 1));
		TreeNode n1 = new TreeNode(new CharFreq('c', 1));
		TreeNode n2 = new TreeNode(new CharFreq('\u0000', 2), n0, n1);
		TreeNode n3 = new TreeNode(new CharFreq('r', 2));
		TreeNode n4 = new TreeNode(new CharFreq('\u0000', 4), n3, n2);
		TreeNode n5 = new TreeNode(new CharFreq('d', 1));
		TreeNode n6 = new TreeNode(new CharFreq('b', 2));
		TreeNode n7 = new TreeNode(new CharFreq('\u0000', 3), n5, n6);
		TreeNode n8 = new TreeNode(new CharFreq('\u0000', '7'), n7, n4);
		TreeNode n9 = new TreeNode(new CharFreq('a', 5));
		TreeNode n10 = new TreeNode(new CharFreq('\u0000', 12), n9, n8);
		return n10;
	}

	/**
	 * This method decompresses a huffman compressed text file.  The compressed
	 * file must be read one bit at a time using the supplied BitReader, and
	 * then by traversing the supplied huffman tree, each sequence of compressed
	 * bits should be converted to their corresponding characters.  The
	 * decompressed characters should be written to the FileWriter
	 *
	 * @param br the BitReader which reads one bit at a time from the
	 *           compressed file
	 *           huffman the huffman tree that was used for compression, and
	 *           hence should be used for decompression
	 *           fw      a FileWriter for storing the decompressed text file
	 */
	public static void decompress(BitReader br, TreeNode huffman, FileWriter fw) throws Exception
	{

		// IMPLEMENT THIS METHOD
		// �����ļ������˹��������ٽ��в���
		if (huffman != null)
		{
			TreeNode currentTreeNode = huffman;
			while (br.hasNext())
			{
				// ��һλ��0������
				if (!br.next())
				{
					currentTreeNode = currentTreeNode.getLeft();
				}
				// ��һλ��1������
				else
				{
					currentTreeNode = currentTreeNode.getRight();
				}
				// �����Ҷ�ӽ�㣬��Ȼ��һ���ַ�
				if (currentTreeNode.isLeaf())
				{
					char c = ((CharFreq) currentTreeNode.getItem()).getChar();
					fw.append(c);
					currentTreeNode = huffman;
				}
			}
			// ����ļ������˵������λû��Ӧ�ַ����Ǳ�Ȼ���ļ���������
			if (currentTreeNode != huffman)
			{
				throw new Exception("ѹ���ļ�������");
			}
		}
	}

	/**
	 * This method traverses the supplied huffman tree and prints out the
	 * codes associated with each character
	 *
	 * @param t the root of the huffman tree to be traversed
	 *          code a String used to build the code for each character as
	 *          the tree is traversed recursively
	 */
	public static void traverses(TreeNode t, String code)
	{

		// IMPLEMENT THIS METHOD
		if (t != null)
		{
			if (t.isLeaf())
			{
				System.out.printf("%s: %s\n", ((CharFreq) t.getItem()).getChar(), code);
			}
			else
			{
				if (t.getLeft() != null)
				{
					String codeCopy = code.concat("0");
					traverses(t.getLeft(), codeCopy);
				}
				if (t.getRight() != null)
				{
					String codeCopy = code.concat("1");
					traverses(t.getRight(), codeCopy);
				}
			}
		}
	}

	public static TreeMap<Character, String> getCodes(TreeNode root)
	{
		TreeMap<Character, String> charMap = new TreeMap<>();
		String code = "";
		// ����մ���������Ҷ�ӽ�㣬��ô������ֻ��һ���ַ������⴦��
		if (root.isLeaf())
		{
			code = code.concat("0");
			charMap.put(((CharFreq) root.getItem()).getChar(), code);
		}
		else
		{
			getCodesRecursive(root, code, charMap);
		}
		return charMap;
	}

	private static void getCodesRecursive(TreeNode root, String code, TreeMap<Character, String> charMap)
	{
		if (root.isLeaf())
		{
			char c = ((CharFreq) root.getItem()).getChar();
			charMap.put(c, code);
		}
		else
		{
			if (root.getLeft() != null)
			{
				String codeCopy = code.concat("0");
				getCodesRecursive(root.getLeft(), codeCopy, charMap);
			}

			if (root.getRight() != null)
			{
				String codeCopy = code.concat("1");
				getCodesRecursive(root.getRight(), codeCopy, charMap);
			}
		}
	}

	/**
	 * This method removes the TreeNode, from an ArrayList of TreeNodes,  which
	 * contains the smallest item.  The items stored in each TreeNode must
	 * implement the Comparable interface.
	 * The ArrayList must contain at least one element.
	 *
	 * @param a an ArrayList containing TreeNode objects
	 * @return the TreeNode in the ArrayList which contains the smallest item.
	 * This TreeNode is removed from the ArrayList.
	 */
	public static TreeNode removeMin(ArrayList a)
	{
		int minIndex = 0;
		for (int i = 0; i < a.size(); i++)
		{
			TreeNode ti = (TreeNode) a.get(i);
			TreeNode tmin = (TreeNode) a.get(minIndex);
			if (((Comparable) (ti.getItem())).compareTo(tmin.getItem()) < 0)
			{
				minIndex = i;
			}
		}
		TreeNode n = (TreeNode) a.remove(minIndex);
		return n;
	}

	/**
	 * This method counts the frequencies of each character in the supplied
	 * FileReader, and produces an output text file which lists (on each line)
	 * each character followed by the frequency count of that character.  This
	 * method also returns an ArrayList which contains TreeNodes.  The item stored
	 * in each TreeNode in the returned ArrayList is a CharFreq object, which
	 * stores a character and its corresponding frequency
	 *
	 * @param fr the FileReader for which the character frequencies are being
	 *           counted
	 *           pw the PrintWriter which is used to produce the output text file
	 *           listing the character frequencies
	 * @return the ArrayList containing TreeNodes.  The item stored in each
	 * TreeNode is a CharFreq object.
	 */
	public static ArrayList countFrequencies(FileReader fr, PrintWriter pw) throws Exception
	{

		// IMPLEMENT THIS METHOD
		char[] buffer = new char[1024];
		int readLength;
		TreeMap<Character, Integer> freqMap = new TreeMap<>();
		ArrayList<TreeNode> list = new ArrayList<>();
		// �Ѷ������ַ�������¼��map��
		while ((readLength = fr.read(buffer)) != -1)
		{
			for (int i = 0; i < readLength; i++)
			{
				freqMap.compute(buffer[i], (key, val) -> (val == null) ? 1 : val + 1);
			}
		}

		// �ٰ�map�����ݷ����ļ���list��
		for (char key : freqMap.keySet())
		{
			pw.println(String.format("%s%c%s%d%s", FREQ_START, key, FREQ_MID, freqMap.get(key), FREQ_END));
			list.add(new TreeNode(new CharFreq(key, freqMap.get(key))));
		}
		return list;
	}

	/**
	 * This method builds a huffman tree from the supplied ArrayList of TreeNodes.
	 * Initially, the items in each TreeNode in the ArrayList store a CharFreq object.
	 * As the tree is built, the smallest two items in the ArrayList are removed,
	 * merged to form a tree with a CharFreq object storing the sum of the frequencies
	 * as the root, and the two original CharFreq objects as the children.  The right
	 * child must be the second of the two elements removed from the ArrayList (where
	 * the ArrayList is scanned from left to right when the minimum element is found).
	 * When the ArrayList contains just one element, this will be the root of the
	 * completed huffman tree.
	 *
	 * @param trees the ArrayList containing the TreeNodes used in the algorithm
	 *              for generating the huffman tree
	 * @return the TreeNode referring to the root of the completed huffman tree
	 */
	public static TreeNode buildTree(ArrayList trees) throws IOException
	{

		// IMPLEMENT THIS METHOD
		// �������������û�н�㣬��ֱ�ӷ���null
		if (trees.size() == 0)
		{
			return null;
		}
		// ������������ֻ��һ����㣬�Ǿ����⴦��
		else if (trees.size() == 1)
		{
			TreeNode node = (TreeNode) trees.get(0);
			CharFreq charFreq = (CharFreq) node.getItem();
			trees.remove(0);
			trees.add(new TreeNode(new CharFreq(charFreq.getChar(), charFreq.getFreq()), node, null));
		}
		else
		{
			TreeNode smallest;
			TreeNode secondSmallest;
			int freqSum = 0;
			while (trees.size() != 1)
			{
				smallest = removeMin(trees);
				secondSmallest = removeMin(trees);
				freqSum = ((CharFreq) smallest.getItem()).getFreq() + ((CharFreq) secondSmallest.getItem()).getFreq();
				trees.add(new TreeNode(new CharFreq('\0', freqSum), smallest, secondSmallest));
			}
		}
		return (TreeNode) trees.get(0);
	}

	/**
	 * This method compresses a text file using huffman encoding.  Initially, the
	 * supplied huffman tree is traversed to generate a lookup table of codes for
	 * each character.  The text file is then read one character at a time, and
	 * each character is encoded by using the lookup table.  The encoded bits for
	 * each character are written one at a time to the specified BitWriter.
	 *
	 * @param fr the FileReader which contains the text file to be encoded
	 *           huffman the huffman tree that was used for compression, and
	 *           hence should be used for decompression
	 *           bw      the BitWriter used to write the compressed bits to file
	 */
	public static void compress(FileReader fr, TreeNode huffman, BitWriter bw) throws Exception
	{

		// IMPLEMENT THIS METHOD
		if (huffman != null)
		{
			TreeMap<Character, String> charMap = getCodes(huffman);
			int c;
			String code;
			while ((c = fr.read()) != -1)
			{
				code = charMap.get((char) c);
				if (code == null || code.length() == 0)
				{
					throw new Exception("�ַ�����Ϊ��");
				}
				for (int i = 0; i < code.length(); i++)
				{
					bw.writeBit(Integer.parseInt(code.substring(i, i + 1)));
				}
			}
		}
	}

	/**
	 * This method reads a frequency file (such as those generated by the
	 * countFrequencies() method) and initialises an ArrayList of TreeNodes
	 * where the item of each TreeNode is a CharFreq object storing a character
	 * from the frequency file and its corresponding frequency.  This method provides
	 * the same functionality as the countFrequencies() method, but takes in a
	 * frequency file as parameter rather than a text file.
	 *
	 * @param inputFreqFile the frequency file which stores characters and their
	 *                      frequency (one character per line)
	 * @return the ArrayList containing TreeNodes.  The item stored in each
	 * TreeNode is a CharFreq object.
	 */
	public static ArrayList readFrequencies(String inputFreqFile) throws Exception
	{

		// IMPLEMENT THIS METHOD
		Reader reader = new InputStreamReader(new FileInputStream(inputFreqFile));
		ArrayList<TreeNode> treeNodeArrayList = new ArrayList<>();
		int c;
		StringBuilder stringBuilder = new StringBuilder();
		String buffer;
		String[] charFreqParts;
		while ((c = reader.read()) != -1)
		{
			stringBuilder.append((char) c);

			// ȥ��ǰ���У����ǲ��Ƿ���һ����¼��ģʽ
			buffer = stringBuilder.toString().trim();
			if (buffer.length() >= FREQ_START.length() + FREQ_MID.length() + FREQ_END.length() + 2)
			{
				if (buffer.substring(0, FREQ_START.length()).equals(FREQ_START) && buffer.substring(buffer.length() - FREQ_END.length()).equals(FREQ_END))
				{
					buffer = buffer.substring(2, buffer.length() - 2);
					charFreqParts = buffer.split(FREQ_MID);
					treeNodeArrayList.add(new TreeNode(new CharFreq(charFreqParts[0].charAt(0), Integer.parseInt(charFreqParts[1]))));
					stringBuilder.delete(0, stringBuilder.length());
				}
			}
		}
		return treeNodeArrayList;
	}

	/* This TextZip application should support the following command line flags:
	QUESTION 2 PART 1
	=================
		 -a : this uses a default prefix code tree and its compressed
		      file, "a.txz", and decompresses the file, storing the output
		      in the text file, "a.txt".  It should also print out the size
		      of the compressed file (in bytes), the size of the decompressed
		      file (in bytes) and the compression ratio
		      �⽫ʹ��ȱʡǰ׺����������ѹ��

				�ļ�������Ȼ���ѹ�ļ����洢���

				���ı��ļ��У���a.txt��������Ӧ�ô�ӡ����С

				��ѹ���ļ�(���ֽ�Ϊ��λ)�У���ѹ���ļ��Ĵ�С

				�ļ�(���ֽ�Ϊ��λ)��ѹ����
	QUESTION 2 PART 2
	=================
		 -f : given a text file (args[1]) and the name of an output frequency file
		      (args[2]) this should count the character frequencies in the text file
		      and store these in the frequency file (with one character and its
		      frequency per line).  It should then build the huffman tree based on
		      the character frequencies, and then print out the prefix code for each
		      character
		       ����һ���ı��ļ�(args[1])�����Ƶ���ļ�������

				(args[2])��Ӧ�ü����ı��ļ��е��ַ�Ƶ��

				������洢��Ƶ���ļ���(����һ���ַ�����ֵ)

				Ƶ��ÿ��)��Ȼ�󹹽����ڹ��������Ĺ�������

				Ȼ���ӡ��ÿ���ַ���ǰ׺����

				�ַ�
	QUESTION 2 PART 3
	=================
		 -c : given a text file (args[1]) and the name of an output frequency file
		      (args[2]) and the name of the output compressed file (args[3]), this
		      should compress file
		      ����һ���ı��ļ�(args[1])�����Ƶ���ļ�������

				(args[2])�����ѹ���ļ�������(args[3])�����

				Ӧ��ѹ���ļ�
	QUESTION 2 PART 4
	=================
		 -d : given a compressed file (args[1]) and its corresponding frequency file
		      (args[2]) and the name of the output decompressed text file (args[3]),
		      this should decompress the file
		      ����һ��ѹ���ļ�(args[1])�����Ӧ��Ƶ���ļ�

				(args[2])�������ѹ���ı��ļ�������(args[3])��

				��Ӧ�û��ѹ�ļ�
	*/

	public static void main(String[] args) throws Exception
	{

		if (args[0].equals("-a")) //ֱ��-a
		{
			BitReader br = new BitReader("src/seven/a.txz");
			FileWriter fw = new FileWriter("src/seven/a.txt");

			// Get the default prefix code tree
			TreeNode tn = abracadbraTree();

			// Decompress the default file "a.txz"
			decompress(br, tn, fw);

			// Close the ouput file
			fw.close();
			// Output the compression ratio
			// Write your own implementation here.
			File input = new File("src/seven/a.txz");
			File output = new File("src/seven/a.txt");
			System.out.printf("Compressed file size: %d bytes\n", input.length());
			System.out.printf("Original file size: %d bytes\n", output.length());
			System.out.printf("Compression ratio: %.2f%%\n", output.length() == 0 ? 0 : ((double) input.length()) / output.length() * 100);
		}

		else if (args[0].equals("-f"))          //-f src/seven/a.txt src/seven/a.freq
		{
			FileReader fr = new FileReader(args[1]);
			PrintWriter pw = new PrintWriter(new FileWriter(args[2]));

			// Calculate the frequencies
			ArrayList trees = countFrequencies(fr, pw);

			// Close the files
			fr.close();
			pw.close();

			// Build the huffman tree
			TreeNode n = buildTree(trees);

			// Display the codes
			traverses(n, "");
		}


		else if (args[0].equals("-c"))   // -c src/seven/a.txt src/seven/a.freq src/seven/test.txz
		{
			FileReader fr = new FileReader(args[1]);
			PrintWriter pw = new PrintWriter(new FileWriter(args[2]));
			ArrayList trees = countFrequencies(fr, pw);

			fr.close();
			pw.close();


			TreeNode n = buildTree(trees);

			// IMPLEMENT NEXT
			// Finish the compress function here
			fr = new FileReader(args[1]);
			File input = new File(args[1]);
			File output = new File(args[3]);
			BitWriter bw = new BitWriter(output);
			compress(fr, n, bw);

			fr.close();
			bw.close();


			// then output the compression ratio
			// Write your own implementation here.
			System.out.println(input.length());
			System.out.printf("Original file size: %d bytes\n", input.length());
			System.out.printf("Compressed file size: %d bytes\n", output.length());
			System.out.printf("Compression ratio: %.2f%%\n", input.length() == 0 ? 0 : ((double) output.length()) / input.length() * 100);

		}

		else if (args[0].equals("-d"))             //-d src/seven/a.txz src/seven/a.freq src/seven/a.txt
		{
            /*given a compressed file (args[1]) and its corresponding frequency file
		      (args[2]) and the name of the output decompressed text file (args[3]),
		      this should decompress the file
		   		����һ��ѹ���ļ�(args[1])�����Ӧ��Ƶ���ļ�(args[2])�������ѹ���ı��ļ�������(args[3])��

				��Ӧ�û��ѹ�ļ�
		    */
			ArrayList a = readFrequencies(args[2]);
			TreeNode tn = buildTree(a);
			BitReader br = new BitReader(args[1]);
			FileWriter fw = new FileWriter(args[3]);
			decompress(br, tn, fw);
			fw.close();

			// Output the compression ratio
			// Write your own implementation here.
			File input = new File(args[1]);
			File output = new File(args[3]);
			System.out.printf("Compressed file size: %d bytes\n", input.length());
			System.out.printf("Original file size: %d bytes\n", output.length());
			System.out.printf("Compression ratio: %.2f%%\n", output.length() == 0 ? 0 : ((double) input.length()) / output.length() * 100);
		}
	}
}