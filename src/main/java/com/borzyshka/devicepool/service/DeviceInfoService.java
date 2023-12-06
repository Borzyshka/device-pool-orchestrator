package com.borzyshka.devicepool.service;

import com.borzyshka.devicepool.service.dto.DeviceInfo;
import com.borzyshka.devicepool.service.util.RuntimeCommandExecutor;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
@Slf4j
public class DeviceInfoService {

    private static final String getAllDevicesADB = "adb devices";
    private static final String uninstallPackegeADB = "adb -S %s uninstall %s";
    private static final List<String> uiAutomatorServers = List.of(
            "io.appium.uiautomator2.server",
            "io.appium.uiautomator2.server.test"
    );
    private static final Pattern deviceIdPattern = Pattern.compile("^(\\d|\\w)+");

    @NonNull
    private final RuntimeCommandExecutor commandExecutor;

    public List<DeviceInfo> getAll() {

        return commandExecutor.executeCommand(getAllDevicesADB).stream()
                .filter(line -> !line.contains("List of devices attached"))
                .map(line -> {
                    final Matcher idMatcher = deviceIdPattern.matcher(line);
                    return idMatcher.find() ? idMatcher.group() : StringUtils.EMPTY;
                }).filter(StringUtils::isNotEmpty).map(DeviceInfo::new).toList();
    }

    public void cleanDevice(@NonNull String udid) {
        uiAutomatorServers.forEach(server -> commandExecutor.executeCommand(String.format(uninstallPackegeADB, udid, server)));
    }

}
