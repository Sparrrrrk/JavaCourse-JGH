import java.util.*;


public class PrimeNumberCalculator
{
    private List<Integer> primeNumberList;
    private final static int LIMIT = 10000;

    public PrimeNumberCalculator()
    {
        primeNumberList = new ArrayList<>();
        getPrimeNumbers();
    }

    private void getPrimeNumbers()
    {
        boolean isPrimeNumber;
        for (int i = 2; i < LIMIT; i++)
        {
            isPrimeNumber = true;
            for (int j = 2; j <= Math.sqrt(i); j++)
            {
                if (i % j == 0)
                {
                    isPrimeNumber = false;
                    break;
                }
            }
            if (isPrimeNumber)
            {
                primeNumberList.add(i);
            }
        }
    }

    public List<Integer> getValidPrimeNumbers()
    {
        List<Integer> validPrimeNumberList = new ArrayList<>();
        for (int num : primeNumberList)
        {
            if (isValidPrimeNumber(num))
            {
                validPrimeNumberList.add(num);
            }
        }
        return validPrimeNumberList;
    }

    public void printValidPrimeNumbers()
    {
        List<Integer> validPrimeNumberList = getValidPrimeNumbers();
        System.out.printf("������������������%d��\n", validPrimeNumberList.size());
        for (int i = 0; i < validPrimeNumberList.size(); i++)
        {
            if (i % 10 == 0)
            {
                System.out.println();
            }
            System.out.printf("%8d", validPrimeNumberList.get(i));
        }
    }

    private boolean isValidPrimeNumber(int number)
    {
        String numberStr = String.valueOf(number);
        // �������ַ������е������ָ�����ݹ�
        for (int i = 1; i < numberStr.length(); i++)
        {
            if (isValidPrimeNumberRecursive(numberStr.substring(0, i)) && isValidPrimeNumberRecursive(numberStr.substring(i)))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isValidPrimeNumberRecursive(String numberPart)
    {
        // �����һλ��0����ôֱ����Ϊ��������
        if (numberPart.charAt(0) == '0')
        {
            return false;
        }
        // ���������Ѿ����������ǾͲ��ü����ָ���
        else if (isPrimeNumber(Integer.parseInt(numberPart)))
        {
            return true;
        }
        // ���������ҵ�һλ����0�����������ָ�ݹ顣ֻҪ��һ�ַָ��������������
        else
        {
            for (int i = 1; i < numberPart.length(); i++)
            {
                if (isValidPrimeNumberRecursive(numberPart.substring(0, i)) && isValidPrimeNumberRecursive(numberPart.substring(i)))
                {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean isPrimeNumber(int number)
    {
        return primeNumberList.contains(number);
    }

    public static void main(String[] args)
    {
        PrimeNumberCalculator calculator = new PrimeNumberCalculator();
        calculator.printValidPrimeNumbers();
    }
}