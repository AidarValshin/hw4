package org.example;

import lombok.extern.log4j.Log4j2;


@Log4j2
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("starting race");
        Team teams[] = new Team[3];

        for (int i = 0; i < teams.length; i++) {
            teams[i] = new Team(i + 1);
        }
        log.info("Teams created");
        Race race = new Race(1000, teams);
        log.info("starting race");
        race.start();
        race.printResults();
    }
}
