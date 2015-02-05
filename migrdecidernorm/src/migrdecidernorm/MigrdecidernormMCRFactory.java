/*
 * MATLAB Compiler: 4.18.1 (R2013a)
 * Date: Wed Feb 04 21:39:35 2015
 * Arguments: "-B" "macro_default" "-W" "java:migrdecidernorm,migrdecidernorm" "-T" 
 * "link:lib" "-d" "C:\\Users\\MR-CUUUL\\migrdecidernorm\\src" "-N" "-p" "fuzzy" "-w" 
 * "enable:specified_file_mismatch" "-w" "enable:repeated_file" "-w" 
 * "enable:switch_ignored" "-w" "enable:missing_lib_sentinel" "-w" "enable:demo_license" 
 * "-v" "class{migrdecidernorm:C:\\Program Files\\MATLAB\\R2013a\\bin\\migration_norm.m}" 
 * "-a" "C:\\Program Files\\MATLAB\\R2013a\\bin\\migrationdeci0_1.fis" 
 */

package migrdecidernorm;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class MigrdecidernormMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "migrdecidern_1B306E5C45336FAF180C4428ACF86D04";
    
    /** Component name */
    private static final String sComponentName = "migrdecidernorm";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(MigrdecidernormMCRFactory.class)
        );
    
    
    private MigrdecidernormMCRFactory()
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
            MigrdecidernormMCRFactory.class, 
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
