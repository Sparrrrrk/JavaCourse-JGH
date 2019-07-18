import java.nio.file.*;

//�ļ����ƣ��ļ�·�����ļ���С
public class MyFile
{
    private final String name;
    private final Path path;
    private final long size;

    public MyFile(String name, String pathStr, long size)
    {
        this.name = name;
        this.path = Paths.get(pathStr);
        this.size = size;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        else if (this.getClass() != obj.getClass())
        {
            return false;
        }
        else
        {
            MyFile file = (MyFile) obj;
            return this.name.equals(file.name) && this.size == file.size;
        }
    }

    public String getName()
    {
        return name;
    }

    public long getSize()
    {
        return size;
    }

    public String getPath()
    {
        return path.toAbsolutePath().toString();
    }

    public void showFile()
    {
        System.out.printf("�ļ���: %s, �ļ�·��: %s, �ļ���С: %d�ֽ�\n", getName(), getPath(), getSize());
    }
}