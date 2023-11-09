package de.unitrier.st.uap.w23.tram;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Scanner;
import org.apache.logging.log4j.core.LoggerContext;

public class AbstractMachine {

    private int PC, PP, FP, TOP;
    private int[] STACK;
    private Instruction[] program;

    private String logSettings;
    private final LoggerContext ctx = (LoggerContext) LogManager.getContext(LogManager.class.getClassLoader(), false);
    private final Logger consoleLogger = ctx.getLogger("consoleLogger");
    private final Logger fileLogger = ctx.getLogger("fileLogger");

    private final int STACK_MIN_SIZE = 10;

    public AbstractMachine(Instruction[] program, String logSettings){
        this(program);
        this.logSettings = logSettings;
    }

    public AbstractMachine(Instruction[] program){
        this.PC = 0;
        this.PP = 0;
        this.FP = 0;
        this.TOP = -1;

        STACK = new int[STACK_MIN_SIZE];

        this.program = program;
    }

    public void run(){
        if(logSettings != null){
            boolean consoleLog = logSettings.contains("c");
            boolean fileLog = logSettings.contains("f");
            boolean stepWise = logSettings.contains("s");
            boolean showCode = logSettings.contains("p");

            if(showCode){
                String programString = "";
                int lineNr=0;
                for(Instruction instr: program) {
                    if (instr != null) {
                        programString += (String.format("%03d", lineNr) + "| " + instr.toString() + "\n");
                        lineNr++;
                    }
                }
                if(consoleLog)consoleLogger.debug("Current Program Code:\n" + programString);
                if(fileLog)fileLogger.debug("Current Program Code:\n" + programString);
                if(!consoleLog && !fileLog) System.out.println("Current Program Code:\n" + programString);
            }

            int runTimeCounter = 1;
            if(consoleLog)consoleLogger.debug(stepToString("Startkonfiguration", 0));
            if(fileLog)fileLogger.debug(stepToString("Startkonfiguration", 0));
            while(PC >= 0){
                if(stepWise) waitForEnter();
                int currentPC = PC;
                execute(program[PC]);
                if(consoleLog){consoleLogger.debug(stepToString(program[currentPC].toString(),runTimeCounter));}
                if(fileLog){fileLogger.debug(stepToString(program[currentPC].toString(),runTimeCounter));}
                runTimeCounter++;
            }

        }
        else{
            while(PC >= 0){execute(program[PC]);}
        }
    }

    public static void waitForEnter() {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    private void execute(Instruction instr){
        switch (instr.getOpcode())
        {
            case Instruction.CONST:
                doConst(instr.getArg1());
                break;
            case Instruction.LOAD:
                doLoad(instr.getArg1(), instr.getArg2());
                break;
            case Instruction.STORE:
                doStore(instr.getArg1(), instr.getArg2());
                break;
            case Instruction.ADD:
                doAdd();
                break;
            case Instruction.SUB:
                doSub();
                break;
            case Instruction.MUL:
                doMul();
                break;
            case Instruction.DIV:
                doDiv();
                break;
            case Instruction.LT:
                doLt();
                break;
            case Instruction.GT:
                doGt();
                break;
            case Instruction.EQ:
                doEq();
                break;
            case Instruction.NEQ:
                doNeq();
                break;
            case Instruction.IFZERO:
                doIfZero(instr.getArg1());
                break;
            case Instruction.GOTO:
                doGoto(instr.getArg1());
                break;
            case Instruction.HALT:
                doHalt();
                break;
            case Instruction.NOP:
                doNop();
                break;
            case Instruction.INVOKE:
                doInvoke(instr.getArg1(), instr.getArg2(), instr.getArg3());
                break;
            case Instruction.RETURN:
                doReturn();
                break;
            case Instruction.POP:
                doPop();
                break;
            default:
                // Error
        }
    }

    private void ensureStackSize(int i){
        if(TOP + i + 1 > STACK.length){
            STACK = Arrays.copyOf(STACK, STACK.length * 2);
        }
    }

    private void checkShrinkStackSize(){
        if(TOP <= STACK.length/4){
            if(STACK.length/2 > 32){
                STACK = Arrays.copyOf(STACK, STACK.length/2);
            }
            else{
                STACK = Arrays.copyOf(STACK, STACK_MIN_SIZE);
            }
        }
    }

    private void doConst(int k){
        ensureStackSize(1);
        STACK[TOP+1] = k;
        TOP = TOP + 1;
        PC = PC + 1;
    }

    private void doAdd(){
        STACK[TOP-1] = STACK[TOP-1] + STACK[TOP];
        TOP = TOP - 1;
        PC = PC + 1;
        checkShrinkStackSize();
    }

    private void doPop(){
        TOP = TOP - 1;
        PC = PC + 1;
        checkShrinkStackSize();
    }

    private void doSub(){
        STACK[TOP-1] = STACK[TOP-1] - STACK[TOP];
        TOP = TOP - 1;
        PC = PC + 1;
        checkShrinkStackSize();
    }

    private void doMul(){
        STACK[TOP-1] = STACK[TOP-1] * STACK[TOP];
        TOP = TOP - 1;
        PC = PC + 1;
        checkShrinkStackSize();
    }

    private void doDiv(){
        STACK[TOP-1] = STACK[TOP-1] / STACK[TOP];
        TOP = TOP - 1;
        PC = PC + 1;
        checkShrinkStackSize();
    }

    private void doLt(){
        if(STACK[TOP-1] < STACK[TOP]){
            STACK[TOP-1] = 1;
        }
        else{
            STACK[TOP-1] = 0;
        }
        TOP = TOP - 1;
        PC = PC + 1;
        checkShrinkStackSize();
    }

    private void doGt(){
        if(STACK[TOP-1] > STACK[TOP]){
            STACK[TOP-1] = 1;
        }
        else{
            STACK[TOP-1] = 0;
        }
        TOP = TOP - 1;
        PC = PC + 1;
        checkShrinkStackSize();
    }

    private void doEq(){
        if(STACK[TOP-1] == STACK[TOP]){
            STACK[TOP-1] = 1;
        }
        else{
            STACK[TOP-1] = 0;
        }
        TOP = TOP - 1;
        PC = PC + 1;
        checkShrinkStackSize();
    }

    private void doNeq(){
        if(STACK[TOP-1] != STACK[TOP]){
            STACK[TOP-1] = 1;
        }
        else{
            STACK[TOP-1] = 0;
        }
        TOP = TOP - 1;
        PC = PC + 1;
        checkShrinkStackSize();
    }

    private void doGoto(int p){
        PC = p;
    }

    private void doIfZero(int p){
        if(STACK[TOP] == 0){
            PC = p;
        }
        else{
            PC = PC + 1;
        }
        TOP = TOP - 1;
        checkShrinkStackSize();
    }

    private void doHalt(){
        PC = -1;
    }

    private void doNop(){
        PC = PC + 1;
    }

    private void doInvoke(int n, int p, int d){
        ensureStackSize(5);
        STACK[TOP + 1] = PC + 1;
        STACK[TOP + 2] = PP;
        STACK[TOP + 3] = FP;
        STACK[TOP + 4] = spp(d, PP, FP);
        STACK[TOP + 5] = sfp(d, PP, FP);
        PP = TOP - n + 1;
        FP = TOP + 1;
        TOP = TOP + 5;
        PC = p;
    }

    private int spp(int d, int pp, int fp){
        if(d == 0) return pp;
        else return spp(d-1, STACK[fp+3], STACK[fp+4]);
    }

    private int sfp(int d, int pp, int fp){
        if(d == 0) return fp;
        else return sfp(d-1, STACK[fp+3], STACK[fp +4]);
    }

    private void doReturn(){
        int res = STACK[TOP];
        TOP = PP;
        PC = STACK[FP];
        PP = STACK[FP+1];
        FP = STACK[FP+2];
        STACK[TOP] = res;
    }

    private void doStore(int k, int d){
        STACK[spp(d,PP,FP) + k] = STACK[TOP];
        TOP = TOP - 1;
        PC = PC + 1;
        checkShrinkStackSize();
    }

    private void doLoad(int k, int d){
        ensureStackSize(1);
        STACK[TOP+1] = STACK[spp(d,PP,FP) + k];
        TOP = TOP + 1;
        PC = PC + 1;
    }

    private String configToString(){
        StringBuilder sb = new StringBuilder();
        sb.append("PC = ").append(PC).append(", ");
        sb.append("PP = ").append(PP).append(", ");
        sb.append("FP = ").append(FP).append(", ");
        sb.append("TOP = ").append(TOP).append(", ");

        sb.append("STACK = (");
        if (TOP >= 0) {
            for (int i = 0; i <= TOP; i++) {
                sb.append(STACK[i]);
                if (i < TOP) {
                    sb.append(", ");
                }
            }
        }
        sb.append(")");

        return sb.toString();
    }

    private String stepToString(String methodCall, int runTimeCounter){
        String format = "%-4s %-" + 20 + "s %s%n";
        return String.format(format, runTimeCounter, methodCall, configToString());
    }

}