/*
 * MATLAB Compiler: 4.18.1 (R2013a)
 * Date: Wed Oct 08 21:04:00 2014
 * Arguments: "-B" "macro_default" "-W" "java:fuzzyopt1,fuzopt1" "-T" "link:lib" "-d" 
 * "C:\\Users\\MR-CUUUL\\fuzzyopt1\\src" "-N" "-p" "fuzzy" "-w" 
 * "enable:specified_file_mismatch" "-w" "enable:repeated_file" "-w" 
 * "enable:switch_ignored" "-w" "enable:missing_lib_sentinel" "-w" "enable:demo_license" 
 * "-v" "class{fuzopt1:C:\\Program Files\\MATLAB\\R2013a\\bin\\dynafis3.m}" 
 */

package fuzzyopt1;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class Fuzzyopt1MCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "fuzzyopt1_E731F66BE8A1458B9ED0C4F60858F30B";
    
    /** Component name */
    private static final String sComponentName = "fuzzyopt1";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(Fuzzyopt1MCRFactory.class)
        );
    
    
    private Fuzzyopt1MCRFactory()
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
            Fuzzyopt1MCRFactory.class, 
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
