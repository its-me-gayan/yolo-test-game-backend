package com.yolo.gamebk.test.service.impl;

import com.yolo.gamebk.test.dto.BetRequest;
import com.yolo.gamebk.test.dto.BetResponse;
import com.yolo.gamebk.test.dto.generic.GenericResponse;
import com.yolo.gamebk.test.service.BetProcessingService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/5/24
 * Time: 2:12 PM
 */
@DisplayName("Betting RTP Calculation Test")
@SpringBootTest
@Log4j2
public class BettingRTPTest {
    private final SecureRandom secureRandomForNumber = new SecureRandom();
    private final SecureRandom secureRandomForBet = new SecureRandom();

    @Autowired
    private BetProcessingService betProcessingService;

    public static BigDecimal calculateRTP(double totalBet, double totalWin) {
        if (totalBet == 0) {
            throw new IllegalArgumentException("Total bet cannot be zero.");
        }
        return BigDecimal.valueOf((totalWin / totalBet) * 100).setScale(2, RoundingMode.HALF_UP);
    }

    @Test
    void bet_RTP_CalculationWithRandomBetAndNumber() throws InterruptedException {


        int rounds = 1000000;
        int threads = 24;

        AtomicReference<Double> totalWin = new AtomicReference<>(0.0);
        AtomicReference<Double> totalSpend = new AtomicReference<>(0.0);

        CountDownLatch latch = new CountDownLatch(rounds);

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        long startTime = System.currentTimeMillis(); // Record start time
        IntStream.range(0, rounds).forEach(i -> {
            executor.submit(() -> {
                try {

                    float randomFloat = 1 + secureRandomForBet.nextFloat() * 99;
                    BigDecimal bigDecimal = new BigDecimal(randomFloat).setScale(2, RoundingMode.HALF_UP);

                    BetRequest betRequest = BetRequest.builder().bet(bigDecimal.doubleValue()).selectedNumber(secureRandomForNumber.nextInt(100) + 1).build();

                    GenericResponse betResponseGenericResponse = betProcessingService.processBet(betRequest);
                    BetResponse data = (BetResponse) betResponseGenericResponse.getData();
                    totalSpend.updateAndGet(aDouble -> aDouble + betRequest.getBet());
                    totalWin.updateAndGet(aDouble -> aDouble + data.getWinAmount());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown(); // Decrease latch count when task completes
                }
            });
        });

        // Wait until all tasks complete (no blocking here)
        latch.await();

        executor.shutdown();
        long endTime = System.currentTimeMillis(); // Record start time

        logFinalInfo(totalSpend, totalWin, startTime, endTime);
    }

    @Test
    void bet_RTP_CalculationWithWithFixBet_Success() throws InterruptedException {
        long startTime = System.currentTimeMillis(); // Record start time

        int rounds = 1000000;
        int threads = 24;
        double bet = 124;

        AtomicReference<Double> totalWin = new AtomicReference<>(0.0);
        AtomicReference<Double> totalSpend = new AtomicReference<>(0.0);

        CountDownLatch latch = new CountDownLatch(rounds);

        ExecutorService executor = Executors.newFixedThreadPool(threads);

        IntStream.range(0, rounds).forEach(i -> {
            executor.submit(() -> {
                try {
                    BetRequest betRequest = BetRequest.builder().bet(bet).selectedNumber(secureRandomForNumber.nextInt(100) + 1).build();

                    GenericResponse betResponseGenericResponse = betProcessingService.processBet(betRequest);
                    BetResponse data = (BetResponse) betResponseGenericResponse.getData();
                    totalSpend.updateAndGet(aDouble -> aDouble + betRequest.getBet());
                    totalWin.updateAndGet(aDouble -> aDouble + data.getWinAmount());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown(); // Decrease latch count when task completes
                }
            });
        });

        // Wait until all tasks complete (no blocking here)
        latch.await();

        executor.shutdown();
        long endTime = System.currentTimeMillis(); // Record start time

        logFinalInfo(totalSpend, totalWin, startTime, endTime);
    }

    private void logFinalInfo(AtomicReference<Double> totalSpend, AtomicReference<Double> totalWin, long startTime, long endTime) {
        BigDecimal finalTotalSpend = BigDecimal.valueOf(totalSpend.get()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal finalTotalWin = BigDecimal.valueOf(totalWin.get()).setScale(2, RoundingMode.HALF_UP);

        System.out.println("Total spend " + finalTotalSpend);
        System.out.println("Total win " + finalTotalWin);

        double rtp = (totalWin.get() / totalSpend.get()) * 100;
        BigDecimal bigDecimalRtp = BigDecimal.valueOf(rtp).setScale(2, RoundingMode.HALF_UP);
        System.out.println("RTP: " + bigDecimalRtp + "%");

        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " milliseconds");
        Assertions.assertTrue(totalSpend.get() > 0);
        Assertions.assertTrue(totalWin.get() > 0);
    }
}
