/*
 * Pathfinder
 * Copyright (C) 2013  Tayler Mulligan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package controller;

import processing.core.PApplet;
import processing.core.PFont;

public class GUIManager {

    private PApplet parent;
    private Algorithm[] algorithms;

    private AlgorithmType selection;

    private int rectWidth;
    private int rectHeight;

    private PFont font;
    public TickBox repeat;

    public GUIManager(PApplet parent) {

        this.rectWidth = (int)((parent.width) * (9.0/10.0));
        this.rectHeight = (int)(parent.height/10.0);

        double y = this.rectHeight*(2.0/10.0);
        double x = this.rectWidth*(2.0/10.0);

        this.font = parent.createFont("Arial", (float)Math.sqrt((y*y) + (x*x)));

        AlgorithmType[] algorithmTypes = AlgorithmType.values();
        this.algorithms = new Algorithm[algorithmTypes.length];

        this.parent = parent;

        this.selection = null;

        for (int index=0; index < algorithmTypes.length; index++) {
            this.algorithms[index] = new Algorithm(algorithmTypes[index]);
            this.algorithms[index].topLeft[0] = ((parent.width/2) - (rectWidth/2));
            this.algorithms[index].topLeft[1] = (index*rectHeight)+10+(int)(10*(index/1.0));
            this.algorithms[index].bottomRight[0] = ((parent.width/2) + (rectWidth/2));
            this.algorithms[index].bottomRight[1] = this.algorithms[index].topLeft[1] + rectHeight;
        }
        this.repeat = new TickBox("Repeat", this.algorithms[this.algorithms.length-1].bottomRight[1]+20, parent.width/2-100);
    }

    public AlgorithmType getSelection() {
        if (this.repeat.isTrue() && this.selection != null) {
            return this.selection;
        }

        drawGUI();

        if (parent.mousePressed) {
            int mouseX = parent.mouseX;
            int mouseY = parent.mouseY;

            if (this.repeat.within(parent.mouseX, parent.mouseY)) {
                this.selection = null;
                this.repeat.toggle();
            }

            for (int index=0; index < algorithms.length; index++) {
                if (algorithms[index].within(mouseX, mouseY)) {
                    this.selection = algorithms[index].type;
                    return this.selection;
                }
            }
        }
        return null;
    }

    public void clearSelection() {
        this.selection = null;
    }

    private void drawGUI() {
        parent.background(0);

        parent.stroke(140);
        parent.fill(30);
        parent.strokeWeight(this.repeat.strokeWeight);
        parent.rectMode(parent.CENTER);

        parent.rect(this.repeat.position[0], this.repeat.position[1], this.repeat.size, this.repeat.size);
        parent.textAlign(parent.LEFT);
        parent.fill(200);
        parent.text(this.repeat.text, this.repeat.position[0]+this.repeat.size+2, this.repeat.position[1]+this.repeat.size/2);

        if (this.repeat.isTrue()) {
            parent.line(this.repeat.position[0]-this.repeat.size/2, this.repeat.position[1]-this.repeat.size/2,
                        this.repeat.position[0]+this.repeat.size/2, this.repeat.position[1]+this.repeat.size/2);

            parent.line(this.repeat.position[0]+this.repeat.size/2, this.repeat.position[1]+this.repeat.size/2,
                        this.repeat.position[0]-this.repeat.size/2, this.repeat.position[1]-this.repeat.size/2);
        }

        this.parent.rectMode(parent.CORNERS);
        for (int index=0; index < algorithms.length; index++) {
            Algorithm algorithm = algorithms[index];

            if (algorithm.within(parent.mouseX, parent.mouseY)) {
                parent.fill(100);
            }
            else {
                parent.fill(30);
            }

            parent.rect(algorithm.topLeft[0], algorithm.topLeft[1], algorithm.bottomRight[0], algorithm.bottomRight[1]);
            parent.textAlign(parent.CENTER);
            parent.fill(230);
            parent.text(algorithm.title, (algorithm.topLeft[0]+algorithm.bottomRight[0])/2,
                       (algorithm.topLeft[1]+algorithm.bottomRight[1])/2);

        }
    }
}

class Algorithm {
    AlgorithmType type;

    int[] topLeft;
    int[] bottomRight;

    String title;

    Algorithm(AlgorithmType type) {
        this.type = type;
        this.topLeft = new int[2];
        this.bottomRight = new int[2];
        this.title = type.toString();
    }

    boolean within(int mouseX, int mouseY) {
        if (mouseX >= topLeft[0] && mouseX <= bottomRight[0]) {
            if (mouseY >= topLeft[1] && mouseY <= bottomRight[1]) {
                return true;
            }
        }
        return false;
    }
}

class TickBox {
    String text;
    int[] position;
    int size = 10;

    boolean state;
    public float strokeWeight = 2;

    TickBox(String text, int x, int y) {
        this.text = text;

        this.position = new int[] {x, y};
        this.state = false;
    }

    boolean isTrue() {
        return this.state;
    }

    boolean within(int mouseX, int mouseY) {
        if (mouseX >= position[0]-this.size/2 && mouseX <= position[0]+this.size/2) {
            if (mouseY >= position[1]-this.size/2 && mouseY <= position[1]+this.size/2) {
                return true;
            }
        }
        return false;
    }

    public void toggle() {
        this.state = !this.state;
    }

    public void set(boolean state) {
        this.state = state;
    }
}