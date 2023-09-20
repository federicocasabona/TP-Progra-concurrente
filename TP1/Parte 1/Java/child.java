import java.io.IOException;

class ChildProcess
{
    public static void showInfo( String id )
    {
        ProcessHandle yo = ProcessHandle.current();
        long pid  = yo.pid();
        long ppid = yo.parent().get().pid();
        System.out.println(id + " PID: " + pid + " PPID: " + ppid );
    }

    public static void main(String[] args) throws IOException,InterruptedException
    {
        showInfo(args[0]);
        if(args[0].equals("B"))
        {
            //Proceso D
            ProcessBuilder processD = new ProcessBuilder("java", "ChildProcess.java", "D");
            processD.inheritIO();
            Process pD = processD.start();

            //Proceso E
            ProcessBuilder processE = new ProcessBuilder("java", "ChildProcess.java", "E");
            processE.inheritIO();
            Process pE = processE.start();

            pD.waitFor();
            pE.waitFor();
        }

        if(args[0].equals("C"))
        {
            //Proceso F
            ProcessBuilder processF = new ProcessBuilder("java", "ChildProcess.java", "F");
            processF.inheritIO();
            Process pF = processF.start();

            pF.waitFor();
        }

        if(args[0].equals("E"))
        {
            //Proceso G
            ProcessBuilder processG = new ProcessBuilder("java", "ChildProcess.java", "G");
            processG.inheritIO();
            Process pG = processG.start();

            //Proceso H
            ProcessBuilder processH = new ProcessBuilder("java", "ChildProcess.java", "H");
            processH.inheritIO();
            Process pH = processH.start();

            pG.waitFor();
            pH.waitFor();
        }

        if(args[0].equals("G"))
          Thread.sleep(100000); // 100 segundos

        if(args[0].equals("H"))
          Thread.sleep(100000); // 100 segundos

        if(args[0].equals("D"))
          Thread.sleep(100000); // 100 segundos

        if(args[0].equals("F"))
          Thread.sleep(100000); // 100 segundos
    }
}
