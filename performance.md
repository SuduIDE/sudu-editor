Performance tests with antivirus off and with dry-run to warm up FS, using graalvm-jdk-22.0.1 

sudu-editor: 2474 folders, 12195 files, 196m 

    java average 0.78175 -> 2.5x faster then node
        time: 0.767s
        time: 0.751s
        time: 0.820s
        time: 0.789s

    graalvm native 0.70175 -> 10% faster then java 21 

        time: 0.686s
        time: 0.690s
        time: 0.712s
        time: 0.719s
    
    node average 1.922
        time: 1.873s
        time: 1.872s
        time: 1.915s
        time: 2.028s

llvm-project: 133.586 Files, 13.366 Folders

    java average 4.12275 -> 3.7x faster then node
        time: 4.097s
        time: 4.182s
        time: 4.138s
        time: 4.074s

    graalvm native exe -> 10% **slower** then java 21
        time: 4.504s
        time: 4.542s
        time: 4.569s
        time: 4.535s
    
    node average 15.2855
        time: 15.155s
        time: 15.473s
        time: 15.189s
        time: 15.325s

