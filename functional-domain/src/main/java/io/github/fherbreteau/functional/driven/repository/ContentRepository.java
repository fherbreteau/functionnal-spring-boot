package io.github.fherbreteau.functional.driven.repository;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;

import java.io.InputStream;

public interface ContentRepository {

    Output<Item> initContent(File item);

    Output<InputStream> readContent(File item);

    Output<Item> writeContent(File item, InputStream content);

    Output<Void> deleteContent(File item);
}
