package blockchain;

public class Main {
    public static void main(String[] args) {

//        Blockchain bc = new Blockchain(6);
//        for (int i = 0; i<4; i++){
//            bc.addBlock();
//        }
//        System.out.println(bc);

        Block b0 = Block.getFirstBlock(0);
        System.out.println(b0);

        Mine m = new Mine(b0, 6);
        System.out.println(m.getResult());


    }
}
