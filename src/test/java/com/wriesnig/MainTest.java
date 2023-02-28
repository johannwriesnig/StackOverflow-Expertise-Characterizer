package com.wriesnig;

import com.wriesnig.api.git.GitApi;
import com.wriesnig.api.stack.StackApi;
import com.wriesnig.db.expertise.ExpertiseDatabase;
import com.wriesnig.db.stack.StackDatabase;
import com.wriesnig.db.stack.stackdumpsconvert.ConvertApplication;
import com.wriesnig.expertise.Tags;
import com.wriesnig.gui.CharacterizerApplicationGui;
import com.wriesnig.utils.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MainTest {
    private final String[] configPropertiesAsParameter = new String[]{"src/main/resources/src/testConfig.properties"};

    @BeforeAll
    public static void setUp() {
        Logger.deactivatePrinting();
    }


    @Test
    public void isSettingGitToken() {
        try (MockedStatic<GitApi> mockedGitApi = mockStatic(GitApi.class);
             MockedConstruction<CharacterizerApplicationGui> mockedGui = mockConstruction(CharacterizerApplicationGui.class)) {
            Main.main(configPropertiesAsParameter);
            mockedGitApi.verify(() -> GitApi.setToken("gitToken"), times(1));
            assertEquals(1, mockedGui.constructed().size());
        }
    }

    @Test
    public void isSettingStackKey() {
        try (MockedStatic<StackApi> mockedStackApi = mockStatic(StackApi.class);
             MockedConstruction<CharacterizerApplicationGui> mockedGui = mockConstruction(CharacterizerApplicationGui.class)) {
            Main.main(configPropertiesAsParameter);
            mockedStackApi.verify(() -> StackApi.setKey("stackKey"), times(1));
            assertEquals(1, mockedGui.constructed().size());
        }
    }

    @Test
    public void isSettingStackDbCredentials() {
        try (MockedStatic<StackDatabase> mockedStackDb = mockStatic(StackDatabase.class);
             MockedConstruction<CharacterizerApplicationGui> mockedGui = mockConstruction(CharacterizerApplicationGui.class)) {
            Main.main(configPropertiesAsParameter);
            mockedStackDb.verify(() -> StackDatabase.setCredentials("dumpsUser", "dumpsPassword", "dumpsUrl"),
                    times(1));
            assertEquals(1, mockedGui.constructed().size());
        }
    }

    @Test
    public void isSettingExpertiseDbCredentials() {
        try (MockedStatic<ExpertiseDatabase> mockedExpertiseDb = mockStatic(ExpertiseDatabase.class);
             MockedConstruction<CharacterizerApplicationGui> mockedGui = mockConstruction(CharacterizerApplicationGui.class)) {
            Main.main(configPropertiesAsParameter);
            mockedExpertiseDb.verify(() -> ExpertiseDatabase.setCredentials("expertiseUser", "expertisePassword", "expertiseUrl"),
                    times(1));
            assertEquals(1, mockedGui.constructed().size());
        }
    }

    @Test
    public void isStartingGui() {
        try (MockedConstruction<CharacterizerApplicationGui> mockedGui = mockConstruction(CharacterizerApplicationGui.class)) {
            Main.main(configPropertiesAsParameter);
            assertEquals(1, mockedGui.constructed().size());
        }
    }

    @Test
    public void isSettingTags() {
        try (MockedConstruction<CharacterizerApplicationGui> mockedGui = mockConstruction(CharacterizerApplicationGui.class)) {
            Main.main(configPropertiesAsParameter);
            assertEquals(Arrays.toString(new String[]{"tag1", "tag2"}), Arrays.toString(Tags.tagsToCharacterize));
            assertEquals(1, mockedGui.constructed().size());
        }
    }


    @Test
    public void isConvertApplicationCalledWithCParameter() {
        try (MockedConstruction<ConvertApplication> mockedConvertApp = mockConstruction(ConvertApplication.class);
             MockedConstruction<CharacterizerApplicationGui> mockedGui = mockConstruction(CharacterizerApplicationGui.class)) {
            Main.main(new String[]{"c"});
            ConvertApplication app = mockedConvertApp.constructed().get(0);
            verify(app, times(1)).run();
            assertEquals(0, mockedGui.constructed().size());
        }
    }

    @Test
    public void noArgumentsReturns() {
        try (MockedConstruction<CharacterizerApplicationGui> mockedGui = mockConstruction(CharacterizerApplicationGui.class)) {
            Main.main(new String[]{});
            assertEquals(0, mockedGui.constructed().size());
        }

    }

    @Test
    public void nonExistingConfigFileThrowsRuntimeException() {
        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class);
             MockedConstruction<CharacterizerApplicationGui> mockedGui = mockConstruction(CharacterizerApplicationGui.class)) {
            assertThrows(RuntimeException.class, () -> Main.main(new String[]{"nonExisting.properties"}));
            mockedLogger.verify(() -> Logger.error(any(), any()), times(1));
            assertEquals(0, mockedGui.constructed().size());
        }
    }

    @Test
    public void processingConfigFileThrowsIOException() {
        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class);
             MockedConstruction<CharacterizerApplicationGui> mockedGui = mockConstruction(CharacterizerApplicationGui.class);
             MockedConstruction<Properties> mockedProperties = mockConstruction(Properties.class,
                     (mock, context) -> {
                         doThrow(IOException.class).when(mock).load((InputStream) any());
                     });) {
            assertThrows(RuntimeException.class, () -> Main.main(configPropertiesAsParameter));
            mockedLogger.verify(() -> Logger.error(any(), any()), times(1));
            assertEquals(0, mockedGui.constructed().size());
        }
    }

}
