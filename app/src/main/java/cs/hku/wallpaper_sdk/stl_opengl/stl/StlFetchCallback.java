package cs.hku.wallpaper_sdk.stl_opengl.stl;

public interface StlFetchCallback {
    void onBefore();
    void onProgress(int progress);
    void onFinish(STLObject stlObject);
    void onError();
}
