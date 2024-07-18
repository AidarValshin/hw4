package org.example;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
@Getter
public class PitStop extends Thread {

    PitWorker[] workers = new PitWorker[4];

    AtomicBoolean isEmpty = new AtomicBoolean(true);

    private CountDownLatch finishPit = null;

    private F1Cars currentCar;
    public PitStop() {
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new PitWorker(i, this);
            workers[i].start();
        }
    }

    public void pitline(F1Cars f1Cars) throws InterruptedException {
        while (!isEmpty.get()) {

        }
        isEmpty.setRelease(false);
        currentCar = f1Cars;
        log.info("Car {} on pit", currentCar.getCarId());
        finishPit = new CountDownLatch(4);
        //условие: на питстоп может заехать только 1 пилот
        //держим поток до момента смены всех шин
        //каждую шину меняет отдельный PitWowker поток
        //дожидаемся когда все PitWorker завершат свою работу над машиной
        //метод запускается из потока болида, нужна синхронизация с потоком питстопа
        // отпускаем машину
        finishPit.await();
        log.info("Car {} have left pit", currentCar);
        isEmpty.setRelease(true);
        currentCar = null;
    }


    @Override
    public void run() {
        while(!isInterrupted()){
            //синхронизируем поступающие болиды и работников питстопа при необходимости
        }
    }

    public F1Cars getCar() {
        //Блокируем поток до момента поступления машины на питстоп и возвращаем ее
        while (isEmpty.get()) {

        }
        return currentCar;
    }

    public CountDownLatch getFinishPit() {
        return finishPit;
    }
}
