package com.wriesnig.db.stack.stackdumpsconvert;

import com.wriesnig.utils.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ConvertApplicationTest {
    private ConvertApplication convertApplication;

    @BeforeEach
    public void setUp(){
        ConvertApplication.dataPath = "src/main/resources/test/convertApp";
        convertApplication = new ConvertApplication();
    }

    @Test
    public void runConvertJobs() throws IOException {
        try(MockedConstruction<ConvertJob> convertJob = mockConstruction(ConvertJob.class,
                (mock,context)->{

        })){
            convertApplication.run();
            assertEquals(3, convertJob.constructed().size());
            for(ConvertJob job: convertJob.constructed())
                verify(job, times(1)).convert();
        }
    }

    @Test
    public void convertJobThrowsException() throws IOException {
        try(MockedStatic<Logger> mockedLogger = mockStatic(Logger.class);
            MockedConstruction<ConvertJob> convertJob = mockConstruction(ConvertJob.class,
                (mock,context)->{
                    doThrow(IOException.class).when(mock).convert();
                })){
            convertApplication.run();

            List<ConvertJob> constructedJobs = convertJob.constructed();
            assertEquals(3, constructedJobs.size());
            verify(constructedJobs.get(0), times(1)).convert();
            verify(constructedJobs.get(1), times(0)).convert();
            verify(constructedJobs.get(2), times(0)).convert();
            mockedLogger.verify(()-> Logger.error(any(), any()),times(1));
        }
    }

    @AfterEach
    public void tearDown(){
        convertApplication = null;
    }
}
