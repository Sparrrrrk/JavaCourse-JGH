import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class FileRepeatSearcher
{
    private final Path folderPath;

    public FileRepeatSearcher(String folderPath) throws IllegalArgumentException
    {
        this.folderPath = Paths.get(folderPath);
        if (Files.notExists(this.folderPath))
        {
            throw new IllegalArgumentException("指定的文件夹不存在");
        }
    }

    private List<MyFile> getAllFiles() throws IOException
    {
        final FileVisitor visitor = new FileVisitor(folderPath);
        return visitor.getAllFiles();
    }

    public List<MyFile> getRepeatedFiles() throws IOException
    {
        // 准备两份文件列表，一份用于删除，一份用于检查
        final List<MyFile> fileList = getAllFiles();
        final List<MyFile> fileListCopy = new LinkedList<>(fileList);
        final List<MyFile> repeatedFileList = new ArrayList<>();

        for (MyFile file : fileList)
        {
            // 从列表中抹掉文件，检查是不是重复的，是重复的就放到列表里面
            fileListCopy.remove(file);
            if (fileListCopy.contains(file) || repeatedFileList.contains(file))
            {
                repeatedFileList.add(file);
            }
        }
        return repeatedFileList;
    }

    public static void main(String[] args)
    {
        try
        {
            if (args.length != 1)
            {
                throw new IllegalArgumentException();
            }
            else
            {
                final FileRepeatSearcher searcher = new FileRepeatSearcher(args[0]);
                final List<MyFile> repeatedFileList = searcher.getRepeatedFiles();
                if (repeatedFileList.size() == 0)
                {
                    System.out.println("没有重复文件");
                }
                else
                {
                    for (MyFile file : repeatedFileList)
                    {
                        file.showFile();
                    }
                }
            }
        }
        catch (IllegalArgumentException e)
        {
            System.err.println("参数无效。请指定有效且存在文件夹");
        }
        catch (IOException e)
        {
            System.err.println("IO 出错");
        }
    }
}