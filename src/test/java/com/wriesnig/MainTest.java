package com.wriesnig;

import com.wriesnig.api.git.GitApi;
import com.wriesnig.api.stack.StackApi;
import com.wriesnig.db.expertise.ExpertiseDatabase;
import com.wriesnig.db.stack.StackDatabase;
import com.wriesnig.db.stack.stackdumpsconvert.ConvertApplication;
import com.wriesnig.expertise.Tags;
import com.wriesnig.gui.CharacterizerApplicationGui;
import com.wriesnig.utils.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MainTest {
    private final static String PROPERTIES_FILE_NAME = "src/main/resources/test/testConfig.properties";
    private Properties properties;

    @BeforeAll
    public static void setUpBeforeAll() {
        Logger.deactivatePrinting();
    }

    @BeforeEach
    public void setUp() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(PROPERTIES_FILE_NAME));
    }

    @Test
    public void shouldCallAllMethodsWhenCorrectArguments() {
        String fileName = "config.properties";
        String[] arguments = new String[]{fileName};
        try (MockedStatic<Main> mainMockedStatic = mockStatic(Main.class);) {
            mainMockedStatic.when(() -> Main.main(arguments)).thenCallRealMethod();
            Main.main(arguments);
            mainMockedStatic.verify(() -> Main.getPropertiesFromConfigFile(fileName), times(1));
            mainMockedStatic.verify(() -> Main.setGitApiProperties(any()), times(1));
            mainMockedStatic.verify(() -> Main.setStackApiKey(any()), times(1));
            mainMockedStatic.verify(() -> Main.setTags(any()), times(1));
            mainMockedStatic.verify(() -> Main.setDbCredentials(any()), times(1));
            mainMockedStatic.verify(Main::startApp, times(1));
        }
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenTooFewArguments() {
        assertThrows(RuntimeException.class, () -> Main.main(new String[]{}));
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenTooManyArguments() {
        assertThrows(RuntimeException.class, () -> Main.main(new String[]{"arg1", "arg2"}));
    }

    @Test
    public void shouldCallConvertApplicationWhenCArgument() {
        try (MockedConstruction<ConvertApplication> mockedConvertApp = mockConstruction(ConvertApplication.class)) {
            Main.main(new String[]{"c"});
            ConvertApplication convertApplication = mockedConvertApp.constructed().get(0);
            verify(convertApplication, times(1)).run();
        }
    }

    @Test
    public void shouldSetTags() {
        Main.setTags(properties);
        String[] tagsFromProperties = properties.get("tags").toString().split(",");
        for (int i = 0; i < tagsFromProperties.length; i++)
            assertEquals(tagsFromProperties[i], Tags.tagsToCharacterize[i]);
    }

    @Test
    public void shouldSetGitProperties() {
        try (MockedStatic<GitApi> mockedGitApi = mockStatic(GitApi.class)) {
            Main.setGitApiProperties(properties);
            mockedGitApi.verify(() -> GitApi.setToken(properties.getProperty("git.token")), times(1));
            mockedGitApi.verify(() -> GitApi.setReposMaxSizeInMB(Integer.parseInt(properties.getProperty("git.max.repo.size.mb"))), times(1));
        }
    }

    @Test
    public void shouldSetStackKey() {
        try (MockedStatic<StackApi> mockedStackApi = mockStatic(StackApi.class)) {
            Main.setStackApiKey(properties);
            mockedStackApi.verify(() -> StackApi.setKey(properties.getProperty("stack.key")), times(1));
        }
    }

    @Test
    public void shouldSetDBCredentials() {
        try (MockedStatic<StackDatabase> mockedStackDb = mockStatic(StackDatabase.class);
             MockedStatic<ExpertiseDatabase> mockedExpertiseDb = mockStatic(ExpertiseDatabase.class)) {
            Main.setDbCredentials(properties);
            mockedStackDb.verify(() -> StackDatabase.setCredentials(
                            properties.getProperty("dumpsDB.user"), properties.getProperty("dumpsDB.password"), properties.getProperty("dumpsDB.url")),
                    times(1));
            mockedExpertiseDb.verify(() -> ExpertiseDatabase.setCredentials(
                            properties.getProperty("expertiseDB.user"), properties.getProperty("expertiseDB.password"), properties.getProperty("expertiseDB.url")),
                    times(1));
        }
    }

    @Test
    public void shouldStartGUIApplication() {
        try (MockedConstruction<CharacterizerApplicationGui> mockedGui = mockConstruction(CharacterizerApplicationGui.class)) {
            Main.startApp();
            assertEquals(1, mockedGui.constructed().size());
        }
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenConfigFileNotFound() {
        assertThrows(RuntimeException.class, () -> Main.getPropertiesFromConfigFile("nonExistingFile.txt"));
    }

    @Test
    public void shouldThrowRuntimeException() {
        try (MockedConstruction<Properties> mockedProperties = getMockedPropertiesThatThrowsIOException()) {
            assertThrows(RuntimeException.class, () -> Main.getPropertiesFromConfigFile("src/main/resources/test/testConfig.properties"));
            assertEquals(1, mockedProperties.constructed().size());
        }
    }

    private MockedConstruction<Properties> getMockedPropertiesThatThrowsIOException() {
        return mockConstruction(Properties.class,
                (mock, context) -> {
                    doThrow(IOException.class).when(mock).load((InputStream) any());
                });
    }

    @Test
    public void shouldReturnProperties(){
        Properties testProperties = Main.getPropertiesFromConfigFile(PROPERTIES_FILE_NAME);
        for(Object key: properties.keySet())
            assertEquals(properties.get(key), testProperties.get(key));
    }


    @AfterEach
    public void tearDown() {
        properties = null;
    }


}
