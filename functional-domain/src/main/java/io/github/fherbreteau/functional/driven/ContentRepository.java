package io.github.fherbreteau.functional.driven;

import io.github.fherbreteau.functional.domain.entities.File;

import java.io.InputStream;

public interface ContentRepository {

    InputStream readContent(File item);

    void writeContent(File item, InputStream content);
}
