import java.io.IOException;

class ParentProcess
{
    static Process childs[];

    public static void createChilds() throws IOException,InterruptedException
    {
            //Proceso B
            ProcessBuilder processB = new ProcessBuilder("java", "ChildProcess.java", "B");
            processB.inheritIO();
            Process pB = processB.start();

            // Proceso C
            ProcessBuilder processC = new ProcessBuilder("java", "ChildProcess.java", "C");
            processC.inheritIO();
            Process pC = processC.start();

            pB.waitFor();
            pC.waitFor();
    }

    public static void main(String[] args) throws IOException, InterruptedException
    {
        ProcessHandle yo = ProcessHandle.current();
        long pid  = yo.pid();
        long ppid = yo.parent().get().pid();
        System.out.println("A" + " PID: " + pid + " PPID: " + ppid );
        createChilds();
    }

}
