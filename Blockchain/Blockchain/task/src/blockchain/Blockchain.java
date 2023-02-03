package blockchain;

import java.util.ArrayList;

public class Blockchain {
    ArrayList<Block> ledger;
    private long numZeros;

    public Blockchain(long numZeros){
        this.numZeros = numZeros;
        ledger = new ArrayList<>();
        ledger.add(Block.getFirstBlock(numZeros));
    }

    public void addBlock(){
        Block lastBlock = ledger.get(ledger.size()-1);
        Block nextBlock = Block.getNextBlock(lastBlock, numZeros);
        ledger.add(nextBlock);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i< ledger.size(); i++){
            sb.append(ledger.get(i).toString() + System.lineSeparator());
        }
        return sb.toString();
    }


}
