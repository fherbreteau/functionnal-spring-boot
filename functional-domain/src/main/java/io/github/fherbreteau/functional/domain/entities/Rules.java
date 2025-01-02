package io.github.fherbreteau.functional.domain.entities;

import java.util.StringTokenizer;

public record Rules(String content) {

    public String toString() {
        StringTokenizer tokenizer = new StringTokenizer(content, "\r\n");
        String start = "";
        String end = "";
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (start.isEmpty()) {
                start = token;
            }
            end = token;
        }
        return String.format("%s ... %s", start, end);
    }
}
