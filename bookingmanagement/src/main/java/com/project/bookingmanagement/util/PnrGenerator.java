package com.project.bookingmanagement.util;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class PnrGenerator {

    private static final String CHARACTERS =
            "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private static final int PNR_LENGTH = 6;

    private final SecureRandom random = new SecureRandom();

    public String generate() {

        StringBuilder pnr = new StringBuilder(PNR_LENGTH);

        for (int i = 0; i < PNR_LENGTH; i++) {
            pnr.append(
                    CHARACTERS.charAt(
                            random.nextInt(CHARACTERS.length())));
        }

        return pnr.toString();
    }
}