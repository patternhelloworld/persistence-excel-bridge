package io.github.patternknife.pxb.config.response.error;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

public class CustomExceptionUtils {


    public static void createNonStoppableErrorMessage(String message, String ex, Logger logger){
        try {
            logger.error("[NON-STOPPABLE ERROR] : " + message + " / " + ex + " / " + " / Thread ID = " + Thread.currentThread().getId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getAllCausesWithStartMessage(Throwable e, String causes) {
        if (e.getCause() == null) return causes;
        causes += e.getCause() + " / ";
        return getAllCausesWithStartMessage(e.getCause(), causes);
    }

    public static String getAllCauses(Throwable e) {
        String causes = "";
        return getAllCausesWithStartMessage(e, causes);
    }

    public static String getAllStackTraces(Throwable e) {
        return ExceptionUtils.getStackTrace(e);
    }

    public static String getFirstTwoStackTraces(Throwable e) {
        StackTraceElement[] stackTraces = e.getStackTrace();
        StringBuilder sb = new StringBuilder();

        int count = Math.min(2, stackTraces.length);
        for (int i = 0; i < count; i++) {
            sb.append(stackTraces[i].toString());
            sb.append("\n");
        }

        return sb.toString();
    }

}
