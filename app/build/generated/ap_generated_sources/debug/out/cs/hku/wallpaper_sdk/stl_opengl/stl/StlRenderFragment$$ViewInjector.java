// Generated code from Butter Knife. Do not modify!
package cs.hku.wallpaper_sdk.stl_opengl.stl;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class StlRenderFragment$$ViewInjector<T extends cs.hku.wallpaper_sdk.stl_opengl.stl.StlRenderFragment> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165353, "field 'stlView'");
    target.stlView = finder.castView(view, 2131165353, "field 'stlView'");
  }

  @Override public void reset(T target) {
    target.stlView = null;
  }
}
