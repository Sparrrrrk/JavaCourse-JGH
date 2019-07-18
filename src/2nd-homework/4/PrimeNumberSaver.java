import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
public class PrimeNumberSaver
{
    private List<Integer> primeNumberList;
    private final int LIMIT;

    public PrimeNumberSaver(int limit)
    {
        LIMIT = limit;
        primeNumberList = new ArrayList<>();
        getPrimeNumbers();
    }

    //�������ҹ�Լ��
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

    public void savePrimeNumbers(String outputFilePathStr, Charset charset) throws IOException
    {
        final Path filePath = Paths.get(outputFilePathStr);
        if (Files.notExists(filePath))
        {
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
        }
        try (PrintWriter writer = new PrintWriter(filePath.toFile(), charset))
        {
            for (int num : primeNumberList)
            {
                writer.println(num);
            }
        }
        System.out.printf("�����%d��������%d��", LIMIT, primeNumberList.size());
    }

    public static void main(String[] args)
    {
        try
        {
            PrimeNumberSaver primeNumberSaver = new PrimeNumberSaver(10000);
            primeNumberSaver.savePrimeNumbers("src/primeNumbers.txt", StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
