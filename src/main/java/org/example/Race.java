package org.example;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
@Getter
@Log4j2
@Setter
public class Race {
    @Getter
    private static AtomicBoolean isRaceStarted = new AtomicBoolean(false);

    private long distance;

    private List<F1Cars> participantCars = new java.util.ArrayList<>();

    private List<Team> teams = new java.util.ArrayList<>();

    private static CountDownLatch finishSignal;


    public Race(long distance, Team[] participantCars) {
        this.distance = distance;
        teams.addAll(List.of(participantCars));
        for(Team team: participantCars) {
            for(F1Cars f1Car: team.getCars()){
                register(f1Car);
            }
        }
        long carsCount = this.participantCars.size();
        finishSignal = new CountDownLatch((int) carsCount);

    }

    /**
     * Запускаем гонку
     */
    public void start() throws InterruptedException {
        for (Team team : teams) {
            team.prepareRace(this);
        }
        isRaceStarted.setRelease(true); //даем команду на старт гонки
        log.info("Race started");
        finishSignal.await();// блокируем поток до завершения гонки
    }


    //Регистрируем участников гонки
    public void register(F1Cars participantCar) {
        participantCars.add(participantCar);
    }


    public void start(F1Cars f1Cars) {
        //фиксация времени старта
        f1Cars.setStartTime(Timestamp.valueOf(LocalDateTime.now()));
    }

    public long finish(F1Cars participant) {
        //фиксация времени финиша
        finishSignal.countDown();
        return (Timestamp.valueOf(LocalDateTime.now()).getTime() - participant.getStartTime().getTime());
         //длительность гонки у данного участника в миллисекундах
    }

    public void printResults() {
        participantCars.sort(F1Cars::compareTo);
        log.info("Результат гонки:");
        int position = 0;
        for (F1Cars participant : participantCars) {
            log.info("Позиция: {} время: {}", position++, participant.getName());
        }
    }
}
