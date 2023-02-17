package blockchain;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

record Block(long id,
             long timestamp,
             long magic,
             long computeTime,
             long minerId,
             String previousHash,
             String currentHash,
             String blockData) {

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Block: " + System.lineSeparator());
        sb.append("Created by: miner" + minerId + System.lineSeparator());
        sb.append("miner" + minerId + " gets 100 VC" + System.lineSeparator());
        sb.append("Id: " + id + System.lineSeparator());
        sb.append("Timestamp: " + timestamp + System.lineSeparator());
        sb.append("Magic number: "+ magic + System.lineSeparator());
        sb.append("Hash of the previous block: " + System.lineSeparator());
        sb.append(previousHash + System.lineSeparator());
        sb.append("Hash of the block: " + System.lineSeparator());
        sb.append(currentHash + System.lineSeparator());
        sb.append("Block data: " + System.lineSeparator());
        sb.append(blockData + System.lineSeparator());
        sb.append("Block was generating for " + computeTime + " seconds" + System.lineSeparator());
        return sb.toString();
    }


    public static long findMagicNumber(long id, long timestamp, String previousHash, long numZeros) {
        Random rd = new Random();
        long magic = rd.nextLong();
        boolean notProved = true;
        Thread ct = Thread.currentThread();
        while (notProved){
            magic = rd.nextLong();
            if (ct.isInterrupted()){
                notProved = false;
            } else {
                notProved = !isProved(getHash(id, timestamp, magic, previousHash), numZeros);
            }
        }
        return magic;
    }

    public static boolean isProved(String currentHash, long numZeros){
        return currentHash.substring(0, (int) numZeros).chars().allMatch(c -> c == 48); // 48 is '0'
    }

    public static Block getFirstBlock(long numZeros){
        long id = 1;
        long timestamp = new Date().getTime();
        String previousHash = "0";
        var i1 = Instant.now();
        long magic = findMagicNumber(id, timestamp, previousHash, numZeros);
        var i2 = Instant.now();
        var computeTime = i1.until(i2, ChronoUnit.SECONDS);
        String currentHash = getHash(id, timestamp, magic, previousHash);

        return new Block(id, timestamp, magic, computeTime, 0, previousHash, currentHash, "no data");
    }

    public static String getHash(long id, long timestamp, long magic, String previousHash) {
        return StringUtil.applySha256(String.valueOf(id) + String.valueOf(timestamp) + String.valueOf(magic) + previousHash);
    }
}
