/*
 * MATLAB Compiler: 4.18.1 (R2013a)
 * Date: Thu Nov 06 13:37:19 2014
 * Arguments: "-B" "macro_default" "-W" "java:fuzzyopt2,fuzopt2" "-T" "link:lib" "-d" 
 * "C:\\Program Files\\MATLAB\\R2013a\\bin\\fuzzyopt2\\src" "-N" "-p" "fuzzy" "-w" 
 * "enable:specified_file_mismatch" "-w" "enable:repeated_file" "-w" 
 * "enable:switch_ignored" "-w" "enable:missing_lib_sentinel" "-w" "enable:demo_license" 
 * "-v" "class{fuzopt2:C:\\Program Files\\MATLAB\\R2013a\\bin\\dynafis4.m}" 
 */

package fuzzyopt2;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class Fuzzyopt2MCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "fuzzyopt2_ADCF4F178F09D8D28E6F6CC59E36501E";
    
    /** Component name */
    private static final String sComponentName = "fuzzyopt2";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(Fuzzyopt2MCRFactory.class)
        );
    
    
    private Fuzzyopt2MCRFactory()
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
            Fuzzyopt2MCRFactory.class, 
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
