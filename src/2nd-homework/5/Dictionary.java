import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Scanner;


public class Dictionary
{
    private final Path path;
    private final String WORD_PREFIX = "%%~WORD||";
    private final String DESCRIPTION_PREFIX = "%%~DESCRIPTION||";
    private final String WORD_SEPARATOR = "--";

    public Dictionary(String filePath) throws IOException
    {
        // ���û�����·������һ��Path����
        this.path = Paths.get(filePath);
        // �����ļ���
        if (Files.notExists(path.getParent()))
        {
            Files.createDirectories(path.getParent());
        }
        // ����ļ������ڣ��ʹ����ļ�
        if (Files.notExists(path))
        {
            Files.createFile(path);
        }
    }

    /*
     * �ļ��ṹ
     * --
     * %%~WORD||balabala
     * %%~DESCRIPTION||balabala
     * --
     * %%~WORD||
     * %%~DESCRIPTION||
     * --
     * */
    public void add(String word, String description) throws IOException
    {
        // ��Ѱ���ǲ����Ѿ�������������ˣ�������ڵõ���������
        final int lineToDeleteNum = find(word);

        // �Ƚ��µĽ��ͷŵ��ļ�ĩβ
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(path.toFile(), true), StandardCharsets.UTF_8))
        {
            writer.append(String.format("%s%s\n%s%s\n%s\n", WORD_PREFIX, word, DESCRIPTION_PREFIX, description, WORD_SEPARATOR));
        }

        // �����������Ѿ�����
        if (lineToDeleteNum != 0)
        {
            // ����һ����ʱ�ļ�
            Path tempFilePath = Files.createTempFile(null, ".dat");
            File tempFile = tempFilePath.toFile();

            // ����Ҫ��������ݷŵ���ʱ�ļ��У�ɾ�������ݺ���
            try (Scanner scanner = new Scanner(path, StandardCharsets.UTF_8);
                 Writer tempWriter = new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8))
            {
                int nowLineNum = 0;//��ǰ���ڶ�ȡ����
                String buffer;
                while (scanner.hasNextLine())
                {
                    nowLineNum++;
                    // ������˸�ɾ�����У�������
                    if (nowLineNum == lineToDeleteNum)
                    {
                        while (!scanner.nextLine().equals(WORD_SEPARATOR))
                        {
                            nowLineNum++;
                            scanner.nextLine();
                        }
                    }
                    // ������Ǹ�ɾ���ģ��Ͷ�ȡ�������ʱ�ļ�
                    else
                    {
                        buffer = scanner.nextLine() + "\n";
                        tempWriter.append(buffer);
                    }
                }
            }
            Files.move(tempFilePath, path, StandardCopyOption.REPLACE_EXISTING);
        }

    }

    // Ѱ���ļ����Ƿ����������ʵĽ��ͣ�������λ�á���������ڷ���0
    private int find(String word) throws IOException
    {
        String buffer;
        int lineNumber = 0;
        int lineNumberCopy = 0;
        try (Scanner scanner = new Scanner(path, StandardCharsets.UTF_8))
        {
            while (scanner.hasNextLine())
            {
                buffer = scanner.nextLine();
                lineNumberCopy++;
                if (buffer.length() >= WORD_PREFIX.length() && buffer.substring(0, WORD_PREFIX.length()).equals(WORD_PREFIX))
                {
                    if (buffer.substring(WORD_PREFIX.length()).equals(word))
                    {
                        lineNumber = lineNumberCopy;
                        break;
                    }
                }
            }
        }
        return lineNumber;
    }

    public void print(String word) throws IOException
    {
        int lineNumber = find(word);
        String buffer;
        if (lineNumber != 0)
        {
            try (Scanner scanner = new Scanner(path, StandardCharsets.UTF_8))
            {
                for (int i = 0; i < lineNumber - 1; i++)
                {
                    scanner.nextLine();
                }
                buffer = scanner.nextLine();
                System.out.printf("����: %s\n", buffer.substring(WORD_PREFIX.length()));
                System.out.print("����: ");
                buffer = scanner.nextLine();
                while (!buffer.equals(WORD_SEPARATOR))
                {
                    System.out.print(buffer.substring(DESCRIPTION_PREFIX.length()));
                    buffer = scanner.nextLine();
                }
                System.out.println();
            }
        }
        else
        {
            System.out.println("���ʲ�����");
        }
    }

    public static void main(String[] args)
    {
        try
        {
            Dictionary dictionary = new Dictionary("src/directory.dat");
            final int ADD_WORD = 0;
            final int FIND_WORD = 1;
            final int EXIT = 2;
            Scanner in = new Scanner(System.in);
            int operation;
            while (true)
            {
                System.out.printf("������Ҫ���еĲ��� %d-��ӵ��� %d-��ѯ���� %d-�˳�\n", ADD_WORD, FIND_WORD, EXIT);
                operation = in.nextInt();
                in.nextLine();
                if (operation < ADD_WORD || operation > EXIT)
                {
                    System.out.println("������Ч������������");
                }
                else if (operation == ADD_WORD)
                {
                    String word, description;
                    System.out.print("�����뵥��: ");
                    word = in.nextLine();
                    System.out.print("���������: ");
                    description = in.nextLine();
                    dictionary.add(word, description);
                    System.out.println("������ϡ����س��������˵�");
                    in.nextLine();
                }
                else if (operation == FIND_WORD)
                {
                    String word;
                    System.out.print("�����뵥��: ");
                    word = in.nextLine();
                    dictionary.print(word);
                    System.out.println("��ѯ��ϡ����س��������˵�");
                    in.nextLine();
                }
                else if (operation == EXIT)
                {
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}