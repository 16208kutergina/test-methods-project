package org.nsu.fit.tm_backend.manager;

import org.slf4j.Logger;
import org.nsu.fit.tm_backend.database.IDBService;

public class ParentManager {
    protected IDBService dbService;
    protected Logger log;

    public ParentManager(IDBService dbService, Logger log) {
        this.dbService = dbService;
        this.log = log;
    }
}
