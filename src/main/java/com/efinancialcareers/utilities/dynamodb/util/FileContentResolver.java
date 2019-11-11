package com.efinancialcareers.utilities.dynamodb.util;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

@Component
public class FileContentResolver {

    private final ApplicationContext applicationContext;
    private final Map<String, String> fileContentCache = new HashMap<>();

    @Autowired
    public FileContentResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String resolveFileContent(String filePath) {
        hasText(filePath, "FilePath can't be null");

        String cachedContent = fileContentCache.get(filePath);
        if (cachedContent != null) {
            return cachedContent;
        }

        Resource resource = applicationContext.getResource(filePath);
        notNull(resource, "Resource with given file path is not found");

        try {
            String fileContent = FileUtils.readFileToString(resource.getFile(), "UTF-8");
            fileContentCache.put(filePath, fileContent);

            return fileContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
