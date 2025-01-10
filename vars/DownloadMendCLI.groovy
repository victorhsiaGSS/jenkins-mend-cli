def call() { 
      echo 'Downloading Mend CLI'
      sh 'mkdir C:\mend\cli && curl https://downloads.mend.io/cli/windows_amd64/mend.exe -o C:\mend\cli\mend.exe'
}
