package com.radioteria.player.broadcast;


import javax.servlet.WriteListener;
import java.io.IOException;

public class BaseWriteListener implements WriteListener {
    @Override
    public void onWritePossible() throws IOException {
    }

    @Override
    public void onError(Throwable throwable) {
    }
}
