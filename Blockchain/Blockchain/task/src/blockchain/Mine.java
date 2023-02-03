package blockchain;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Mine {

    private static final int POOL_SIZE = 4;
    private Block result;

    record Miner(Block previousBlock, long numZeros, long minerId) implements Callable<Block> {

        @Override
        public Block call() throws Exception {
            long id = previousBlock.id() + 1;
            long timestamp = new Date().getTime();
            String previousHash = previousBlock.currentHash();
            var i1 = Instant.now();
            long magic = Block.findMagicNumber(id, timestamp, previousHash, numZeros);
            var i2 = Instant.now();
            var computeTime = i1.until(i2, ChronoUnit.SECONDS);
            String currentHash = Block.getHash(id, timestamp, magic, previousHash);

            return new Block(id, timestamp, magic, computeTime, minerId, previousHash, currentHash);
        }
    }

    public Mine(Block previousBlock, long numZeros){
        ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);

        List<Callable<Block>> tasks = new ArrayList<Callable<Block>>();

        tasks.add(new Miner(previousBlock, numZeros, 1));
        tasks.add(new Miner(previousBlock, numZeros, 2));
        tasks.add(new Miner(previousBlock, numZeros, 3));
        tasks.add(new Miner(previousBlock, numZeros, 4));


        try {
            this.result = executorService.invokeAny(tasks);
            executorService.shutdownNow();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public Block getResult(){
        return result;
    }
}
