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
            throw new IllegalArgumentException("ָ�����ļ��в�����");
        }
    }

    private List<MyFile> getAllFiles() throws IOException
    {
        final FileVisitor visitor = new FileVisitor(folderPath);
        return visitor.getAllFiles();
    }

    public List<MyFile> getRepeatedFiles() throws IOException
    {
        // ׼�������ļ��б�һ������ɾ����һ�����ڼ��
        final List<MyFile> fileList = getAllFiles();
        final List<MyFile> fileListCopy = new LinkedList<>(fileList);
        final List<MyFile> repeatedFileList = new ArrayList<>();

        for (MyFile file : fileList)
        {
            // ���б���Ĩ���ļ�������ǲ����ظ��ģ����ظ��ľͷŵ��б�����
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
                    System.out.println("û���ظ��ļ�");
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
            System.err.println("������Ч����ָ����Ч�Ҵ����ļ���");
        }
        catch (IOException e)
        {
            System.err.println("IO ����");
        }
    }
}