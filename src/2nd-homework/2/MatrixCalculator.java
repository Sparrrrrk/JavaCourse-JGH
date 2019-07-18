/*�����ı��ļ����ļ�����Ϊa.txt���ļ�����Ϊһ��8��8�е��ַ���������Ϊ1��0�ַ���
    ���̼�����þ�����ˮƽ������ߴ�ֱ�������б�߷�������1���ĸ�����*/

import java.io.*;
import java.nio.charset.*;
import java.util.Scanner;


public class MatrixCalculator
{
	private final int MATRIX_SIZE = 8;
	private final int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];

	public MatrixCalculator(String filePath, Charset charset) throws FileNotFoundException, MatrixFormatException
	{
		String buffer;
		try (Scanner scanner = new Scanner(new FileInputStream(filePath), charset))
		{
			int row;
			for (row = 0; scanner.hasNextLine(); row++)
			{
				buffer = scanner.nextLine();
				if (buffer.length() != MATRIX_SIZE)
				{
					throw new MatrixFormatException("No enough cols");
				}
				for (int col = 0; col < MATRIX_SIZE; col++)
				{
					matrix[row][col] = buffer.charAt(col) == '0' ? 0 : 1;
				}
			}
			if (row != MATRIX_SIZE)
			{
				throw new MatrixFormatException("No enough rows");
			}
		}
	}

	public void showMatrix()
	{
		printMatrix(matrix);
		System.out.println();
	}

	public static void printMatrix(int[][] matrix)
	{
		for (int[] row : matrix)
		{
			for (int num : row)
			{
				System.out.printf("%-3d", num);
			}
			System.out.println();
		}
	}

	// �����������ֱ������ض�λ�ó�����ˮƽ����ֱ��б������1�ĸ���

	private int horizontalCalculate(int row, int col)
	{
		int num = 0;//����1�ĸ���
		// ������Լ�������1���Ǿ�ֱ�ӷ���0
		if (matrix[row][col] == 1)
		{
			int colCopy = col;
			// ��������
			while (colCopy >= 0 && matrix[row][colCopy] == 1)
			{
				num++;
				colCopy--;
			}

			colCopy = col + 1;
			// ��������
			while (colCopy <= MATRIX_SIZE - 1 && matrix[row][colCopy] == 1)
			{
				num++;
				colCopy++;
			}
		}
		return num;
	}

	private int verticalCalculate(int row, int col)
	{
		int num = 0;//����1�ĸ���
		// ������Լ�������1���Ǿ�ֱ�ӷ���0
		if (matrix[row][col] == 1)
		{
			int rowCopy = row;
			// ��������
			while (rowCopy >= 0 && matrix[rowCopy][col] == 1)
			{
				num++;
				rowCopy--;
			}

			rowCopy = row + 1;
			while (rowCopy <= MATRIX_SIZE - 1 && matrix[rowCopy][col] == 1)
			{
				num++;
				rowCopy++;
			}
		}
		return num;
	}

	// �����ϵ�����
	private int mainSlantedCalculate(int row, int col)
	{
		int num = 0;//����1�ĸ���
		// ������Լ�������1���Ǿ�ֱ�ӷ���0
		if (matrix[row][col] == 1)
		{
			int rowCopy = row;
			int colCopy = col;
			// ����������
			while (rowCopy >= 0 && colCopy >= 0 && matrix[rowCopy][colCopy] == 1)
			{
				num++;
				rowCopy--;
				colCopy--;
			}

			// ����������
			rowCopy = row + 1;
			colCopy = col + 1;
			while (rowCopy <= MATRIX_SIZE - 1 && colCopy <= MATRIX_SIZE - 1 && matrix[rowCopy][colCopy] == 1)
			{
				num++;
				rowCopy++;
				colCopy++;
			}
		}
		return num;
	}

	// �����ϵ�����
	private int viceSlantedCalculate(int row, int col)
	{
		int num = 0;//����1�ĸ���
		// ������Լ�������1���Ǿ�ֱ�ӷ���0
		if (matrix[row][col] == 1)
		{
			int rowCopy = row;
			int colCopy = col;
			// ����������
			while (rowCopy >= 0 && colCopy <= MATRIX_SIZE - 1 && matrix[rowCopy][colCopy] == 1)
			{
				num++;
				rowCopy--;
				colCopy++;
			}

			// ����������
			rowCopy = row + 1;
			colCopy = col - 1;
			while (rowCopy <= MATRIX_SIZE - 1 && colCopy >= 0 && matrix[rowCopy][colCopy] == 1)
			{
				num++;
				rowCopy++;
				colCopy--;
			}
		}
		return num;
	}

	private static int max(int... arr)
	{
		int max = arr[0];
		for (int i : arr)
		{
			if (i > max)
			{
				max = i;
			}
		}
		return max;
	}

	// �½�һ�����󣬶�����λ�ý��������������������㣬ȡ���ֵ�������ȡ�����������ֵ
	public int calculate()
	{
		int[][] maxMatrix = new int[8][8];
		for (int row = 0; row < MATRIX_SIZE; row++)
		{
			for (int col = 0; col < MATRIX_SIZE; col++)
			{
				maxMatrix[row][col] = max(horizontalCalculate(col, row), verticalCalculate(col, row), mainSlantedCalculate(row, col), viceSlantedCalculate(row, col));
			}
		}
		int maxVal = 0;
		for (int[] row : maxMatrix)
		{
			for (int i : row)
			{
				if (i > maxVal)
				{
					maxVal = i;
				}
			}
		}
		printMatrix(maxMatrix);
		return maxVal;
	}

	public static void main(String[] args)
	{
		try
		{
			MatrixCalculator calculator = new MatrixCalculator("src/test.txt", StandardCharsets.UTF_8);
			calculator.showMatrix();
			System.out.println(calculator.calculate());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}