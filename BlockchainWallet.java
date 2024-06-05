import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;
import java.util.Scanner;

public class BlockchainWallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private float balance;

    public BlockchainWallet() {
        generateKeyPair();
        balance = 100.0f; // Initially adding 100 to each account
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            SecureRandom random = SecureRandom.getInstanceStrong();
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public void sendMoney(BlockchainWallet recipient, float amount) {
        if (balance >= amount) {
            balance -= amount;
            recipient.receiveMoney(amount);
            System.out.println("Transaction Successful! Sent " + amount + " from " +
                    getPublicKey() + " to " + recipient.getPublicKey());
        } else {
            System.out.println("Insufficient funds to send.");
        }
    }

    public void receiveMoney(float amount) {
        balance += amount;
    }

    public float getBalance() {
        return balance;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BlockchainWallet walletA = new BlockchainWallet();
        BlockchainWallet walletB = new BlockchainWallet();
        boolean running = true;
        while (running) {
            System.out.println("Choose an option:");
            System.out.println("1. Check Wallet Balance");
            System.out.println("2. Send Money");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("WalletA Balance: " + walletA.getBalance());
                    System.out.println("WalletB Balance: " + walletB.getBalance());
                    break;
                case 2:
                    System.out.println("Enter the amount to send from WalletA to WalletB:");
                    float amountToSend = scanner.nextFloat();
                    walletA.sendMoney(walletB, amountToSend);
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
        scanner.close();
    }
}