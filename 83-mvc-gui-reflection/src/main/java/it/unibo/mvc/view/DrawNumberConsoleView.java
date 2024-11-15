package it.unibo.mvc.view;

import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.api.DrawResult;

public class DrawNumberConsoleView implements DrawNumberView {

    private static final String FRAME_NAME = "Draw Number App";
    private static final String NEW_GAME = ": a new game starts!";

    private DrawNumberController controller;

    @Override
    public void setController(final DrawNumberController observer) {
        this.controller = observer;
    }

    @Override
    public void start() {
        System.out.println(FRAME_NAME + NEW_GAME);
    }

    @Override
    public void result(final DrawResult res) {
        switch (res) {
            case YOURS_HIGH, YOURS_LOW -> {
                System.out.println(res.getDescription());
                return;
            }
            case YOU_WON -> System.out.println(res.getDescription() + NEW_GAME);
            case YOU_LOST -> System.out.println(res.getDescription() + NEW_GAME);
            default -> throw new IllegalStateException("Unknown game state");
        }
        controller.resetGame();
    }
}
