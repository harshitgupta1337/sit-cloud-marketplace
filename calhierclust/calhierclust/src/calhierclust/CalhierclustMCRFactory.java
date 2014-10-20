/*
 * MATLAB Compiler: 4.18.1 (R2013a)
 * Date: Thu Oct 09 16:54:52 2014
 * Arguments: "-B" "macro_default" "-W" "java:calhierclust,calclust" "-T" "link:lib" "-d" 
 * "C:\\Program Files\\MATLAB\\R2013a\\bin\\calhierclust\\src" "-N" "-p" "nnet" "-p" 
 * "stats" "-w" "enable:specified_file_mismatch" "-w" "enable:repeated_file" "-w" 
 * "enable:switch_ignored" "-w" "enable:missing_lib_sentinel" "-w" "enable:demo_license" 
 * "-v" "class{calclust:C:\\Program Files\\MATLAB\\R2013a\\bin\\calhierclust.m}" 
 */

package calhierclust;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class CalhierclustMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "calhierclust_4D8321C4A76983F28D9420F6F21D9131";
    
    /** Component name */
    private static final String sComponentName = "calhierclust";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(CalhierclustMCRFactory.class)
        );
    
    
    private CalhierclustMCRFactory()
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
            CalhierclustMCRFactory.class, 
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
