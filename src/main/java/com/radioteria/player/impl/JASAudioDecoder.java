package com.radioteria.player.impl;

import com.radioteria.player.api.AudioDecoder;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class JASAudioDecoder implements AudioDecoder {
    @Override
    public InputStream getDecodedStream(String filename) throws UnsupportedAudioFileException, IOException {
        return AudioSystem.getAudioInputStream(new File(filename));
    }
}
