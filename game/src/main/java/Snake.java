import java.util.ArrayList;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.awt.Graphics;

import java.awt.event.KeyEvent;

public class Snake {
    // TODO make them private at some point
    public ArrayList<SnakeBodyPart> snakeBody;
    public int playerID, xCorHead, yCorHead, length; 
    private boolean up, down, left, right = false;
    private RemoteSpace gameState, playerOneInput, playerOneOutput;

    public Snake(int playerID, int initXCorHead, int initYCorHead, int initLength) {
        this.playerID = playerID;
        this.snakeBody = new ArrayList<SnakeBodyPart>();
        this.xCorHead = initXCorHead;
        this.yCorHead = initYCorHead;
        this.length = initLength;
    }}