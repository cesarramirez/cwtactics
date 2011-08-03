package com.cwt.game;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

/**
 *
 * @author BlackCat [blackcat.myako@gmail.com]
 */
public class RhinoEnvironment {

    public static final String[] paths = new String[]{ "/jsLib",
                                                 "/com/cwt/game",
                                                 "/com/meowEngine",
                                                 "/com/nekoJS" };
    
    public static final RhinoEnvironment INSTANCE = new RhinoEnvironment();
    private final Context context;
    private final ScriptableObject rootScope;
    private static final Logger logger = Logger.getLogger("NekoJS");

    static {

        // setup logger
        SimpleFormatter formatter = new SimpleFormatter();
        ConsoleHandler cHandler = new ConsoleHandler();
        cHandler.setFormatter(formatter);
        logger.setUseParentHandlers(false);
        logger.addHandler(cHandler);
    }

    private RhinoEnvironment() {

        context = Context.enter();
        context.setOptimizationLevel(9);
        rootScope = context.initStandardObjects();

        // inject logger
        context.evaluateString(rootScope, "NEKO_SYS_LOGGER = "
                + "function( msg ){"
                + " com.cwt.game.RhinoEnvironment.INSTANCE.log(msg);"
                + "}",
                "", 0, null);

        // inject wait function
        context.evaluateString(rootScope, "NEKO_SYS_WAIT = "
                + "function( time ){"
                + " try{ java.lang.System.sleep(time); } "
                + " catch(e){}"
                + "}",
                "", 0, null);

        // inject file loader function
        context.evaluateString(rootScope, "NEKO_SYS_LOADFILE  = "
                + "function( time ){"
                + " try{  "
                + " "
                + " }"
                + " catch(e){}"
                + "}",
                "", 0, null);
        
        initSecurity();
    }

    /**
     * Set ups the security settings of MeowEngine to prevent miss using of
     * the scripting features, e.g. to make cheating possible.
     */
    private void initSecurity() {
        /*
         * COMMENTED OUT FOR DEBUG PROCESS
         *

        // initializes the class shutter
        context.setClassShutter( new ClassShutter(){
        public boolean visibleToScripts( String className )
        {
        return className.startsWith( SCRIPT_API_PACKAGE );
        }
        });


         */
        // TODO implement sandbox feature.. if neccessary
        // http:codeutopia.net/blog/2009/01/02/sandboxing-rhino-in-java/
    }

    public void log(String message) {
        logger.info(message);
    }

    private Context getContext() {
        return context;
    }

    private ScriptableObject getRootContext() {
        return rootScope;
    }

    @Override
    protected void finalize() throws Throwable {

        context.exit();
        super.finalize();
    }

    private static class SimpleFormatter extends Formatter {

        @Override
        public String format(LogRecord rec) {
            StringBuilder buf = new StringBuilder(1000);

            buf.append("Neko:: ");
            buf.append(rec.getMessage());
            buf.append("\n");

            return buf.toString();
        }
    }

    public static void main(String[] args){

        INSTANCE.log("START NEKO TEST");
    }
}
