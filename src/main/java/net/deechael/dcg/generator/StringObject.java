package net.deechael.dcg.generator;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

public final class StringObject extends SimpleJavaFileObject {

    private final String content;

    public StringObject(URI uri, Kind kind, String content) {
        super(uri, kind);
        this.content = content;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return content;
    }

}
