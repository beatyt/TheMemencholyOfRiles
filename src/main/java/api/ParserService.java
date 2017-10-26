package api;

import java.io.IOException;

/**
 * Created by user on 2016-02-10.
 */
public interface ParserService {

    void call(String headline) throws IOException;
}
