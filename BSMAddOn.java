package brainstone;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface BSMAddOn
{
  public abstract String[] texture();

  public abstract String[] foreignTexture();

  public abstract int[] side();

  public abstract int[] bottom();
}