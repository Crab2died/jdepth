package com.github.database;

import org.junit.Test;

import static com.stratio.cassandra.lucene.builder.Builder.*;

public class CassandraLucenePlugin {

    @Test
    public void test() {

        String filter = search()
                .filter(bool()
                        .must(match("c_a", "11"), match("c_b", "22"))
                        .should(match("c_c", "33"), match("c_d", "44"))
                )
                .build();
        System.out.println(filter);
    }
}
