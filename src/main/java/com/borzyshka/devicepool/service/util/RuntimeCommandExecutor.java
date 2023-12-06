package com.borzyshka.devicepool.service.util;

import com.borzyshka.devicepool.service.exception.RuntimeCommandExecutionException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RuntimeCommandExecutor {


    @NonNull
    public List<String> executeCommand(@NonNull String command) {

        final List<String> results = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                results.add(line);
            }
            reader.close();

        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeCommandExecutionException(command);
        }

        return results;
    }
}
