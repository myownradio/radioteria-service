package com.radioteria.player.api;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

public interface AudioDecoder {
    InputStream getDecodedStream(String filename) throws UnsupportedAudioFileException, IOException;
}
