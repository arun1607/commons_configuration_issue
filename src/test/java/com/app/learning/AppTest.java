package com.app.learning;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void loadOfEscapedFields() throws Exception {
        final File propFile = new File(tempFolder.getRoot(), "paging.properties");
        if (!propFile.exists()) {
            propFile.createNewFile();
        }
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> configurationBuilder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(params.fileBased()
                        .setFile(propFile).setListDelimiterHandler(new DefaultListDelimiterHandler(',')));
        configurationBuilder.setAutoSave(true);
        FileBasedConfiguration configuration = configurationBuilder.getConfiguration();
        configuration.setProperty("pagination.token.field.value.extract.pattern", "directoryObjects\\/\\$\\/Microsoft\\.DirectoryServices\\.(\\w+)\\?\\$skiptoken=(?<paramValue>.*)");
        configurationBuilder.save();

        Properties properties = new Properties();
//properties.setProperty("pagination.token.field.value.extract.pattern", "directoryObjects\\/\\$\\/Microsoft\\.DirectoryServices\\.(\\w+)\\?\\$skiptoken=(?<paramValue>.*)");
        try (final InputStream inputStream = new FileInputStream(propFile)) {
            properties.load(inputStream);
        }
        String pattern = properties.getProperty("pagination.token.field.value.extract.pattern");
        assertFalse("directoryObjects\\/\\$\\/Microsoft\\.DirectoryServices\\.(\\w+)\\?\\$skiptoken=(?<paramValue>.*)".equals(pattern));
        assertEquals("directoryObjects\\/\\$\\/Microsoft\\.DirectoryServices\\.(\\w+)\\?\\$skiptoken=(?<paramValue>.*)", configuration.getString("pagination.token.field.value.extract.pattern"));
    }
}