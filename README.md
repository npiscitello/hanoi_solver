#Installation and Execution
##Build
    cd src/hanoi_solver
    javac -cp .:commons-cli-1.3.1.jar Main.java Peg.java Piece.java BadMoveException.java
##Run
This application was originally developed in Eclipse; to run on the command line:
    cd src
    java -cp .:hanoi_solver/commons-cli-1.3.1.jar hanoi_solver.Main
##Clean
    rm src/hanoi_solver/*.class
