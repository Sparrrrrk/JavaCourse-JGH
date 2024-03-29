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
        // 从用户输入路径创建一个Path对象
        this.path = Paths.get(filePath);
        // 创建文件夹
        if (Files.notExists(path.getParent()))
        {
            Files.createDirectories(path.getParent());
        }
        // 如果文件不存在，就创建文件
        if (Files.notExists(path))
        {
            Files.createFile(path);
        }
    }

    /*
     * 文件结构
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
        // 先寻找是不是已经存在这个单词了，如果存在得到所在行数
        final int lineToDeleteNum = find(word);

        // 先将新的解释放到文件末尾
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(path.toFile(), true), StandardCharsets.UTF_8))
        {
            writer.append(String.format("%s%s\n%s%s\n%s\n", WORD_PREFIX, word, DESCRIPTION_PREFIX, description, WORD_SEPARATOR));
        }

        // 如果这个单词已经存在
        if (lineToDeleteNum != 0)
        {
            // 创建一个临时文件
            Path tempFilePath = Files.createTempFile(null, ".dat");
            File tempFile = tempFilePath.toFile();

            // 把想要保存的内容放到临时文件中，删除的内容忽略
            try (Scanner scanner = new Scanner(path, StandardCharsets.UTF_8);
                 Writer tempWriter = new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8))
            {
                int nowLineNum = 0;//当前正在读取的行
                String buffer;
                while (scanner.hasNextLine())
                {
                    nowLineNum++;
                    // 如果到了该删除的行，就跳过
                    if (nowLineNum == lineToDeleteNum)
                    {
                        while (!scanner.nextLine().equals(WORD_SEPARATOR))
                        {
                            nowLineNum++;
                            scanner.nextLine();
                        }
                    }
                    // 如果不是该删除的，就读取并存进临时文件
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

    // 寻找文件中是否存在这个单词的解释，返回其位置。如果不存在返回0
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
                System.out.printf("单词: %s\n", buffer.substring(WORD_PREFIX.length()));
                System.out.print("解释: ");
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
            System.out.println("单词不存在");
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
                System.out.printf("请输入要进行的操作 %d-添加单词 %d-查询单词 %d-退出\n", ADD_WORD, FIND_WORD, EXIT);
                operation = in.nextInt();
                in.nextLine();
                if (operation < ADD_WORD || operation > EXIT)
                {
                    System.out.println("操作无效。请重新输入");
                }
                else if (operation == ADD_WORD)
                {
                    String word, description;
                    System.out.print("请输入单词: ");
                    word = in.nextLine();
                    System.out.print("请输入解释: ");
                    description = in.nextLine();
                    dictionary.add(word, description);
                    System.out.println("输入完毕。按回车返回主菜单");
                    in.nextLine();
                }
                else if (operation == FIND_WORD)
                {
                    String word;
                    System.out.print("请输入单词: ");
                    word = in.nextLine();
                    dictionary.print(word);
                    System.out.println("查询完毕。按回车返回主菜单");
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