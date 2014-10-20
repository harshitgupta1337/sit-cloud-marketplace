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
import java.util.*;

/**
 * The <code>migrdecider</code> class provides a Java interface to the M-functions
 * from the files:
 * <pre>
 *  C:\\Program Files\\MATLAB\\R2013a\\bin\\migration.m
 * </pre>
 * The {@link #dispose} method <b>must</b> be called on a <code>migrdecider</code> 
 * instance when it is no longer needed to ensure that native resources allocated by this 
 * class are properly freed.
 * @version 0.0
 */
public class migrdecider extends MWComponentInstance<migrdecider>
{
    /**
     * Tracks all instances of this class to ensure their dispose method is
     * called on shutdown.
     */
    private static final Set<Disposable> sInstances = new HashSet<Disposable>();

    /**
     * Maintains information used in calling the <code>migration</code> M-function.
     */
    private static final MWFunctionSignature sMigrationSignature =
        new MWFunctionSignature(/* max outputs = */ 1,
                                /* has varargout = */ false,
                                /* function name = */ "migration",
                                /* max inputs = */ 1,
                                /* has varargin = */ false);

    /**
     * Shared initialization implementation - private
     */
    private migrdecider (final MWMCR mcr) throws MWException
    {
        super(mcr);
        // add this to sInstances
        synchronized(migrdecider.class) {
            sInstances.add(this);
        }
    }

    /**
     * Constructs a new instance of the <code>migrdecider</code> class.
     */
    public migrdecider() throws MWException
    {
        this(MigrdeciderMCRFactory.newInstance());
    }
    
    private static MWComponentOptions getPathToComponentOptions(String path)
    {
        MWComponentOptions options = new MWComponentOptions(new MWCtfExtractLocation(path),
                                                            new MWCtfDirectorySource(path));
        return options;
    }
    
    /**
     * @deprecated Please use the constructor {@link #migrdecider(MWComponentOptions componentOptions)}.
     * The <code>com.mathworks.toolbox.javabuilder.MWComponentOptions</code> class provides API to set the
     * path to the component.
     * @param pathToComponent Path to component directory.
     */
    public migrdecider(String pathToComponent) throws MWException
    {
        this(MigrdeciderMCRFactory.newInstance(getPathToComponentOptions(pathToComponent)));
    }
    
    /**
     * Constructs a new instance of the <code>migrdecider</code> class. Use this 
     * constructor to specify the options required to instantiate this component.  The 
     * options will be specific to the instance of this component being created.
     * @param componentOptions Options specific to the component.
     */
    public migrdecider(MWComponentOptions componentOptions) throws MWException
    {
        this(MigrdeciderMCRFactory.newInstance(componentOptions));
    }
    
    /** Frees native resources associated with this object */
    public void dispose()
    {
        try {
            super.dispose();
        } finally {
            synchronized(migrdecider.class) {
                sInstances.remove(this);
            }
        }
    }
  
    /**
     * Invokes the first m-function specified by MCC, with any arguments given on
     * the command line, and prints the result.
     */
    public static void main (String[] args)
    {
        try {
            MWMCR mcr = MigrdeciderMCRFactory.newInstance();
            mcr.runMain( sMigrationSignature, args);
            mcr.dispose();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    /**
     * Calls dispose method for each outstanding instance of this class.
     */
    public static void disposeAllInstances()
    {
        synchronized(migrdecider.class) {
            for (Disposable i : sInstances) i.dispose();
            sInstances.clear();
        }
    }

    /**
     * Provides the interface for calling the <code>migration</code> M-function 
     * where the first input, an instance of List, receives the output of the M-function and
     * the second input, also an instance of List, provides the input to the M-function.
     * <p>M-documentation as provided by the author of the M function:
     * <pre>
     * %UNTITLED Summary of this function goes here
     * %   Detailed explanation goes here
     * </pre>
     * </p>
     * @param lhs List in which to return outputs. Number of outputs (nargout) is
     * determined by allocated size of this List. Outputs are returned as
     * sub-classes of <code>com.mathworks.toolbox.javabuilder.MWArray</code>.
     * Each output array should be freed by calling its <code>dispose()</code>
     * method.
     *
     * @param rhs List containing inputs. Number of inputs (nargin) is determined
     * by the allocated size of this List. Input arguments may be passed as
     * sub-classes of <code>com.mathworks.toolbox.javabuilder.MWArray</code>, or
     * as arrays of any supported Java type. Arguments passed as Java types are
     * converted to MATLAB arrays according to default conversion rules.
     * @throws MWException An error has occurred during the function call.
     */
    public void migration(List lhs, List rhs) throws MWException
    {
        fMCR.invoke(lhs, rhs, sMigrationSignature);
    }

    /**
     * Provides the interface for calling the <code>migration</code> M-function 
     * where the first input, an Object array, receives the output of the M-function and
     * the second input, also an Object array, provides the input to the M-function.
     * <p>M-documentation as provided by the author of the M function:
     * <pre>
     * %UNTITLED Summary of this function goes here
     * %   Detailed explanation goes here
     * </pre>
     * </p>
     * @param lhs array in which to return outputs. Number of outputs (nargout)
     * is determined by allocated size of this array. Outputs are returned as
     * sub-classes of <code>com.mathworks.toolbox.javabuilder.MWArray</code>.
     * Each output array should be freed by calling its <code>dispose()</code>
     * method.
     *
     * @param rhs array containing inputs. Number of inputs (nargin) is
     * determined by the allocated size of this array. Input arguments may be
     * passed as sub-classes of
     * <code>com.mathworks.toolbox.javabuilder.MWArray</code>, or as arrays of
     * any supported Java type. Arguments passed as Java types are converted to
     * MATLAB arrays according to default conversion rules.
     * @throws MWException An error has occurred during the function call.
     */
    public void migration(Object[] lhs, Object[] rhs) throws MWException
    {
        fMCR.invoke(Arrays.asList(lhs), Arrays.asList(rhs), sMigrationSignature);
    }

    /**
     * Provides the standard interface for calling the <code>migration</code>
     * M-function with 1 input argument.
     * Input arguments may be passed as sub-classes of
     * <code>com.mathworks.toolbox.javabuilder.MWArray</code>, or as arrays of
     * any supported Java type. Arguments passed as Java types are converted to
     * MATLAB arrays according to default conversion rules.
     *
     * <p>M-documentation as provided by the author of the M function:
     * <pre>
     * %UNTITLED Summary of this function goes here
     * %   Detailed explanation goes here
     * </pre>
     * </p>
     * @param nargout Number of outputs to return.
     * @param rhs The inputs to the M function.
     * @return Array of length nargout containing the function outputs. Outputs
     * are returned as sub-classes of
     * <code>com.mathworks.toolbox.javabuilder.MWArray</code>. Each output array
     * should be freed by calling its <code>dispose()</code> method.
     * @throws MWException An error has occurred during the function call.
     */
    public Object[] migration(int nargout, Object... rhs) throws MWException
    {
        Object[] lhs = new Object[nargout];
        fMCR.invoke(Arrays.asList(lhs), 
                    MWMCR.getRhsCompat(rhs, sMigrationSignature), 
                    sMigrationSignature);
        return lhs;
    }
}
