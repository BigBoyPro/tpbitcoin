/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package tpbitcoin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bitcoinj.core.*;
import org.bitcoinj.params.UnitTestParams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class App {

    public static void main(String[] args) {

        // Q1 hashrate
        System.out.println("\nQ1");
        double estimatedHashrate = new HashRateEstimator(500,5).estimate();
        System.out.print("Hashrate: ");
        System.out.println(estimatedHashrate);

        // Q2: latest block from mainet (bitcoin blockchain) and its predecessor
        Context context = new Context(new UnitTestParams()); // required for working with bitcoinj
        Explorer explorer = new Explorer(); // for interacting with blockchain.info API
        String latestHash = explorer.getLatestHash();
        Block latestBlock = explorer.getBlockFromHash(context.getParams(), latestHash);

        System.out.println("\nQ2");
        System.out.print("Nonce: ");
        System.out.println(latestBlock.getNonce());
        System.out.print("Niveau de difficulté: ");
        System.out.println(latestBlock.getDifficultyTargetAsInteger());

        // Q3 Some TXs
        System.out.println("\nQ3");
        System.out.print("Transaction 1: ");
        System.out.println(latestBlock.getTransactions().get(0).getHashAsString());
        System.out.print("Transaction 2: ");
        System.out.println(latestBlock.getTransactions().get(1).getHashAsString());

        // Q4 Mine a new block
        Miner miner = new Miner(context.getParams());
        ArrayList<Transaction> txs = new ArrayList<>();

        // Q5
        System.out.println("\nQ5");
        System.out.println("Non, mon bloc ne serait pas accepté par le réseau car il ne répond pas à la difficulté actuelle du réseau Bitcoin et manque la preuve de travail valide requise.");

        // Q6
        System.out.println("\nQ6");
        long gpuHashrate = 45_000_000; // 45 MH/s
        System.out.println("Avec un hashrate de 45 MH/s et le niveau de difficulté donné, il me faudrait des années pour miner un bloc seul");

        // Q7
        System.out.println("\nQ7");
        BigInteger difficulty = BigInteger.valueOf(2).pow(20);
        long predictedTimeSeconds = ImpactUtils.expectedMiningTime(gpuHashrate, difficulty);
        double estimatedYears = predictedTimeSeconds / (365.25 * 24 * 3600);
        System.out.print("Temps moyen pour miner un bloc sur ma machine : ");
        System.out.println(estimatedYears + " years");

        // Q8
        System.out.println("\nQ8");
        BigInteger blockDifficulty = latestBlock.getDifficultyTargetAsInteger();
        double totalNetworkHashrate = ImpactUtils.calculateNetworkHashrate(blockDifficulty);
        System.out.print("Hashrate du réseau : ");
        System.out.println(totalNetworkHashrate + " h/s");

        // Q9/Q10 energy w/ most profitable hardware
        System.out.println("\nQ9/Q10");
        double networkEnergyUsage = ImpactUtils.calculateEnergyConsumptionLast24h(100, 2000);
        System.out.print("Énergie consommée par le réseau bitcoin dans les dernières 24h: ");
        System.out.println(networkEnergyUsage + " kWh");

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(YearMonth.class,new YearMonthAdapter())
                .create();
        List<MiningHardware> hardwares = new ArrayList<>();

        URL resource = App.class.getClassLoader().getResource("hardware.json");
        try(BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
            Type listType = new TypeToken<ArrayList<MiningHardware>>(){}.getType();
            hardwares = gson.fromJson(reader,listType);
        } catch (Exception e) {
            System.err.println("error opening/reading hardware.json " + e.getMessage());
        }
    }
}
