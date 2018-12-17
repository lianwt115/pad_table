package tabledemo.ctl.com.from.data.format.count;

/**
 * Created by huang on 2017/11/6.
 */

public interface ICountFormat<T,N extends Number> {

    void count(T t);

    N getCount();

    String getCountString();

    void clearCount();
}
