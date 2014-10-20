/*
 * MATLAB Compiler: 4.18.1 (R2013a)
 * Date: Mon Oct 20 14:48:47 2014
 * Arguments: "-B" "macro_default" "-W" "java:migrdecider,migrdecider" "-T" "link:lib" 
 * "-d" "C:\\Program Files\\MATLAB\\R2013a\\bin\\migrdecider\\src" "-N" "-p" "fuzzy" "-w" 
 * "enable:specified_file_mismatch" "-w" "enable:repeated_file" "-w" 
 * "enable:switch_ignored" "-w" "enable:missing_lib_sentinel" "-w" "enable:demo_license" 
 * "-v" "class{migrdecider:C:\\Program Files\\MATLAB\\R2013a\\bin\\migration.m}" "-a" 
 * "C:\\Program Files\\MATLAB\\R2013a\\bin\\migrationdeci1.fis" 
 */

package migrdecider;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class MigrdeciderMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "migrdecider_26B918250A47A13DEB72B0D13394B581";
    
    /** Component name */
    private static final String sComponentName = "migrdecider";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(MigrdeciderMCRFactory.class)
        );
    
    
    private MigrdeciderMCRFactory()
    {
        // Never called.
    }
    
    public static MWMCR newInstance(MWComponentOptions componentOptions) throws MWException
    {
        if (null == componentOptions.getCtfSource()) {
            componentOptions = new MWComponentOptions(componentOptions);
            componentOptions.setCtfSource(sDefaultComponentOptions.getCtfSource());
        }
        return MWMCR.newInstance(
            componentOptions, 
            MigrdeciderMCRFactory.class, 
            sComponentName, 
            sComponentId,
            new int[]{8,1,0}
        );
    }
    
    public static MWMCR newInstance() throws MWException
    {
        return newInstance(sDefaultComponentOptions);
    }
}
