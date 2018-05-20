package com.zoovisitors.backend.map;

import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by aviv on 16-Mar-18.
 */

public class Point {
    int x;
    int y;
    private Set<Animal.PersonalStories> closestAnimalStories;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        closestAnimalStories = new HashSet<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void addCloseAnimalStory(Animal.PersonalStories animalStory) {
        closestAnimalStories.add(animalStory);
    }

    public Set<Animal.PersonalStories> getClosestAnimalStories() {
        return closestAnimalStories;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
