package storage;

import com.mongodb.MongoClient;

/**
 * Created by user on 2016-02-08.
 */
public class MongoDB {
    // To directly connect to a single MongoDB server
// (this will not auto-discover the primary even if it's a member of a replica set)
    MongoClient mongoClient = new MongoClient();

}
