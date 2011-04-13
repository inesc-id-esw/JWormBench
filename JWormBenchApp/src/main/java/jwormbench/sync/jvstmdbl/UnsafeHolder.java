package jwormbench.sync.jvstmdbl;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.misc.Unsafe;

/**
 * From Deuce framework.
 *  
 * @author Guy Korland
 */
public class UnsafeHolder {

  final private static Logger logger = Logger.getLogger("jvstm.reflection");

  final private static Unsafe unsafe;
  static{
    Unsafe unsafeValue = null;
    try{
      Field field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      unsafeValue = (Unsafe)field.get(null);
    }catch( Exception e){
      logger.log(Level.SEVERE, "Fail to initialize Unsafe.", e);
    }
    unsafe = unsafeValue;
  }

  public static Unsafe getUnsafe() {
    return unsafe;
  }
}
