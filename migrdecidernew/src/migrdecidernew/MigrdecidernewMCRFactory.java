/*
 * MATLAB Compiler: 4.18.1 (R2013a)
 * Date: Thu Nov 06 14:19:16 2014
 * Arguments: "-B" "macro_default" "-W" "java:migrdecidernew,migrdecidernew" "-T" 
 * "link:lib" "-d" "C:\\Program Files\\MATLAB\\R2013a\\bin\\migrdecidernew\\src" "-N" 
 * "-p" "fuzzy" "-w" "enable:specified_file_mismatch" "-w" "enable:repeated_file" "-w" 
 * "enable:switch_ignored" "-w" "enable:missing_lib_sentinel" "-w" "enable:demo_license" 
 * "-v" "class{migrdecidernew:C:\\Program 
 * Files\\MATLAB\\R2013a\\bin\\migration_shrink.m}" "-a" "C:\\Program 
 * Files\\MATLAB\\R2013a\\bin\\migrationdeci2.fis" 
 */

package migrdecidernew;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class MigrdecidernewMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "migrdecidern_AAA664AE8D5164EC2F1F2C3F14F56944";
    
    /** Component name */
    private static final String sComponentName = "migrdecidernew";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(MigrdecidernewMCRFactory.class)
        );
    
    
    private MigrdecidernewMCRFactory()
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
            MigrdecidernewMCRFactory.class, 
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
