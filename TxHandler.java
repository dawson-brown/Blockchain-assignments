import java.util.HashSet;

public class TxHandler {

    private UTXOPool currentUPool;

    /** Creates a public ledger whose current UTXOPool (collection of unspent
     * transaction outputs) is utxoPool. This should make a defensive copy of
     * utxoPool by using the UTXOPool(UTXOPool uPool) constructor.
     */
    public TxHandler(UTXOPool utxoPool){
        this.currentUPool = new UTXOPool(utxoPool);
    }


    /** Returns true if
     * (1) all outputs claimed by tx are in the current UTXO pool,
     * (2) the signatures on each input of tx are valid,
     * (3) no UTXO is claimed multiple times by tx,
     * (4) all of tx’s output values are non-negative, and
     * (5) the sum of tx’s input values is greater than or equal to the sum of
     its output values; and false otherwise.
     */
    public boolean isValidTx​(Transaction tx){

        int i=0;
        HashSet<UTXO> claimed = new HashSet<>();
        double inOutDiff=0;

        for (Transaction.Input in: tx.getInputs()) {

            UTXO utxo = new UTXO(in.prevTxHash, in.outputIndex);

            //verify the current pool contains the claimed output.
            if (!currentUPool.contains(utxo))
                return false;
                //if the current pool does contain the output, verify that output is only claimed once.
            else if (!claimed.add(utxo))
                return false;

            Transaction.Output op = currentUPool.getTxOutput(utxo);

            //verify the signature for each input in tx
            if (!Crypto.verifySignature( op.address, tx.getRawDataToSign(i++), in.signature )) return false;

            inOutDiff += op.value;
        }

        for (Transaction.Output op: tx.getOutputs()) {
            if (op.value < 0) return false;
            inOutDiff -= op.value;
        }

        return inOutDiff>=0;
    }


    /** Handles each epoch by receiving an unordered array of proposed
     * transactions, checking each transaction for correctness,
     * returning a mutually valid array of accepted transactions,
     * and updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs​(Transaction[] possibleTxs){
        return possibleTxs;
    }

}
