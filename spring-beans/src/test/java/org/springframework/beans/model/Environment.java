package org.springframework.beans.model;

/**
 * <p>〈功能概述〉.
 *
 * @author haisen /20228/22
 */
public class Environment {
    private Food food;

    public Environment() {
    }

    public Environment(Food food) {
        this.food = food;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
