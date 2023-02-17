package blockchain;

import java.util.ArrayList;

public class Blockchain {
    ArrayList<Block> ledger;
    private long numZeros;

    public Blockchain(){
        this.numZeros = 0; // initialize set to 0
        ledger = new ArrayList<>();
        ledger.add(Block.getFirstBlock(numZeros));
    }

    private void balanceDemand(){
//        var lastTime = ledger.get(ledger.size()-1).computeTime();
        long lastTime = 1;
        if (lastTime<1){
            numZeros = numZeros+1;
            System.out.printf("N was increased to %d%n", numZeros);
        } else if (lastTime > 5){
            numZeros = numZeros-1;
            System.out.printf("N was decreased to %d%n", numZeros);
        } else {
            System.out.printf("N stays the same%n");
        }
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i< ledger.size(); i++){
            sb.append(ledger.get(i).toString() + System.lineSeparator());
        }
        return sb.toString();
    }

    public void mine(long numBlocks){
        Mine m;
        Block newBlock = ledger.get(ledger.size()-1);

        //print first block
        System.out.print(newBlock);
        balanceDemand();
        System.out.println();

        //find remaining blocks
        for (int i=1; i<numBlocks; i++){
            m = new Mine(newBlock, numZeros);
            newBlock = m.getResult();
            ledger.add(newBlock);

            System.out.print(newBlock);
            balanceDemand();
            System.out.println();
        }

    }


}
