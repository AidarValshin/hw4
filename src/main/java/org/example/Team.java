package org.example;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
public class Team {
    private final long id;

    private final F1Cars[] cars = new F1Cars[2];

    private final PitStop pitStop = new PitStop();

    public Team(long id) {
        log.info("Team {} creating", id);
        this.id = id;
        for (int i = 0; i < this.cars.length; i++) {
            this.cars[i] = new F1Cars(id * 10 + i, pitStop);
        }
        log.info("Team {} created", id);
        pitStop.start();
    }

    public void prepareRace(Race race) {
        for (int i = 0; i < this.cars.length; i++) {
            this.cars[i].prepareRace(race);
        }
    }


}
